
/**
 *
 * @author NaderSl
 */
 public final class PokeHashReqResult
    {
        public PokeHashResp resp;
        public int responseCode, rateLimitSecond, ratePeriodEnd, maxRequestCount, rateRequestsRemaining;
        public long timeCost;

        public PokeHashReqResult(PokeHashResp resp, int responseCode, int rateLimitSecond, int ratePeriodEnd, int maxRequestCount, int rateRequestsRemaining, long timeCost) {
            this.resp = resp;
            this.responseCode = responseCode;
            this.rateLimitSecond = rateLimitSecond;
            this.ratePeriodEnd = ratePeriodEnd;
            this.maxRequestCount = maxRequestCount;
            this.rateRequestsRemaining = rateRequestsRemaining;
            this.timeCost = timeCost;
        }

    public PokeHashResp getResp() {
        return resp;
    }

    public void setResp(PokeHashResp resp) {
        this.resp = resp;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getRateLimitSecond() {
        return rateLimitSecond;
    }

    public void setRateLimitSecond(int rateLimitSecond) {
        this.rateLimitSecond = rateLimitSecond;
    }

    public int getRatePeriodEnd() {
        return ratePeriodEnd;
    }

    public void setRatePeriodEnd(int ratePeriodEnd) {
        this.ratePeriodEnd = ratePeriodEnd;
    }

    public int getMaxRequestCount() {
        return maxRequestCount;
    }

    public void setMaxRequestCount(int maxRequestCount) {
        this.maxRequestCount = maxRequestCount;
    }

    public int getRateRequestsRemaining() {
        return rateRequestsRemaining;
    }

    public void setRateRequestsRemaining(int rateRequestsRemaining) {
        this.rateRequestsRemaining = rateRequestsRemaining;
    }

    public long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(long timeCost) {
        this.timeCost = timeCost;
    }

    @Override
    public String toString() {
        return "PokeHashReqResult{" + "resp=" + resp + ", responseCode=" + responseCode + ", rateLimitSecond=" + rateLimitSecond + ", ratePeriodEnd=" + ratePeriodEnd + ", maxRequestCount=" + maxRequestCount + ", rateRequestsRemaining=" + rateRequestsRemaining + ", timeCost=" + timeCost + '}';
    }
        
    };