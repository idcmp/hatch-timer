# Quick Tips #

```
  [240ms] placeWigglies
    [240ms] oldPlaceWiggly
      [240ms] doPlaceWiggly
```

The time spent in `placeWigglies` is completely spent in `oldPlaceWiggly` which
in turn is completely spent in `doPlaceWiggly`.


---


```
  [105ms] addWiggliesToBasket
    [52ms] addWiggly
    [53ms] addWiggly
```

The sum of the two children is equal to the parent.  This means that
the time in the parent is completely spent in the children.


---



```
  [105ms] addWiggliesToBasket
    [72ms] addWiggly
    [33ms] addWiggly
```

The time in the first call to `addWiggly` is significantly more
expensive than the second.  Logic flow being identical, this tends to
be related to caching in the first call being taken advantage of in
the second. **Suggestion:** Add more tracing _within_ `addWiggly`
that may be involved with caching to confirm this hypothesis.


---


You've added logging to `readBook`, `readPage` and `flipPage`, when you run your code you notice `flipPage` is missing!

```
  [10ms] readBook
    [8ms] readPage
```

Stack elements less than 3ms are not logged.  **Suggestion:** Use `TimerStackUtil.setMinimumLoggingThreshold`.  Pass in 0 to always show stack elements. (Including ones that are 0ms!)

---



```
  [100ms] pickApples
    [23ms] validatePick
  [33ms] ...
```

A lot of time is spent in the parent `pickApples` method, only a fraction
is spent in the child `validatePick` method.  **Suggestion:**  Add more tracing
_within_ `pickApples`.


---



```
  [50ms] placeWiggly
    [10ms] validateWiggly
    [10ms] examineBasket
    [10ms] validateStock
    [10ms] processPurchase
    [10ms] auditPurchase
```

This is pretty common.  In situations like these, if you _really_ need
to make it faster, you may want to combine calls to the same resource
together.  If `validateStock`, `processPurchase` and
`auditPurchase` all access the same database, perhaps these can be
wrapped together into one round trip calling a stored procedure.
Alternatively you can look at database performance, network usage or
CPU speed.  Remember, there's a cost associated to you optimizing this
code.  It may be cheaper to just buy another server.


---



```
 // Sometimes it says:
 [100ms] addToBasketSlurp
  [...etc

 // Other times it says:
 [3589ms] addToBasketSlurp
```

Sometimes this business method is very fast, other times it's very
slow.  If the stack dump doesn't tell you why, then I recommend
selectively enabling dumping of the stack only when it's having
performance issues.  See TracingProblems for more.


---



# More ideas welcome... #