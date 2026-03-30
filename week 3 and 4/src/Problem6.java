public class Problem6 {

    static int floor(int[] arr, int target) {
        int res = -1, low = 0, high = arr.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] <= target) {
                res = arr[mid];
                low = mid + 1;
            } else high = mid - 1;
        }
        return res;
    }

    static int ceil(int[] arr, int target) {
        int res = -1, low = 0, high = arr.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] >= target) {
                res = arr[mid];
                high = mid - 1;
            } else low = mid + 1;
        }
        return res;
    }

    public static void main(String[] args) {
        int[] arr = {10, 25, 50, 100};
        int target = 30;

        System.out.println("Floor: " + floor(arr, target));
        System.out.println("Ceiling: " + ceil(arr, target));
    }
}