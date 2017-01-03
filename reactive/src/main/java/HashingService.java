
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

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

    public static boolean async = true;
    public static AtomicBoolean taskCompleted = new AtomicBoolean(false);

    /**
     * Reactive Throttling
     */
    public static void main(String[] argV) {
        
        //pre-defined requests to throttle
        final PokeHashReq[] pokeHashRequests = new PokeHashReq[]{new PokeHashReq(),new PokeHashReq(),new PokeHashReq(),new PokeHashReq(),new PokeHashReq()};
        
        //grab their results and handle them.
        Observable<PokeHashReqResult> results = Observable.from(pokeHashRequests).subscribeOn(async ? Schedulers.computation() : Schedulers.immediate()).
                flatMap(r -> r.getResult().onErrorReturn(e -> { //continue after whatever error occuring by setting result to an empty pokehashresult.
                    return new PokeHashReqResult(null, 0, 0, 0, 0, 0, 0);
                }));
        results.subscribe(
                pokeHashReqResult -> {
                    // Handle the next HashReqResult,in this test just print it, you migh want to add it to a list and handle it later.
                    System.out.println(pokeHashReqResult);
                    try {
                        Thread.sleep(REQUEST_RATE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                },
                // error handling
                t -> {
                    t.printStackTrace();
                },
                //task completed
                () -> {
                    taskCompleted.set(true);
                }
        );

        while (!taskCompleted.get()) {} // wait until task is completed.
        System.out.println("Done Throttling.");
        results.unsubscribeOn(Schedulers.computation());
    }
}
