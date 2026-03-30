import java.util.*;

public class Problem5 {

    public static void main(String[] args) {
        String[] arr = {"accA", "accB", "accB", "accC"};
        String target = "accB";

        int first = -1, last = -1;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(target)) {
                if (first == -1) first = i;
                last = i;
            }
        }

        System.out.println("First: " + first + ", Last: " + last);

        int index = Arrays.binarySearch(arr, target);
        System.out.println("Binary Index: " + index);

        int count = 0;
        for (String s : arr)
            if (s.equals(target)) count++;

        System.out.println("Count: " + count);
    }
}