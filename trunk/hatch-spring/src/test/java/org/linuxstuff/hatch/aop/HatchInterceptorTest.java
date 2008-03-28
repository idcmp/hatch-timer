package org.linuxstuff.hatch.aop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linuxstuff.hatch.aop.HatchInterceptor;
import org.linuxstuff.hatch.aop.TimerStrategy;

@RunWith(JMock.class)
public class HatchInterceptorTest {
  private final Mockery jmock = new JUnit4Mockery ();

  @Test
  public void successfulCall () throws Throwable {
    HatchInterceptor interceptor = new HatchInterceptor ();

    final ProceedingJoinPoint call = jmock.mock (ProceedingJoinPoint.class);
    final TimerStrategy strategy = jmock.mock (TimerStrategy.class);

    provideMockSignatureForJoinpoint (call);

    jmock.checking (new Expectations () {
      {
        Sequence calls = jmock.sequence ("sane-call-sequence");
        one (strategy).push (with (any (String.class)));
        inSequence (calls);

        one (call).proceed ();
        inSequence (calls);
        will (returnValue ("OK!"));

        one (strategy).pop (with (any (String.class)));
        inSequence (calls);
      }
    });

    interceptor.setTimerStrategy (strategy);

    assertEquals ("OK!", interceptor.timeMethod (call));
  }

  @Test
  public void exceptionalCall () throws Throwable {
    HatchInterceptor interceptor = new HatchInterceptor ();

    final ProceedingJoinPoint call = jmock.mock (ProceedingJoinPoint.class);
    final TimerStrategy strategy = jmock.mock (TimerStrategy.class);

    provideMockSignatureForJoinpoint (call);

    final Exception expectedError = new Exception ("Boo.");

    jmock.checking (new Expectations () {
      {
        Sequence calls = jmock.sequence ("sane-call-sequence");
        one (strategy).push (with (any (String.class)));
        inSequence (calls);

        one (call).proceed ();
        inSequence (calls);
        will (throwException (expectedError));

        one (strategy).pop (with (any (String.class)));
        inSequence (calls);
      }
    });

    interceptor.setTimerStrategy (strategy);

    try {
      interceptor.timeMethod (call);
      fail ();
    } catch (Exception e) {
      assertEquals (expectedError, e);
    }
  }

  private void provideMockSignatureForJoinpoint (final ProceedingJoinPoint call) {
    jmock.checking (new Expectations () {
      {
        Signature signature = jmock.mock (Signature.class);

        allowing (call).getSignature ();
        will (returnValue (signature));

        allowing (signature);
      }
    });
  }
}
