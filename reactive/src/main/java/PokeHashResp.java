



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author NaderSL
 */
public class PokeHashResp {

    private int locationAuthHash, locationHash;
    private List<Long> hashedRequests = new ArrayList();

    public PokeHashResp(int locationAuthHash, int locationHash, long... hashedRequests) {
        this.locationAuthHash = locationAuthHash;
        this.locationHash = locationHash;
        for (long hashedReq : hashedRequests) {
            this.hashedRequests.add(hashedReq);
        }
    }

    public int getLocationAuthHash() {
        return locationAuthHash;
    }

    public int getLocationHash() {
        return locationHash;
    }

    public List<Long> getHashedRequests() {
        return hashedRequests;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for(long hashedReq : hashedRequests)
        {
            sb.append(hashedReq).append(",");
        }
        return "{" + "locationAuthHash=" + locationAuthHash + ", locationHash=" + locationHash + ", hashedRequests=" + sb.toString() + '}';
    }

 
}
