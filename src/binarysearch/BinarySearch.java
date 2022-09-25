package binarysearch;


public class BinarySearch {

    public static int binarySearch(int[] values, int num) {
        int begin = 0, end = values.length - 1;
        int mid = 0, pivot = 0;

        while (begin <= end) {
            pivot = (end + begin) >> 1;
            mid = values[pivot];

            if (mid > num) end = pivot - 1;
            else if (mid < num) begin = pivot + 1;
            else return pivot;
        }

        return -1;
    }

    public static int binarySearch2(int[] values, int num) {
        int begin = 0, pivot = 0, end = values.length - 1;

        while (begin + 1 < end) {
            pivot = (end + begin) >> 1;

            if (values[pivot] > num) end = pivot;
            else if (values[pivot] < num) begin = pivot;
            else break;
        }

        return begin;
    }

    public static int upperBound(int[] values, int num) {
        int begin = 0, end = values.length;
        int mid = 0, pivot = 0;

        while (begin < end) {
            pivot = (end + begin) >> 1;
            mid = values[pivot];

            if (mid > num) end = pivot;
            else begin = pivot + 1;
        }

        return end;
    }

    public static int lowerBound(int[] values, int num) {
        int begin = 0, end = values.length;
        int mid = 0, pivot = 0;

        while (begin < end) {
            pivot = (end + begin) >> 1;
            mid = values[pivot];

            if (mid >= num) end = pivot;
            else begin = pivot + 1;
        }

        return begin;
    }
}
