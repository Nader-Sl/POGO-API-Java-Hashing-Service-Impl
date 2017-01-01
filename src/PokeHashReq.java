
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import org.json.JSONArray;

import org.json.JSONObject;

/**
 *
 * @author NaderSL
 */
public class PokeHashReq implements Callable<PokeHashResp> {

    //fields need to be public for json serialization.
    public long Timestamp;
    public double Latitude, Longitude, Altitude;
    public String AuthTicket, SessionData;
    public List<String> Requests = new ArrayList();
    public List<PokeHashReqHandler> handlers = new ArrayList<>();

    public PokeHashReq(long timeStamp, double latitude, double longitude, double altitude, byte[] authTicket, byte[] sessionData, byte[]... reqs) {
        this.Timestamp = timeStamp;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Altitude = altitude;
        this.AuthTicket = Base64.getEncoder().encodeToString(authTicket);
        this.SessionData = Base64.getEncoder().encodeToString(sessionData);
        for (byte[] arr : reqs) {
            Requests.add(Base64.getEncoder().encodeToString(arr));
        }
    }

    public PokeHashReq() {
        //Valid Test Data
        this(1482825574, 22.4, 22.4, 1, new byte[]{10, 80, 32, 37, -17, 33, 34, 94, 53, 38, 77, 110, 48, -125, 115, -3, -102, 110, 118, -112, -2, 11, -92, 57, 88, -57, 102, -111, 126, 59, 127, -82, 8, -118, -102, 110, 95, -98, -97, -105, -79, 23, -41, -5, 45, -34, -92, 122, -49, -53, -60, -77, 31, 121, 97, -108, 55, 111, 91, -61, 51, 45, 32, 9, -102, -50, -111, -34, -80, -87, -59, 68, 54, 29, 44, -99, -117, 46, 100, -29, -17, 95, 16, -82, -121, -118, -124, -108, 43, 26, 16, 72, 6, 93, 65, 83, 19, 58, 19, -78, -109, -79, -66, -69, 54, -28, 4}, new byte[]{32, -48, -119, 75, -58, 72, 68, 57, -105, -96, -112, -116, -115, 24, -125, 107}, new byte[]{8, 2, 18, 10, 10, 8, 10, 2, 85, 83, 18, 2, 101, 110}, new byte[]{8, 2, 18, 10, 10, 8, 10, 2, 85, 83, 18, 2, 101, 110});
    }

    public void addHandler(PokeHashReqHandler handler) {
        handlers.add(handler);
    }

    public void removeHandler(PokeHashReqHandler handler) {
        handlers.remove(handler);
    }

    @Override
    public PokeHashResp call() throws Exception {
        final long currTimeStamp = System.currentTimeMillis();
        PokeHashResp resp = null;
        int responseCode = -1;

        final HttpURLConnection con = (HttpURLConnection) new URL(HashingService.API_ENDPOINT_URL).openConnection();

        //add reuqest header
        con.setRequestMethod("POST");

        con.setRequestProperty("content-type", "application/json");
        con.setRequestProperty("X-AuthToken", HashingService.POKE_HASH_KEY);

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        final JSONObject obj = new JSONObject(this, new String[]{"Timestamp", "Latitude", "Longitude", "Altitude", "AuthTicket", "SessionData", "Requests"});
        System.out.println("Sending Hash Service Request: " + obj.toString());

        wr.writeBytes(obj.toString());
        wr.flush();
        wr.close();

        responseCode = con.getResponseCode();
        final int rateLimitSecond = con.getHeaderFieldInt("X-RateLimitSeconds", responseCode),
                ratePeriodEnd = con.getHeaderFieldInt("X-RatePeriodEnd", responseCode),
                maxRequestCount = con.getHeaderFieldInt("X-MaxRequestCount", responseCode),
                rateRequestsRemaining = con.getHeaderFieldInt("X-RateRequestsRemaining", responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            if ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            final JSONObject respObj = new JSONObject("" + response);
            final int locationAuthHash = respObj.getInt("locationAuthHash");
            final int locationHash = respObj.getInt("locationHash");
            final JSONArray reqHashesArr = respObj.getJSONArray("requestHashes");
            final long[] reqHashes = new long[reqHashesArr.length()];
            for (int i = 0; i < reqHashes.length; i++) {
                reqHashes[i] = reqHashesArr.getLong(i);
            }
            in.close();
            resp = new PokeHashResp(locationAuthHash, locationHash, reqHashes);
        }

        for (PokeHashReqHandler handler : handlers) {
            handler.onComplete(resp, responseCode, (System.currentTimeMillis() - currTimeStamp), rateLimitSecond, ratePeriodEnd, maxRequestCount, rateRequestsRemaining);
        }
        return resp;
    }
}
