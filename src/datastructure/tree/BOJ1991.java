package datastructure.tree;

import java.io.*;
import java.util.*;


/**
 * 트리 순회 - BOJ1991
 * -----------------
 * Input 1
 * 7
 * A B C
 * B D .
 * C E F
 * E . .
 * F . G
 * D . .
 * G . .
 *
 * Output 1
 * ABDCEFG
 * DBAECFG
 * DBEGFCA
 * -----------------
 */
public class BOJ1991 {

    private static class Node {
        char value;
        Node left, right;

        public Node(char value) {
            this.value = value;
        }

        public void addLeft(Node left) {
            this.left = left;
        }

        public void addRight(Node right) {
            this.right = right;
        }
    }

    private static StringBuilder result = new StringBuilder();
    private static Node[] nodes;
    private static Node root;
    private static int N;

    public static void preOrderTraversal(Node node) {
        Node current = node;

        if (current == null) {
            return;
        } else {
            result.append(current.value);
            preOrderTraversal(node.left);
            preOrderTraversal(node.right);
        }
    }

    public static void inOrderTraversal(Node node) {
        Node current = node;

        if (current == null) {
            return;
        } else {
            inOrderTraversal(node.left);
            result.append(current.value);
            inOrderTraversal(node.right);
        }
    }

    public static void postOrderTraversal(Node node) {
        Node current = node;

        if (current == null) {
            return;
        } else {
            postOrderTraversal(node.left);
            postOrderTraversal(node.right);
            result.append(current.value);
        }
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        nodes = new Node[N];
        for (int i = 0; i < N; i++) {
            nodes[i] = new Node((char) ('A' + i));
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            char root = st.nextToken().charAt(0);
            char left = st.nextToken().charAt(0);
            char right = st.nextToken().charAt(0);
            if (left != '.') nodes[root - 'A'].addLeft(nodes[left - 'A']);
            if (right != '.') nodes[root - 'A'].addRight(nodes[right - 'A']);
        }

        root = nodes[0];
        preOrderTraversal(root);
        result.append('\n');
        inOrderTraversal(root);
        result.append('\n');
        postOrderTraversal(root);

        // write the result
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}