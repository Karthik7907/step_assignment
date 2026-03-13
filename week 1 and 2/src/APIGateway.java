import java.util.concurrent.*;
import java.util.*;

class APIGateway {

    private int tokens;
    private final int maxTokens;
    private final double refillRate; // tokens per second
    private long lastRefillTime;

    public APIGateway(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {

        refillTokens();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    private void refillTokens() {

        long now = System.currentTimeMillis();

        double tokensToAdd =
                ((now - lastRefillTime) / 1000.0) * refillRate;

        if (tokensToAdd > 0) {

            tokens = Math.min(maxTokens, tokens + (int) tokensToAdd);

            lastRefillTime = now;
        }
    }

    public int getTokens() {
        return tokens;
    }
}

class RateLimiter {

    private final ConcurrentHashMap<String, APIGateway> clients =
            new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS = 1000;
    private static final double REFILL_RATE = 1000.0 / 3600; // per second

    public String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId,
                new APIGateway(MAX_REQUESTS, REFILL_RATE));

        APIGateway bucket = clients.get(clientId);

        if (bucket.allowRequest()) {

            return "Allowed (" + bucket.getTokens()
                    + " requests remaining)";
        }

        return "Denied (0 requests remaining)";
    }


    public Map<String, Object> getRateLimitStatus(String clientId) {

        APIGateway bucket = clients.get(clientId);

        Map<String, Object> status = new HashMap<>();

        status.put("used", MAX_REQUESTS - bucket.getTokens());
        status.put("limit", MAX_REQUESTS);
        status.put("remaining", bucket.getTokens());

        return status;
    }


    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        String client = "abc123";

        for (int i = 0; i < 5; i++) {

            System.out.println(
                    limiter.checkRateLimit(client));
        }

        System.out.println(
                limiter.getRateLimitStatus(client));
    }
}