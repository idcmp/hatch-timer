In **hatch-core**, there is `ExceedsDurationMetricsLogger`.  Combining this with by request processing and thread-specific tracing will enable you to track why a set of code did not perform within the specified duration.

```

   TimerStackUtil.setState(TraceState.BY_REQUEST);
   ....

   public void businessMethod() {
     TimerStackUtil.enableAndPush("businessMethod");
     ...
     TimerStackUtil.setThreadLocalMetricsLogger(
        new ExceedsDurationMetricsLogger(BasicMetricsLogger.LOGGER,999));
     TimerStackUtil.pop("businessMethod");
   }
```

The above code will enable tracing for that method.  However, Hatch will only
dump the stack if the business method took 1 second or greater.

This is supremely handy to diagnose contention issues at run time without having to log every single request.

You can also construct a metricsLogger statically and use it:
```

   private static final cashierLogger = new ExceedsDurationMetricsLogger(new Log4jMetricsLogger(),249);
   ...
   public void cashInStocks(...) {
     TimerStackUtil.enableAndPush("cashInStocks");
     TimerStackUtil.setThreadLocalMetricsLogger(cashierLogger);
     .......
```


I should write more here.