# Hatch and Spring-AOP #

For you propeller-heads who've been drinking the AOP kool-ade, the `hatch-spring` component allows you to insert Hatch timers into your class via Spring or aspectJ aspect injection.

## Advising Classes ##

Attaching hatch-timer to classes using Spring's AOP supprt is relatively straightforward. The integration test for this aspect is comprised of the following classes:

```
public class IntegrationTest {
  @Test
  public void demonstrateAspect () throws InterruptedException {
    ApplicationContext context = new ClassPathXmlApplicationContext (
        "integration-test-context.xml");

    CallerBean integrationTestBean = (CallerBean) context
        .getBean ("integrationBean");

    integrationTestBean.startTest ();
  }
}
```

```
public class CallerBean {
  public void setTestBean (IntegrationTestBean testBean) {
    this.testBean = testBean;
  }

  public void startTest () throws InterruptedException {
    testBean.slowMethod ();
  }

  private IntegrationTestBean testBean;
}
```

```
public class IntegrationTestBean {
  private int                 delay;
  private IntegrationTestBean next;

  public void setDelay (int innerDelay) {
    this.delay = innerDelay;
  }

  public void slowMethod () throws InterruptedException {
    Thread.sleep (delay);
    if (next != null)
      next.slowMethod ();
  }

  public void setNext (IntegrationTestBean next) {
    this.next = next;
  }
}
```

These classes are designed to allow a variable amount of delay and depth of stack trace in the output, to show off a few features of the aspect.

The beans are defined and connected to each other by Spring:

```
<bean id="integrationBean"
  class="example.CallerBean">
  <property name="testBean" ref="fastBean" />
</bean>

<bean id="fastBean"
  class="example.IntegrationTestBean">
  <property name="delay" value="10" />
  <property name="next" ref="slowBean" />
</bean>

<bean id="slowBean"
  class="example.IntegrationTestBean">
  <property name="delay" value="200" />
  <property name="next" ref="obeseBean" />
</bean>

<bean id="obeseBean"
  class="example.IntegrationTestBean">
  <property name="delay" value="700" />
</bean>
```

This creates a call stack with one `CallerBean` calling a chain of three increasingly-slow `IntegrationTestBean` instances.

## Configuring the Aspects ##

The `HatchInterceptor` advice class defaults to tracking method call times using the Hatch `push(name)` method, which does not activate logging but will participate in ongoing logging and is appropriate for most code. Instances of the interceptor can be configured with an alternate strategy, `EnablePopStrategy`, which will activate logging. This is often appropriate for methods through which external code calls the advised service.

The interceptor also defaults to formatting method signatures by printing the full signature, which is rather verbose. Applications can customize the way method signatures are logged by providing a value for the  `signatureTranslator` property of the interceptor bean.

The integration test sets up the following interceptor beans:

```
<bean id="hatchStartTimer"
  class="org.linuxstuff.hatch.aop.HatchInterceptor">
  <property name="timerStrategy">
    <bean class="org.linuxstuff.hatch.aop.EnablePopStrategy" />
  </property>
</bean>

<bean id="hatchTimer"
  class="org.linuxstuff.hatch.aop.HatchInterceptor">
  <property name="signatureTranslator">
    <bean class="org.linuxstuff.hatch.aop.MethodNameTranslator" />
  </property>
</bean>
```

`hatchStartTimer` is configured with the `EnablePopStrategy` and is applied, below, to the entry point into the integration test. It will print the full name of the method being profiled, and will enable Hatch. `hatchTimer` is configured to participate in existing Hatch execution and to use a much shorter signature naming strategy.

## Binding to Pointcuts ##

The Hatch aspects are intended to be run around other method calls. To tie together the two examples above, the following configuration injects `hatchStartTime` around calls to any `CallerBean` and `hatchTimer` around any `IntegrationTestBean`.

```
<aop:config>
  <aop:aspect ref="hatchStartTimer">
    <aop:around
      pointcut="execution (* example.CallerBean.* (..))"
      method="timeMethod" />
  </aop:aspect>

  <aop:aspect ref="hatchTimer">
    <aop:around
      pointcut="execution (* example.IntegrationTestBean.* (..))"
      method="timeMethod" />
  </aop:aspect>
</aop:config>
```

## The Result ##

When JUnit runs the integration test, it produces the following output, logging the time taken at each level of the call chain. Note the different formats for method calls.

```
[910ms] public void example.CallerBean.startTest() throws java.lang.InterruptedException
  [910ms] slowMethod
    [900ms] slowMethod
      [700ms] slowMethod
```