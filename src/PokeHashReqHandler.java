
/**
 *
 * @author NaderSl
 */
@FunctionalInterface
public interface PokeHashReqHandler {

    //statusCode returns -1 if an exception occurs.
    public void onComplete(final PokeHashResp resp, final int responseCode, final long timeCost, int rateLimitSeconds, int ratePeriodEnd, int maxRequestCount, int rateRequestsRemaining);
}
