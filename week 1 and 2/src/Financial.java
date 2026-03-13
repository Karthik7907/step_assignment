import java.util.*;

class Transaction {

    int id;
    int amount;
    String merchant;
    String account;
    int time; // minutes since midnight

    Transaction(int id, int amount, String merchant, String account, int time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

class Financial {

    List<Transaction> transactions;

    Financial(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    // Classic Two-Sum
    public void findTwoSum(int target) {

        Map<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction pair = map.get(complement);

                System.out.println(
                        "Pair Found: (" + pair.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    // Two-Sum with 1 hour time window
    public void findTwoSumTimeWindow(int target) {

        Map<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction pair = map.get(complement);

                if (Math.abs(t.time - pair.time) <= 60) {

                    System.out.println(
                            "Time Window Pair: (" + pair.id + ", " + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }

    // Duplicate detection
    public void detectDuplicates() {

        Map<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "_" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());

            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.println("Duplicate detected: " + key);

                for (Transaction t : list)
                    System.out.println("Account: " + t.account);
            }
        }
    }

    // K-Sum using recursion
    public void findKSum(int k, int target) {

        List<Integer> nums = new ArrayList<>();

        for (Transaction t : transactions)
            nums.add(t.amount);

        List<List<Integer>> result =
                kSum(nums, target, k, 0);

        System.out.println("K-Sum Results: " + result);
    }

    private List<List<Integer>> kSum(List<Integer> nums,
                                     int target, int k, int start) {

        List<List<Integer>> res = new ArrayList<>();

        if (k == 2) {

            Map<Integer, Integer> map = new HashMap<>();

            for (int i = start; i < nums.size(); i++) {

                int complement = target - nums.get(i);

                if (map.containsKey(complement)) {

                    res.add(Arrays.asList(complement, nums.get(i)));
                }

                map.put(nums.get(i), i);
            }

            return res;
        }

        for (int i = start; i < nums.size(); i++) {

            for (List<Integer> subset :
                    kSum(nums, target - nums.get(i), k - 1, i + 1)) {

                List<Integer> temp = new ArrayList<>();

                temp.add(nums.get(i));
                temp.addAll(subset);

                res.add(temp);
            }
        }

        return res;
    }


    public static void main(String[] args) {

        List<Transaction> data = new ArrayList<>();

        data.add(new Transaction(1, 500, "StoreA", "acc1", 600));
        data.add(new Transaction(2, 300, "StoreB", "acc2", 615));
        data.add(new Transaction(3, 200, "StoreC", "acc3", 630));
        data.add(new Transaction(4, 500, "StoreA", "acc4", 700));

        Financial fd = new Financial(data);

        fd.findTwoSum(500);

        fd.findTwoSumTimeWindow(500);

        fd.detectDuplicates();

        fd.findKSum(3, 1000);
    }
}