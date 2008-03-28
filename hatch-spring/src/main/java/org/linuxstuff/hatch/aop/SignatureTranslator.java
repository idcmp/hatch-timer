package org.linuxstuff.hatch.aop;

import org.aspectj.lang.Signature;

/**
 * Strategy for converting AspectJ {@link Signature} objects into strings for
 * display or logging.
 */
public interface SignatureTranslator {
	public String nameCall(Signature signature);
}
