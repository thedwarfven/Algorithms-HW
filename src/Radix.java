/*
@author Benjamin Le
Implement radix sort and test with 100000 random numbers as input
 */
import java.util.Arrays;
import java.util.Random;

public class Radix {
    public static void radixSort(int[] arr) {
        int max = getMax(arr);

        for (int place = 1; max / place > 0; place *= 10) {
            countingSort(arr, place);
        }
    }

    private static void countingSort(int[] arr, int place) {
        int[] output = new int[arr.length];
        int[] count = new int[10];

        for (int value : arr) {
            int digit = (value / place) % 10;
            count[digit]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = arr.length - 1; i >= 0; i--) {
            int digit = (arr[i] / place) % 10;
            output[count[digit] - 1] = arr[i];
            count[digit]--;
        }

        System.arraycopy(output, 0, arr, 0, arr.length);
    }

    private static int getMax(int[] arr) {
        int max = arr[0];
        for (int value : arr) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        Random rand = new Random();
        int[] mainArray = new int[100000];
        for (int i = 0; i < 100000; i++) {
            mainArray[i] = rand.nextInt(1000000);
        }
        long  startTime = System.nanoTime();
        radixSort(mainArray);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Radix Sort Time (ns): " + totalTime);
        boolean sorted = true;
        for (int i = 0; i < 100000-1; i++) {
            if (mainArray[i] > mainArray[i + 1]) {
                sorted = false;
                System.out.println("Array not sorted");
                break;
            }
        }
        if (sorted) {
            System.out.println("Sorted array");
        }
    }
}
