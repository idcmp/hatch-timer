package com.unreasonent.hatch.aop;

import org.linuxstuff.hatch.TimerStackUtil;

/**
 * A timer strategy using {@link TimerStackUtil#enableAndPush(String)} and
 * {@link TimerStackUtil#pop(String)} to time method invocation.
 */
public class EnablePopStrategy extends PushPopStrategy implements TimerStrategy {

  /**
   * Delegates to {@link TimerStackUtil#enableAndPush(String)}.
   * 
   * @see com.unreasonent.hatch.aop.TimerStrategy#push(java.lang.String)
   */
  @Override
  public void push (String callName) {
    TimerStackUtil.enableAndPush (callName);
  }

}
