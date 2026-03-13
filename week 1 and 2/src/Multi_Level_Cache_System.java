 import java.util.*;
class VideoData {
    String videoId;
    String content;
    long lastAccess;
    VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
        this.lastAccess = System.currentTimeMillis();
    }
}
public class Multi_Level_Cache_System {
    private final int L1_CAPACITY = 10_000;
    private final int L2_CAPACITY = 100_000;
    private final LinkedHashMap<String, VideoData> L1Cache;
    private final LinkedHashMap<String, VideoData> L2Cache;
    private final Map<String, VideoData> L3Database; // Simulated DB
    private long L1Hits = 0, L2Hits = 0, L3Hits = 0;
    private long L1Misses = 0, L2Misses = 0, L3Misses = 0;
    private final Map<String, Integer> accessCount = new HashMap<>();
    private final int PROMOTION_THRESHOLD = 3; // promote to L1 after 3 accesses
    public Multi_Level_Cache_System() {
        L1Cache = new LinkedHashMap<>(L1_CAPACITY, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1_CAPACITY;
            }
        };
        L2Cache = new LinkedHashMap<>(L2_CAPACITY, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L2_CAPACITY;
            }
        };
        L3Database = new HashMap<>();
        for (int i = 1; i <= 1_000_000; i++) {
            L3Database.put("video_" + i, new VideoData("video_" + i, "VideoContent_" + i));
        }
    }
    public VideoData getVideo(String videoId) {
        if (L1Cache.containsKey(videoId)) {
            L1Hits++;
            return L1Cache.get(videoId);
        } else {
            L1Misses++;
        }
        if (L2Cache.containsKey(videoId)) {
            L2Hits++;
            VideoData video = L2Cache.get(videoId);
            incrementAccess(videoId);
            if (accessCount.get(videoId) >= PROMOTION_THRESHOLD) {
                promoteToL1(videoId, video);
            }
            return video;
        } else {
            L2Misses++;
        }
        if (L3Database.containsKey(videoId)) {
            L3Hits++;
            VideoData video = L3Database.get(videoId);
            L2Cache.put(videoId, video);
            incrementAccess(videoId);
            return video;
        } else {
            L3Misses++;
            return null;
        }
    }
    private void incrementAccess(String videoId) {
        accessCount.put(videoId, accessCount.getOrDefault(videoId, 0) + 1);
    }
    private void promoteToL1(String videoId, VideoData video) {
        L1Cache.put(videoId, video);
        accessCount.put(videoId, 0);
    }
    public void invalidate(String videoId) {
        L1Cache.remove(videoId);
        L2Cache.remove(videoId);
        L3Database.remove(videoId);
        accessCount.remove(videoId);
    }
    public void getStatistics() {
        long totalL1 = L1Hits + L1Misses;
        long totalL2 = L2Hits + L2Misses;
        long totalL3 = L3Hits + L3Misses;
        double L1Rate = totalL1 == 0 ? 0 : (L1Hits * 100.0 / totalL1);
        double L2Rate = totalL2 == 0 ? 0 : (L2Hits * 100.0 / totalL2);
        double L3Rate = totalL3 == 0 ? 0 : (L3Hits * 100.0 / totalL3);
        System.out.println("L1: Hit Rate " + String.format("%.2f", L1Rate) + "%, Hits: " + L1Hits + ", Misses: " + L1Misses);
        System.out.println("L2: Hit Rate " + String.format("%.2f", L2Rate) + "%, Hits: " + L2Hits + ", Misses: " + L2Misses);
        System.out.println("L3: Hit Rate " + String.format("%.2f", L3Rate) + "%, Hits: " + L3Hits + ", Misses: " + L3Misses);
        long totalHits = L1Hits + L2Hits + L3Hits;
        long totalRequests = totalL1 + totalL2 + totalL3;
        double overallRate = totalRequests == 0 ? 0 : (totalHits * 100.0 / totalRequests);
        System.out.println("Overall: Hit Rate " + String.format("%.2f", overallRate) + "%, Total Requests: " + totalRequests);
    }
    public static void main(String[] args) {
        Multi_Level_Cache_System cacheSystem = new Multi_Level_Cache_System();
        System.out.println("Request 1: " + cacheSystem.getVideo("video_123").content);
        System.out.println("Request 2: " + cacheSystem.getVideo("video_123").content);
        System.out.println("Request 3: " + cacheSystem.getVideo("video_999").content);
        cacheSystem.getStatistics();
    }
}

