import java.util.Comparator;

/** AVL extension of your BST. Heights count levels: null = 0, leaf = 1. */
public class AVLTree<E> extends BST<E> {
    public AVLTree() { super(); }
    public AVLTree(Comparator<E> comparator) { super(comparator); }
    public AVLTree(E[] elements) {
        this();
        for (E e : elements) insert(e);
    }

    public static class AVLTreeNode<E> extends TreeNode<E> {
        private int height = 1;
        public AVLTreeNode(E element) { super(element); }
    }

    @Override
    protected TreeNode<E> createNewNode(E e) { return new AVLTreeNode<>(e); }

    private int height(TreeNode<E> node) {
        return node == null ? 0 : ((AVLTreeNode<E>) node).height;
    }

    public int getHeight() { return height(root); }

    /** Positive means the left subtree is taller. */
    public int balanceFactor(TreeNode<E> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(TreeNode<E> node) {
        ((AVLTreeNode<E>) node).height = 1 + Math.max(height(node.left), height(node.right));
    }

    private TreeNode<E> rotateRight(TreeNode<E> top) {
        TreeNode<E> pivot = top.left;
        top.left = pivot.right;
        pivot.right = top;
        updateHeight(top);
        updateHeight(pivot);
        return pivot;
    }

    private TreeNode<E> rotateLeft(TreeNode<E> top) {
        TreeNode<E> pivot = top.right;
        top.right = pivot.left;
        pivot.left = top;
        updateHeight(top);
        updateHeight(pivot);
        return pivot;
    }

    private TreeNode<E> rebalance(TreeNode<E> node) {
        updateHeight(node);
        if (balanceFactor(node) > 1) {
            if (balanceFactor(node.left) < 0) node.left = rotateLeft(node.left); // LR
            return rotateRight(node); // LL
        }
        if (balanceFactor(node) < -1) {
            if (balanceFactor(node.right) > 0) node.right = rotateRight(node.right); // RL
            return rotateLeft(node); // RR
        }
        return node;
    }

    @Override
    public boolean insert(E e) {
        int previousSize = size;
        root = insert(root, e);
        return size != previousSize;
    }

    private TreeNode<E> insert(TreeNode<E> node, E e) {
        if (node == null) {
            size++;
            return createNewNode(e);
        }
        int comparison = c.compare(e, node.element);
        if (comparison < 0) node.left = insert(node.left, e);
        else if (comparison > 0) node.right = insert(node.right, e);
        else return node;
        return rebalance(node);
    }

    @Override
    public boolean delete(E e) {
        int previousSize = size;
        root = delete(root, e);
        return size != previousSize;
    }

    private TreeNode<E> delete(TreeNode<E> node, E e) {
        if (node == null) return null;
        int comparison = c.compare(e, node.element);
        if (comparison < 0) node.left = delete(node.left, e);
        else if (comparison > 0) node.right = delete(node.right, e);
        else {
            if (node.left == null || node.right == null) {
                size--;
                return node.left != null ? node.left : node.right;
            }
            TreeNode<E> successor = node.right;
            while (successor.left != null) successor = successor.left;
            node.element = successor.element;
            node.right = delete(node.right, successor.element);
        }
        return rebalance(node);
    }

    /** Your recurrence, evaluated iteratively to avoid repeated recursion.
     *  int is exact through height 44. Height 45 would overflow. */
    public static int MinNode(int h) {
        if (h < 0) throw new IllegalArgumentException("Height must be nonnegative.");
        if (h > 44) throw new IllegalArgumentException("The count exceeds int at height 45.");
        if (h == 0) return 0;
        int previous = 0;
        int current = 1;
        for (int level = 2; level <= h; level++) {
            int next = previous + current + 1;
            previous = current;
            current = next;
        }
        return current;
    }

    /** Build one minimum-node AVL tree of EXACTLY h levels.
     *  The returned tree has integer keys 1..MinNode(h) in BST order.
     *  Direct linking preserves the desired shape. Insertion might change it.
     *  Limit allocation to 17,710 nodes (height 20). Counting supports height 44. */
    public static AVLTree<Integer> minimumNodeTree(int h) {
        if (h < 0 || h > 20)
            throw new IllegalArgumentException("Tree generation supports heights 0 through 20.");
        AVLTree<Integer> tree = new AVLTree<>();
        tree.root = buildMinimum(h, new int[]{1});
        tree.size = MinNode(h);
        return tree;
    }

    private static AVLTreeNode<Integer> buildMinimum(int h, int[] nextKey) {
        if (h == 0) return null;
        // A minimum AVL tree has minimum subtrees of heights h-1 and h-2.
        AVLTreeNode<Integer> left = buildMinimum(h - 1, nextKey);
        AVLTreeNode<Integer> node = new AVLTreeNode<>(nextKey[0]++);
        node.left = left;
        node.right = buildMinimum(Math.max(0, h - 2), nextKey);
        node.height = h;
        return node;
    }
}
