package datastructure.tree;

import java.io.*;


/**
 * 이진 검색 트리 - BOJ5639
 * -----------------
 * Input 1
 * 50
 * 30
 * 24
 * 5
 * 28
 * 45
 * 98
 * 52
 * 60
 *
 * Output 1
 * 5
 * 28
 * 24
 * 45
 * 30
 * 60
 * 52
 * 98
 * 50
 * -----------------
 */
public class BOJ5639 {

    private static class Node {
        int value;
        Node left, right;

        public Node(int value) {
            this.value = value;
        }

        public void add(int value) {
            if (value > this.value) {
                if (right == null) {
                    right = new Node(value);
                } else {
                    right.add(value);
                }
            } else {
                if (left == null) {
                    left = new Node(value);
                } else {
                    left.add(value);
                }
            }
        }
    }

    private static StringBuilder result = new StringBuilder();
    private static Node root;

    public static void postOrderTraversal(Node node) {
        Node current = node;

        if (current == null) {
            return;
        } else {
            postOrderTraversal(node.left);
            postOrderTraversal(node.right);
        }
        result.append(current.value).append('\n');
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            int num = Integer.parseInt(line);
            if (root == null) root = new Node(num);
            else root.add(num);
        }
        postOrderTraversal(root);

        // write the result
        result.delete(result.length() - 1, result.length());
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
