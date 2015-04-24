# Introduction #

Hatch is very easy to use in your projects.  This page will walk you through the initial steps of getting it going.  Start here if you want to try Hatch out for the first time.

# Details #

In a few moments, you'll be up and running.  First you'll need to include hatch in your project, then update your code, and then see the results!

## Maven ##

First start by adding our maven repository to your project.  Add to your pom.xml:

```
  <repositories>
    <!-- This repo is used by org.linuxstuff.hatch.* -->
    <repository>
      <id>hatch-maven2-repo</id>
      <name>Hatch: Maven Repository on Google Code</name>
      <url>http://hatch-timer.googlecode.com/svn/repo</url>
    </repository>
  </repositories>
```

Then include hatch as a dependency:

```
  <dependencies>
    <dependency>
      <groupId>org.linuxstuff.hatch</groupId>
      <artifactId>hatch-core</artifactId>
      <!-- See http://hatch-timer.googlecode.com/ for current version -->
      <version>1.0-SNAPSHOT</version>
    </dependency>
  </dependencies>
```

That's it!  If you need to **idea:idea** or **eclipse:eclipse** do it now.

## Your Code ##

Find some code you want to discover the performance breakdown on.  If you don't have any, create a test class that has some methods in it that call `Thread.sleep()`.

The proper pattern for introducing Hatch is to add the push/pop calls **in actual method**, not wrap calls to the method.  This is important to follow in large projects for consistency.

### Before ###

So you've found some code:

```

  public void businessMethod(BusinessBean bizBean) {
     validateRequest(bizBean);
     doFirstBusinessThing(bizBean.getParam());
     doLastBusinessThing(bizBean.getParam());
  }
```

### After ###

We'll ignore exception handling to get you started:

```
  public void businessMethod(BusinessBean bizBean) {
     TimerStackUtil.enableAndPush("businessMethod");
     validateRequest(bizBean);
     doFirstBusinessThing(bizBean.getParam());
     doLastBusinessThing(bizBean.getParam());
     TimerStackUtil.pop("businessMethod");

  }
```

Add similar calls in `validateRequest` `doFirstBusinessThing` and `doLastBusinessThing` too.  You can use `TimerStackUtil.push()` instead in those classes.  The strings you use are up to you.

That's it.

## Running ##

Compile and start your project.  When a call to `businessMethod` completes, on `System.out` you will see:

```
  [160ms] businessMethod
   [80ms] validateRequest
   [50ms] doFirstBusinessThing
   [20ms] doLastBusinessThing
```

Sure takes a long time to `validateRequest`.  See GrokkingTips2 for a few quick tips on reading the output.

You may notice these values do not sum up correctly. This means that 10ms are spent in `businessMethod` itself.

# Conclusion #

I know that was pretty simple, but you now have a basic understanding of how to use Hatch.  If this seems interesting to you, see the DetailedIntroduction for more details and learn about how to use it on production code.  The HatchingWithEclipse and SpringAop pages have some tips on saving you some time too!