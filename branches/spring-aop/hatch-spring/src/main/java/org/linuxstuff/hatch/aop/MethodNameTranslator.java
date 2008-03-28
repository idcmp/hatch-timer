package org.linuxstuff.hatch.aop;

import org.aspectj.lang.Signature;

/**
 * Flattens signatures to the name of the called method.
 */
public class MethodNameTranslator implements SignatureTranslator {

  public String nameCall (Signature signature) {
    return signature.getName ();
  }

}
