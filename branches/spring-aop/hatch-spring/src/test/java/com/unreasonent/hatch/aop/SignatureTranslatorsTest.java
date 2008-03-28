package com.unreasonent.hatch.aop;

import static org.junit.Assert.assertEquals;

import org.aspectj.lang.Signature;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith (JMock.class)
public class SignatureTranslatorsTest {
  private final Mockery jmock = new JUnit4Mockery ();

  @Test
  public void fullSignature () {
    final Signature sig = jmock.mock (Signature.class);
    jmock.checking (new Expectations () {
      {
        allowing (sig).toLongString ();
        will (returnValue ("a very long string"));
      }
    });

    FullSignatureTranslator namer = new FullSignatureTranslator ();
    assertEquals ("a very long string", namer.nameCall (sig));
  }

  @Test
  public void methodName () {
    final Signature sig = jmock.mock (Signature.class);
    jmock.checking (new Expectations () {
      {
        allowing (sig).getName ();
        will (returnValue ("hi"));
      }
    });

    MethodNameTranslator namer = new MethodNameTranslator ();
    assertEquals ("hi", namer.nameCall (sig));
  }
}
