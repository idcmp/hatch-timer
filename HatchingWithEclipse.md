# Introduction #

Having instrumented a few different classes, here are some tips that you may want to consider when adding Hatch with Eclipse.

I strongly recommend starting with the top-most or outer-most method in your system.  This is likely some business-sounding method like `performTrade`, `validateAccount` or `login`.  In my example, it's called `solveLUE`.

I assume you've read the GettingStarted page and added the necessary artifacts to your build path at this point.

# Autocomplete Types #

Eclipse will guess at types based on the acronym you enter.  Enter "**TSU**" and hit **Ctrl-Space** to save yourself needless keystrokes.

|![http://hatch-timer.googlecode.com/svn/wiki/images/hatch-with-eclipse-1.png](http://hatch-timer.googlecode.com/svn/wiki/images/hatch-with-eclipse-1.png) | >becomes> |![http://hatch-timer.googlecode.com/svn/wiki/images/hatch-with-eclipse-2.png](http://hatch-timer.googlecode.com/svn/wiki/images/hatch-with-eclipse-2.png) |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------|:----------|:---------------------------------------------------------------------------------------------------------------------------------------------------------|

  * Note: Teach your fingers "**TSU**" **Ctrl-Space** "**.push**"

# Depth First Navigation #

Using **Ctrl-Click** and **Alt-Left** you can navigate in and out of the methods.  If you're not used to using **Alt-Left** to navigate, this may take a few tries to perfect.

| **Ctrl-Click** into a Method | Make Your Change | **Alt-Left** out of the Method |
|:-----------------------------|:-----------------|:-------------------------------|
| ![http://hatch-timer.googlecode.com/svn/wiki/images/hatch-with-eclipse-ctrlclick.png](http://hatch-timer.googlecode.com/svn/wiki/images/hatch-with-eclipse-ctrlclick.png) | ![http://hatch-timer.googlecode.com/svn/wiki/images/hatch-with-eclipse-sum.png](http://hatch-timer.googlecode.com/svn/wiki/images/hatch-with-eclipse-sum.png) | ![http://hatch-timer.googlecode.com/svn/wiki/images/hatch-with-eclipse-arrows.png](http://hatch-timer.googlecode.com/svn/wiki/images/hatch-with-eclipse-arrows.png) |

  * Note: Depending multiple **Alt-Left** may be needed to get back to the previous method.
  * Note: I'm not kidding, this takes a bit to get used to, but now you'll grow to really like it.

# Check Diffs Before Compiling #

Once you have instrumented some code in your application, grab a diff from your local repository.  You want to check three things:
  1. You haven't accidentally reformatted the source code, making your diff larger than it is.
  1. You correctly used `pop` at the end of your methods (and didn't accidentally paste in another `push`).
  1. The string used in `push` matches the one in `pop`.

This quick check before you compile/deploy will catch any dumb mistakes!

# Conclusion #

If you use other environments or have more tips, let me know!  Don't forget to check out GrokkingTips2 and AdvancedHatchUsage if you haven't already.