 import java.util.*;
public class Username_Availability_Checker {
    private final Map<String, Integer> userMap = new HashMap<>();
    private final Map<String, Integer> attemptMap = new HashMap<>();
    public void registerUser(String username, int userId) {
        if (userMap.containsKey(username)) {
            System.out.println("Username already taken: " + username);
        } else {
            userMap.put(username, userId);
        }
    }
    public boolean checkAvailability(String username) {
        recordAttempt(username);
        return !userMap.containsKey(username);
    }
    public List<String> suggestAlternatives(String username) {
        Set<String> suggestions = new LinkedHashSet<>(); // avoids duplicates, preserves order
        for (int i = 1; i <= 3; i++) {
            String alt = username + i;
            if (!userMap.containsKey(alt)) {
                suggestions.add(alt);
            }
        }
        String replaced = username.replace("_", ".");
        if (!userMap.containsKey(replaced)) {
            suggestions.add(replaced);
        }
        String prefixed = "the_" + username;
        if (!userMap.containsKey(prefixed)) {
            suggestions.add(prefixed);
        }
        return new ArrayList<>(suggestions);
    }
    private void recordAttempt(String username) {
        attemptMap.put(username, attemptMap.getOrDefault(username, 0) + 1);
    }
    public String getMostAttempted() {
        return attemptMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
    public static void main(String[] args) {
        Username_Availability_Checker checker = new Username_Availability_Checker();
        checker.registerUser("john_doe", 12345);
        checker.registerUser("jane_smith", 67890);
        System.out.println("john_doe available? " + checker.checkAvailability("john_doe"));
        System.out.println("jane_smith available? " + checker.checkAvailability("jane_smith"));
        System.out.println("new_user available? " + checker.checkAvailability("new_user"));
        System.out.println("Suggestions for john_doe: " + checker.suggestAlternatives("john_doe"));
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        System.out.println("Most attempted username: " + checker.getMostAttempted());
    }
}

