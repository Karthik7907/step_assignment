import java.util.*;
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEnd = false;
}
class SearchEngine {
    private TrieNode root = new TrieNode();
    private HashMap<String, Integer> frequency = new HashMap<>();
    public void insert(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());

            node = node.children.get(c);
        }

        node.isEnd = true;

        frequency.put(query,
                frequency.getOrDefault(query, 0) + 1);
    }
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c))
                return new ArrayList<>();

            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();

        dfs(node, prefix, results);

        PriorityQueue<String> pq =
                new PriorityQueue<>(
                        (a, b) -> frequency.get(a) - frequency.get(b));

        for (String query : results) {

            pq.offer(query);

            if (pq.size() > 10)
                pq.poll();
        }

        List<String> suggestions = new ArrayList<>();

        while (!pq.isEmpty())
            suggestions.add(pq.poll());

        Collections.reverse(suggestions);

        return suggestions;
    }
    private void dfs(TrieNode node, String current,
                     List<String> results) {

        if (node.isEnd)
            results.add(current);

        for (char c : node.children.keySet()) {

            dfs(node.children.get(c),
                    current + c, results);
        }
    }
    public void updateFrequency(String query) {

        frequency.put(query,
                frequency.getOrDefault(query, 0) + 1);
    }
    public static void main(String[] args) {

        SearchEngine system =
                new SearchEngine();
        system.insert("java tutorial");
        system.insert("javascript");
        system.insert("java download");
        system.insert("java tutorial");
        system.insert("java features");
        system.insert("java tutorial");

        System.out.println(system.search("jav"));

        system.updateFrequency("java features");

        System.out.println(system.search("jav"));
    }
}