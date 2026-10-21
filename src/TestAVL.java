import java.util.*;

/** Standalone checks. No JavaFX or external test library required. */
public class TestAVL {
    private static int checks;
    private static void require(boolean condition) {
        checks++;
        if (!condition) throw new AssertionError("Check " + checks + " failed");
    }
    private static int inspect(AVLTree<Integer> tree, BST.TreeNode<Integer> node,
                               long lower, long upper) {
        if (node == null) return 0;
        require(node.element > lower && node.element < upper);
        int left = inspect(tree, node.left, lower, node.element);
        int right = inspect(tree, node.right, node.element, upper);
        require(Math.abs(left - right) <= 1);
        require(tree.balanceFactor(node) == left - right);
        return 1 + Math.max(left, right);
    }
    private static void verify(AVLTree<Integer> tree, TreeSet<Integer> expected) {
        require(inspect(tree, tree.getRoot(), Long.MIN_VALUE, Long.MAX_VALUE) == tree.getHeight());
        List<Integer> actual = new ArrayList<>();
        for (int value : tree) actual.add(value);
        require(actual.equals(new ArrayList<>(expected)));
        require(tree.getSize() == expected.size());
    }
    public static void main(String[] args) {
        int[] counts = {0, 1, 2, 4, 7, 12, 20, 33, 54, 88, 143, 232, 376};
        for (int h = 0; h <= 20; h++) {
            AVLTree<Integer> tree = AVLTree.minimumNodeTree(h);
            require(tree.getHeight() == h);
            require(tree.getSize() == AVLTree.MinNode(h));
            if (h < counts.length) require(tree.getSize() == counts[h]);
            TreeSet<Integer> expected = new TreeSet<>();
            for (int i = 1; i <= tree.getSize(); i++) expected.add(i);
            verify(tree, expected);
        }
        require(AVLTree.MinNode(44) == 1836311902);
        for (int bad : new int[]{-1, 45, Integer.MAX_VALUE}) {
            try { AVLTree.MinNode(bad); throw new AssertionError("Missing rejection"); }
            catch (IllegalArgumentException expected) { require(true); }
        }
        for (Integer[] order : new Integer[][]{{3,2,1}, {1,2,3}, {3,1,2}, {1,3,2}}) {
            AVLTree<Integer> tree = new AVLTree<>(order);
            require(tree.getRoot().element == 2);
            verify(tree, new TreeSet<>(Arrays.asList(order)));
        }
        Random random = new Random(42);
        AVLTree<Integer> tree = new AVLTree<>();
        TreeSet<Integer> expected = new TreeSet<>();
        for (int i = 0; i < 10000; i++) {
            int value = random.nextInt(500);
            if (random.nextBoolean()) require(tree.insert(value) == expected.add(value));
            else require(tree.delete(value) == expected.remove(value));
            verify(tree, expected);
        }
        for (int value : new ArrayList<>(expected)) {
            require(tree.delete(value));
            expected.remove(value);
            verify(tree, expected);
        }
        // Generated trees must support ordinary AVL updates too.
        tree = AVLTree.minimumNodeTree(10);
        expected.clear();
        for (int i = 1; i <= tree.getSize(); i++) expected.add(i);
        List<Integer> deletionOrder = new ArrayList<>(expected);
        Collections.shuffle(deletionOrder, random);
        for (int value : deletionOrder) {
            require(tree.delete(value));
            expected.remove(value);
            verify(tree, expected);
        }
        AVLTree<Integer> reverse = new AVLTree<Integer>(Comparator.reverseOrder());
        for (int i = 0; i < 100; i++) reverse.insert(i);
        for (int i = 0; i < 100; i++) require(reverse.getKthSmallest(i + 1) == 99 - i);
        for (int i = 0; i < 100; i++) require(reverse.delete(i));
        require(reverse.isEmpty());
        tree.insert(10);
        tree.clear();
        require(tree.getHeight() == 0 && tree.size() == 0);
        System.out.println("Passed " + checks + " checks.");
    }
}
