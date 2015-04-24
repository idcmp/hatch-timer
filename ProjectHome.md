# Hatch: A Stack-based Performance Logger #

  * Check out the GettingStarted page!

| Release Version | 0.7 | [Release0Dot7](Release0Dot7.md) |
|:----------------|:----|:--------------------------------|
| Snapshot Version | 1.0-SNAPSHOT |  |

## Introduction ##

Logging is still the simplest and most popular way of tracing a program's execution. Even with many advanced tools at our disposal, we still lean toward simple _I Am Here._ style debugging.

Hatch is a simple stack-based performance logger.  Just by adding begin/end style calls into your code, you can trace how much time it is taking to perform a specific request.

## Example Output ##

```
 [135ms] request share purchase
   [5ms] validate purchase request
   [40ms] check account balances
    [39ms] synchronous balance request
   [30ms] read uncommitted share availability check
   [55ms] queue request
     [30ms] persist message to queue
     [15ms] write audit log
```

## Features ##

  * Threads can request tracing on a per-request basis, reducing clutter
  * Enable or disable globally at runtime without restarting.
  * Only log traces that meet specific criteria ("Only log requests that took over 1000ms during business hours.")
  * Log to System.out or log4j (or extend the basic logger to log wherever you want, see AdvancedHatchUsage).

## More Information ##

  * Poke around the [source](http://code.google.com/p/hatch-timer/source/browse/trunk) to find out more.
  * See the [release notes](http://code.google.com/p/hatch-timer/w/list?q=label:ReleaseNotes) history.