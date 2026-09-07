/*
@author Benjamin Le
implement insertion, merge, quick, bubble sort
implement main
      array of randomly chosen 1000000 numbers.
     copy it to iarray, barray, marray, qarray
     call insertion( iarray), merge(marray), quck(qarray), bubble(b array)
   compare their execution time
 */


import java.util.Arrays;
import java.util.Random;
import java.util.Collections;

public class HW0903Sort {
    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int currentElement = arr[i];
            int k;
            for (k = i-1; k>=0 && arr[k]>currentElement;k--){
                arr[k+1] = arr[k];
            }
            arr[k+1] = currentElement;
        }
    }

    public static void bubbleSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            for (int j = i-1; j >= 0; j--) {
                if (arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }
    public static void merge(int[] list1, int[] list2, int[] temp) {
        int current1 = 0;
        int current2 = 0;
        int current3 = 0;

        while (current1 < list1.length && current2 < list2.length) {
            if (list1[current1] < list2[current2])
                temp[current3++] = list1[current1++];
            else
                temp[current3++] = list2[current2++];
        }

        while (current1 < list1.length)
            temp[current3++] = list1[current1++];
        while (current2 < list2.length)
            temp[current3++] = list2[current2++];
    }

    public static void mergeSort(int[] arr) {
        if (arr.length > 1) {
            int[] firstHalf = new int[arr.length/2];
            System.arraycopy(arr, 0, firstHalf, 0, arr.length/2);
            mergeSort(firstHalf);

            int secondHalfLength = arr.length - arr.length/2;
            int[] secondHalf = new int[secondHalfLength];
            System.arraycopy(arr, arr.length/2, secondHalf, 0, secondHalfLength);
            mergeSort(secondHalf);

            merge(firstHalf, secondHalf, arr);
        }
    }

    public static void quickSort(int[] list) {
        quickSort(list, 0, list.length - 1);
    }

    private static void quickSort(int[] list, int first, int last) {
        if (last > first) {
            int pivotIndex = partition(list, first, last);
            quickSort(list, first, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, last);
        }
    }

    private static int partition(int[] list, int first, int last) {
        int pivot = list[first]; // Choose the first element as the pivot
        int low = first + 1; // Index for forward search
        int high = last; // Index for backward search

        while (high > low) {
            // Search forward from left
            while (low <= high && list[low] <= pivot)
                low++;
            // Search backward from right
            while (low <= high && list[high] > pivot)
                high--;
            // Swap two elements in the list
            if (high > low) {
                int temp = list[high];
                list[high] = list[low];
                list[low] = temp;
            }
        }
        while (high > first && list[high] >= pivot)
            high--;
        if (pivot > list[high]) {
            list[first] = list[high];
            list[high] = pivot;
            return high;
        }
        else {
            return first;
        }
    }


    public static void main(String[] args) {
        Random rand = new Random();
        int[] mainArray = new int[1000000];
        for (int i = 0; i < 1000000; i++) {
            mainArray[i] = rand.nextInt(1000000);
        }
        int[] insertSortArray = Arrays.copyOf(mainArray, mainArray.length);
        int[] bubbleSortArray = Arrays.copyOf(mainArray, mainArray.length);
        int[] mergeSortArray = Arrays.copyOf(mainArray, mainArray.length);
        int[] quickSortArray = Arrays.copyOf(mainArray, mainArray.length);

        long  startTime = System.nanoTime();
        insertionSort(insertSortArray);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Insertion Sort Time (ns): " + totalTime);

        startTime = System.nanoTime();
        bubbleSort(bubbleSortArray);
        endTime = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("Bubble Sort Time (ns): " + totalTime);

        startTime = System.nanoTime();
        mergeSort(mergeSortArray);
        endTime = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("Merge Sort Time (ns): " + totalTime);

        startTime = System.nanoTime();
        quickSort(quickSortArray);
        endTime = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("Quick Sort Time (ns): " + totalTime);



    }
}
