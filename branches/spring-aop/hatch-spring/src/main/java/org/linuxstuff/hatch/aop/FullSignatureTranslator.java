package org.linuxstuff.hatch.aop;

import org.aspectj.lang.Signature;

/**
 * Flattens signatures to a complete signature string. This calls
 * {@link Signature#toLongString()} to determine the signature.
 */
public class FullSignatureTranslator implements SignatureTranslator {

  public String nameCall (Signature signature) {
    return signature.toLongString ();
  }
}
