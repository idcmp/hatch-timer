# Introduction #

This document assumes you've read both the GettingStarted and DetailedIntroduction documents.  If you haven't, you really should.

We'll talk about how to lie about performance metrics and how to do something with that output other than log to System.out.

# Lies #

To experiment with Hatch, you may wish you could put your own performance data in at times.  In production you may want to use this to collect the processing time of an asynchronous operation that your code is processing the results of.

Otherwise this feature is mainly for playing with Hatch:

```
  public void businessMethod(BusinessBean bizBean) {
     TimerStackUtil.enableAndPush("businessMethod");
     validateRequest(bizBean);
     doFirstBusinessThing(bizBean.getParam());
     doLastBusinessThing(bizBean.getParam());
     TimerStackUtil.pop("businessMethod",500l);
  }
```

Above you see that we're telling Hatch that `businessMethod` actually took 500ms. The method signature takes a `Long` object, so Java will autobox your primitive, but it must know it's a long. Passing `null` will be the same as calling the one-argument `pop`.

# Logging Metrics #

Hatch logs to `System.out` out of the box.  This is the `BasicMetricsLogger`. In real systems, this can be sub-optimal.  To get around this, you can use your own custom metrics logger.

Loggers can be changed globally or per-stack (just like how `enableAndPush` turns on tracing).

To log globally:

```
     TimerStackUtil.setMetricsLogger(BasicMetricsLogger.LOGGER);
```

To see an example of how to set the per-stack logger, see TracingProblems.

Note that the `BasicMetricsLogger` is in **hatch-core** and implicitly referencing it will cause compile errors should you switch to **hatch-null** for testing.

## Log4J Logger ##

Hatch includes a log4j metrics logger in the **hatch-log4j** artifact.  There are two steps to using it.  First add it into your **pom.xml**:

```
<dependencies>
  <dependency>
    <groupId>org.linuxstuff.hatch</groupId>
    <artifactId>hatch-log4j</artifactId>
    <!-- See http://hatch-timer.googlecode.com/ for current version -->
    <version>1.0-SNAPSHOT</version>
  </dependency>
</dependencies>
```

Then globally set it as the default logger:

```
     TimerStackUtil.setMetricsLogger(new Log4jMetricsLogger());
```

Traces will be logged at the **INFO** level.  Errors will be logged at the **ERROR** level. Both use "**org.linuxstuff.hatch.MetricsLogger**" as a category name.

# Conclusion #

It's fairly easy to extend the `BasicMetricsLogger` to do some fairly interesting things.  See the TracingProblems page for more information.