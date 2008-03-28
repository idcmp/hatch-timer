package org.linuxstuff.hatch.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

/**
 * Interceptor for method calls that uses a {@link TimerStrategy} to record
 * method invocations and completions. The call itself is left unmodified, but
 * useful performance statistics can be delivered via tools like Hatch.
 * 
 * @see TimerStrategy
 */
public class HatchInterceptor {
  /**
   * Configures the method signature translation strategy. By default, called
   * methods' {@link Signature}s are translated using a
   * {@link FullSignatureTranslator}, which can be very verbose. Alternate
   * implementations of {@link SignatureTranslator} allow the application to
   * customise the Hatch output.
   * 
   * @param translator
   *            the call signature translator to use for subsequent timing.
   */
  public void setSignatureTranslator (SignatureTranslator translator) {
    if (translator == null)
      throw new IllegalArgumentException (
          "Signature translator may not be null.");
    this.signatureTranslator = translator;
  }

  /**
   * Changes the timer strategy used to record method invocation times. The
   * default strategy is a {@link PushPopStrategy}, which is appropriate
   * for use on internal methods as it does not explicitly enable Hatch.
   * Alternate implementations can be used to fine-tune the way in which Hatch
   * is invoked, or to replace Hatch entirely with another call-timing tool.
   * 
   * @param timerStrategy
   *            the method-call timing strategy to use.
   */
  public void setTimerStrategy (TimerStrategy timerStrategy) {
    if (timerStrategy == null)
      throw new IllegalArgumentException ("Timer strategy may not be null.");
    this.timerStrategy = timerStrategy;
  }

  /**
   * Intercepts method calls and times them. Prior to continuing the method
   * call, this method calls {@link #pushTimer(String)} with the translated
   * signature; after the method call completes, this calls
   * {@link #popTimer(String)} with the same translated signature.
   * 
   * @param call
   *            the method call to intercept.
   * @return the method call's return value, if any.
   * @throws Throwable
   *             the method call's thrown exception, if any.
   * @see #setSignatureTranslator(SignatureTranslator)
   */
  public Object timeMethod (ProceedingJoinPoint call) throws Throwable {
    String callName = getCallName (call.getSignature ());

    pushTimer (callName);
    try {
      return call.proceed ();
    } finally {
      popTimer (callName);
    }
  }

  private String getCallName (Signature signature) {
    return signatureTranslator.nameCall (signature);
  }

  private void popTimer (String callName) {
    timerStrategy.pop (callName);
  }

  private void pushTimer (String callName) {
    timerStrategy.push (callName);
  }

  private SignatureTranslator signatureTranslator = new FullSignatureTranslator ();
  private TimerStrategy       timerStrategy       = new PushPopStrategy ();
}
