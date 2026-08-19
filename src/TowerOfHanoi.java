/*
@author Benjamin Le
implement Tower of Hanoi with 4 and 9 stack
 */

import java.util.Stack;
public class TowerOfHanoi {
    public static void towerOfHanoi(Stack<Integer> source ,Stack<Integer> helper ,Stack<Integer> destination , int n) {
        if (n==0) return;
        towerOfHanoi(source,destination,helper,n-1);//move disk n-1 from source to helper
        destination.push(source.pop());// moving disk n from source to destination
        towerOfHanoi(helper,source,destination,n-1);//move disk n-1 from helper to destination

        System.out.println("N is: " + n);

    }

    public static void main(String[] args) {
        Stack<Integer> a = new Stack<>();
        Stack<Integer> b = new Stack<>();
        Stack<Integer> c = new Stack<>();

        for (int i = 4; i >= 1; i--) {
            a.push(i);
        }
        System.out.println("Initial set up: \n"+a+"\n"+b+"\n"+c);
        towerOfHanoi(a,b,c,4);
        System.out.println("Final product: \n"+a+"\n"+b+"\n"+c);

        Stack<Integer> c2 = new Stack<>();
        for (int i = 9; i >= 1; i--) {
            a.push(i);
        }
        System.out.println("Initial set up: \n"+a+"\n"+b+"\n"+c2);
        towerOfHanoi(a,b,c2,9);
        System.out.println("Final product: \n"+a+"\n"+b+"\n"+c2);
    }
}
