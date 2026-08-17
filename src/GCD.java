/*
@author Benjamin Le
where starting m always larger than n
 */

import java.util.ArrayList;
import java.util.Random;
public class GCD {
    public static long gcdRecursive(long m, long n) {
        if (n == 0) {
            return m;
        }
        return gcdRecursive(n, m % n);
    }

    public static long gcdNonRecursive(long m, long n) {
        long remainder = Math.floorMod(m, n);
        long intermediate;
        while (remainder != 0) {
            intermediate = m % n;
            m = n;
            n = intermediate;
            remainder = Math.floorMod(m, n);
        }
        return n;
    }

    public static void main(String[] args) {
        Random rand = new Random();
        ArrayList<Long> listM = new ArrayList<Long>();
        ArrayList<Long> listN = new ArrayList<Long>();
        for (int i = 0; i < 10; i++) {
            listM.add(rand.nextLong(100000));
            listN.add(rand.nextLong(listM.get(i)));//generating random strings that m is always larger than n
        }
        for (int i = 0; i < listM.size(); i++) {
            long startTime = System.nanoTime();
            System.out.println("Increment: " + i+1 + " Recursive GCD of "+
                    listM.get(i) + " and " + listN.get(i)
            + " is: " + gcdRecursive(listM.get(i), listN.get(i)));
            long endTime = System.nanoTime();
            System.out.println("Time taken: " + (endTime - startTime));
        }

        for (int i = 0; i < listM.size(); i++) {
            long startTime = System.nanoTime();
            System.out.println("Increment: " + i + " NonRecursive GCD of "+
                    listM.get(i) + " and " + listN.get(i)
                    + " is: " + gcdNonRecursive(listM.get(i), listN.get(i)));
            long endTime = System.nanoTime();
            System.out.println("Time taken: " + (endTime - startTime));
        }

        //example of really large numbers that have known GCD
        long m = 997*12034;
        long n = 997*2307;
        long startTime = System.nanoTime();
        System.out.println("Recursive GCD of "+
                m + " and " + n
                + " is: " + gcdRecursive(m, n));
        long endTime = System.nanoTime();
        long startTime2 = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime));
        System.out.println("NonRecursive GCD of "+
                m + " and " + n
                + " is: " + gcdNonRecursive(m, n));
        long endTime2 = System.nanoTime();
        System.out.println("Time taken: " + (endTime2 - startTime2));

    }
}
