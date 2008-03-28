package org.linuxstuff.hatch.aop;

import org.linuxstuff.hatch.TimerStackUtil;

/**
 * A timer strategy using {@link TimerStackUtil#push(String)} and
 * {@link TimerStackUtil#pop(String)} to time method invocation.
 */
public class PushPopStrategy implements TimerStrategy {

  /**
   * Delegates to {@link TimerStackUtil#pop(String)}.
   * 
   * @see org.linuxstuff.hatch.aop.TimerStrategy#pop(java.lang.String)
   */
  public void pop (String callName) {
    TimerStackUtil.pop (callName);
  }

  /**
   * Delegates to {@link TimerStackUtil#push(String)}.
   * 
   * @see org.linuxstuff.hatch.aop.TimerStrategy#push(java.lang.String)
   */
  public void push (String callName) {
    TimerStackUtil.push (callName);
  }

}
