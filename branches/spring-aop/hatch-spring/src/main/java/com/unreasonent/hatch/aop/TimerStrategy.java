package com.unreasonent.hatch.aop;

public interface TimerStrategy {

  public void push (String callName);

  public void pop (String callName);

}
