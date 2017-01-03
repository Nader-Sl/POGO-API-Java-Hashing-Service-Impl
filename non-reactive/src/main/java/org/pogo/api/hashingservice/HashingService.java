package org.pogo.api.hashingservice;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author NaderSL
 */
public class HashingService {

    public final static int RPM = 150; //requests per minute
    public final static String POKE_HASH_KEY = "XXXXXXXXXXXXXXXXX"; // Replace with hashing service key purchased from Bossland.
    public final static String API_VERSION = "v121";

    public final static String API_ENDPOINT_BASE_URL = "http://pokehash.buddyauth.com/";
    public final static String API_ENDPOINT_DIR = "api/".concat(API_VERSION).concat("/hash");
    public final static String API_ENDPOINT_URL = API_ENDPOINT_BASE_URL.concat(API_ENDPOINT_DIR);

    public final static int RPS = (int) (Math.ceil(RPM / 60d)); //requests per second
    public final static int REQUEST_RATE = 1000 / RPS; //in milli seconds
    public static long delay;

    // Asynchronous Http Request
    public static Future<PokeHashResp> requestHash(ScheduledExecutorService executor, PokeHashReq req) throws InterruptedException, ExecutionException {
        Future<PokeHashResp> response = executor.schedule(req, delay += REQUEST_RATE, TimeUnit.MILLISECONDS);
        return response;
    }

    /**
     * Throttle Test
     */
    public static void main(String[] argV) throws Exception {

        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        final int requestsSize = 5;
        final long startTimeStamp = System.currentTimeMillis();

        for (int i = 0; i < requestsSize; i++) {
            final PokeHashReq req = new PokeHashReq();
            final int n = i + 1;
            // if handling is time costy, better using an async mechanism.
            req.addHandler((reqResult) -> {
                System.out.println(
                        String.format("Task %d completed in %d ms  @[ %d ]ms , response [%d]: \n %s \n X-RateLimitSeconds = %d, X-RatePeriodEnd = %d, X-MaxRequestCount = %d, X-RateRequestsRemaining = %d",
                                n, reqResult.timeCost, (System.currentTimeMillis() - startTimeStamp), reqResult.responseCode, reqResult.resp, reqResult.rateLimitSecond, reqResult.ratePeriodEnd, reqResult.maxRequestCount, reqResult.rateRequestsRemaining
                        ));
            });
            requestHash(executor, req);// PokeHashReq's default constructor has random valid values.
        }
        executor.shutdown();
        executor.awaitTermination((requestsSize * REQUEST_RATE) + (500 * requestsSize), TimeUnit.MILLISECONDS);
        System.out.println("Done Throttling.");
    }
}
