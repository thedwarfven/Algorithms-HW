public class test {
    public static long gcdRecursive(long m, long n) {
        if (n == 0) {
            return m;
        }
        return gcdRecursive(n, m%n);
    }
    public static long gcdNonRecursive(long m, long n) {
        long remainder = Math.floorMod(m, n);;
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
        System.out.println(gcdNonRecursive(45048, 12484));
    }
}
