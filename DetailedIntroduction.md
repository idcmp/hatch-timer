# Introduction #

This document assumes you've done the GettingStarted.  If you haven't, you will need to
go there now to update your **pom.xml** to include Hatch.

In this document you will learn some of the different trace modes of Hatch as well as how to enable at runtime and how to completely disable Hatch at compile time.

# Push/Pop #

Hatch has a stack that it uses to keep track of the performance of different areas of your code.  To add to the stack, you `push()` an element onto it.  When that element completes, your code must called a corresponding `pop()`.  When the last element is popped off the stack, Hatch will log the stack.  Stacks are thread local.

# Proper Usage #

In GettingStarted we added some code around a business method.  However, if an exception was  thrown, Hatch would not see the matching pop element.  There are a few different ways to tackle this problem:

**Example 1: The code before**
```
     public void businessMethod() {
         firstActvitiy();
         secondActvitiy();
         thirdActvitiy();
     }
```

If your code is **already** wrapped in a try/catch block after variable initialization, you can add the pop call into a finally block.  This is more useful in helper/utility classes your application may have.

**Example 2: Try/Finally**
```
     public void businessMethod() {
         try {
           TimerStackUtil.push("businessMethod");
           firstActvitiy();
           secondActvitiy();
           thirdActvitiy();
         } finally {
           TimerStackUtil.pop("businessMethod");
         }
     }
```

**Example 3: Try/Finally**
```
     private void secondActivity() throws UglyException;

     public void businessMethod() {
         try {
           ...
           try {
             TimerStackUtil.push("businessMethod");
             firstActvitiy();
             secondActvitiy();
             thirdActvitiy();
           } finally {
             TimerStackUtil.pop("businessMethod");
           }
         } catch (UglyException ugly) {
           ...
         }
     }
```


However, the normal case is you won't have a giant try/catch block.  You are then left with two options.  The first is wrapping the code in a try/catch block.  You may want to consider this if you're making good use of Hatch in your project.

Your second option is to do nothing:

**Example 4: Tracing only on Success**
```
     public void businessMethod() {
         TimerStackUtil.push("businessMethod");
         firstActvitiy();
         secondActvitiy();
         thirdActvitiy();
         TimerStackUtil.pop("businessMethod");
     }
```


In the above situation, you will only get successful dumps if no exception is thrown.  This is what you want anyway, since collecting metrics for your code when it errors is less useful than when it succeeds (unless you have some sort of crazy exception handling framework).

My recommendation is to go with **Example 3** most of the time, and if you can add push/pop calls in try/catch/finally blocks easily, do so.

# Enabling/Disabling Tracing #

Hatch has three tracing modes: **on**, **off** and **by request**.

| On | All calls to push/pop are recorded. When the last pop is done, the stack is logged and all thread-local variables are reset. |
|:---|:-----------------------------------------------------------------------------------------------------------------------------|
| Off |  Aggressively shut off any tracing. Individual threads **CANNOT** override tracing if it is turned off. |
| By Request |  Effectively turn tracing off unless an individual thread requests to be traced (see `StackTimerUtil.enableAndPush`) Until tracing is enabled, all requests to push elements onto the stack are ignored. **This is the default, and unless you're having performance  problems, you should use it instead of Off**. |

To enable tracing everywhere in your application, you just need to call:

```
   TimerStackUtil.setState(TraceState.ON);
```

To go back to by request procesing:

```
   TimerStackUtil.setState(TraceState.BY_REQUEST);
```

You can add this to your management interface to turn tracing on/off at runtime.

# Tracing By Request #

You may have a particular business function of which you're generally interested in knowing the performance.  Using **by request** processing, your application can easily record the performance of just that business function (and any utility/helper classes it has).

You saw an example of this in the GettingStarted page.  Using `enableAndPush` instead of just `push` will turn on thread-specific tracing until the last element is popped off the stack.

```
       ...
       TimerStackUtil.setState(Tracestate.BY_REQUEST);
       ...
       public void businessMethod() {
         TimerStackUtil.enableAndPush("businessMethod"); // first element in stack
         firstActvitiy();           // each of these has push/pop
         secondActvitiy();          // calls.  No calls to enableAndPush.
         thirdActvitiy();
         TimerStackUtil.pop("businessMethod"); // pop last element in stack.
         // The next call to push() on this thread will do nothing,
         // as the stack was emptied and you will need to re-request tracing.
      }
```

Generally speaking you should **not** use `enableAndPush` anywhere except in methods that handle external requests (session beans, etc..). Liberal use of it instead of push will reduce the value of processing by request, and will lead someone to eventually turn profiling off.

# Disabling Hatch at Compile Time #

If you find problems with Hatch, please report them in the issue tracker.

At some point, your coworkers may suspect that Hatch is causing their code to be nonperformant.  To prove them wrong, you can disable Hatch at compile time, by replacing the **hatch-core** dependency with a **hatch-null** dependency in your **pom.xml**.

```
<dependencies>
    <dependency>
      <groupId>org.linuxstuff.hatch</groupId>
      <artifactId>hatch-null</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
  </dependencies>
```

This replaces `TimerStackUtil` with an empty version that does nothing! **WARNING** Do not deploy code with **hatch-null**.  It cannot co-exist with **hatch-core** and if other projects in your classpath use **hatch-core**, you will have both put on your classpath.

Also, if you are using a custom metrics logger (explained later), you may find you cannot switch in **hatch-null** as easily. Future versions of Hatch may fix this.

If you find problems with Hatch, please report them in the issue tracker.

# Conclusion #

Using Eclipse?  See the quick HatchingWithEclipse page, then check out the AdvancedHatchUsage page for the last cool bits Hatch has to offer.