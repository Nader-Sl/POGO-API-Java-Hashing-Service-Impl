package org.pogo.api.hashingservice;


/**
 *
 * @author NaderSl
 */
@FunctionalInterface
public interface PokeHashReqHandler {

    //statusCode returns -1 if an exception occurs.
    public void onComplete(final PokeHashReqResult result);
}
