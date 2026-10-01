import java.util.Scanner;
import java.io.*;
public class HuffmanCode {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a text: ");
        String text = input.nextLine();
        int[] counts = getCharacterFrequency(text); // Count frequency
        System.out.printf("%-15s%-15s%-15s%-15s%n",
                "Char Value", "Character", "Frequency", "Code");
        Tree tree = getHuffmanTree(counts); // Create a Huffman tree
        String[] codes = getCode(tree == null ? null : tree.root); // Get codes
        for (int i = 0; i < codes.length; i++)
            if (counts[i] != 0) // (char)[i] is not in text if counts[i] is 0
                System.out.printf("%-15d%-15s%-15d%-15s%n",
                        i, (char)i + "", counts[i], codes[i]);
        byte[] compressed = compress(text);
        System.out.println("Compressed bytes (including header): " + compressed.length);
        System.out.println("Decoded text: " + decompress(compressed));
    }

    // Format: magic, symbol count, (char, frequency) entries, then packed bits.
    // Frequencies rebuild the same tree and preserve the original text length.
    public static byte[] compress(String text) {
        int[] counts = getCharacterFrequency(text);
        Tree tree = getHuffmanTree(counts);
        String[] codes = getCode(tree == null ? null : tree.root);
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(buffer);
            out.writeInt(0x48554631); // HUF1
            int symbols = 0;
            for (int count : counts) if (count > 0) symbols++;
            out.writeInt(symbols);
            for (int i = 0; i < counts.length; i++) {
                if (counts[i] > 0) {
                    out.writeChar(i);
                    out.writeInt(counts[i]);
                }
            }
            int value = 0, used = 0;
            for (int i = 0; i < text.length(); i++) {
                String code = codes[text.charAt(i)];
                for (int j = 0; j < code.length(); j++) {
                    value = (value << 1) | (code.charAt(j) - '0');
                    if (++used == 8) {
                        out.writeByte(value);
                        value = 0;
                        used = 0;
                    }
                }
            }
            if (used > 0) out.writeByte(value << (8 - used));
            out.flush();
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String decompress(byte[] data) {
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
            if (in.readInt() != 0x48554631) throw new IllegalArgumentException("Invalid header");
            int symbols = in.readInt();
            if (symbols < 0 || symbols > Character.MAX_VALUE + 1)
                throw new IllegalArgumentException("Invalid symbol count");
            int[] counts = new int[Character.MAX_VALUE + 1];
            long length = 0;
            for (int i = 0; i < symbols; i++) {
                char ch = in.readChar();
                int count = in.readInt();
                if (count <= 0 || counts[ch] != 0)
                    throw new IllegalArgumentException("Invalid frequency");
                counts[ch] = count;
                length += count;
            }
            if (length > Integer.MAX_VALUE) throw new IllegalArgumentException("Text too large");
            Tree tree = getHuffmanTree(counts);
            StringBuilder result = new StringBuilder();
            if (tree == null) {
                if (in.available() != 0) throw new IllegalArgumentException("Unexpected data");
                return "";
            }
            Tree.Node node = tree.root;
            int value = 0, remaining = 0;
            while (result.length() < length) {
                if (remaining == 0) {
                    value = in.readUnsignedByte();
                    remaining = 8;
                }
                int bit = (value >>> --remaining) & 1;
                if (tree.root.left == null) {
                    if (bit != 0) throw new IllegalArgumentException("Invalid single-symbol code");
                    result.append(tree.root.element);
                } else {
                    node = bit == 0 ? node.left : node.right;
                    if (node.left == null) {
                        result.append(node.element);
                        node = tree.root;
                    }
                }
            }
            if (in.available() != 0 || (value & ((1 << remaining) - 1)) != 0)
                throw new IllegalArgumentException("Unexpected data or nonzero padding");
            String text = result.toString();
            if (!java.util.Arrays.equals(counts, getCharacterFrequency(text)))
                throw new IllegalArgumentException("Frequency mismatch");
            return text;
        } catch (IOException e) {
            throw new IllegalArgumentException("Truncated compressed data", e);
        }
    }

     public static String[] getCode(Tree.Node root) {
     if (root == null) return new String[Character.MAX_VALUE + 1];
     String[] codes = new String[Character.MAX_VALUE + 1];
     root.code = root.left == null ? "0" : "";
     assignCode(root, codes);
     return codes;
     }

     private static void assignCode(Tree.Node root, String[] codes) {
     if (root.left != null) {
     root.left.code = root.code + "0";
     assignCode(root.left, codes);
     root.right.code = root.code + "1";
     assignCode(root.right, codes);
     }
     else {
     codes[(int)root.element] = root.code;
     }
     }


    public static Tree getHuffmanTree(int[] counts) {
        // Create a heap to hold trees
        Heap<Tree> heap = new Heap<>(); // Defined in Section 23.6.5
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > 0)
                heap.add(new Tree(counts[i], (char)i)); // A leaf node tree
        }

        while (heap.getSize() > 1) {
            Tree t1 = heap.remove(); // Remove the smallest weight tree
            Tree t2 = heap.remove(); // Remove the next smallest weight
            heap.add(new Tree(t1, t2)); // Combine two trees
        }

        return heap.getSize() == 0 ? null : heap.remove(); // The final tree
    }

    /** Get the frequency of the characters */
    public static int[] getCharacterFrequency(String text) {
        int[] counts = new int[Character.MAX_VALUE + 1]; // All UTF-16 char values

        for (int i = 0; i < text.length(); i++)
            counts[(int)text.charAt(i)]++; // Count the character in text

        return counts;
    }

    /** Define a Huffman coding tree */
    public static class Tree implements Comparable<Tree> {
        Node root; // The root of the tree

        /** Create a tree with two subtrees */
        public Tree(Tree t1, Tree t2) {
            root = new Node();
            root.left = t1.root;
            root.right = t2.root;
            root.weight = t1.root.weight + t2.root.weight;
            root.minElement = Math.min(t1.root.minElement, t2.root.minElement);
        }

        /** Create a tree containing a leaf node */
        public Tree(int weight, char element) {
            root = new Node(weight, element);
        }

        @Override
        public int compareTo(Tree t) {
            int comparison = Integer.compare(t.root.weight, root.weight);
            return comparison != 0 ? comparison : Integer.compare(t.root.minElement, root.minElement);
        }

        public class Node {
            char element; // Stores the character for a leaf node
            int minElement; // Tie-breaker for deterministic trees
            int weight; // weight of the subtree rooted at this node
            Node left; // Reference to the left subtree
            Node right; // Reference to the right subtree
            String code = ""; // The code of this node from the root

            /** Create an empty node */
            public Node() {
            }

            /** Create a node with the specified weight and character */
            public Node(int weight, char element) {
                this.weight = weight;
                this.element = element;
                this.minElement = element;
            }
        }
    }
}

