package DAY18;

import java.util.*;

class Task0010_Binary_tree {
        static class Node {
            int data;
            Node left, right;

            Node(int value) {
                data = value;
                left = right = null;
            }
        }

        public static void printCornerNodes(Node root) {
            if (root == null)
                return;

            Queue<Node> queue = new LinkedList<>();
            queue.add(root);

            while (!queue.isEmpty()) {
                int size = queue.size();
                Node first = null, last = null;

                for (int i = 0; i < size; i++) {
                    Node current = queue.poll();

                    if (i == 0) first = current;
                    if (i == size - 1) last = current;

                    if (current.left != null) queue.add(current.left);
                    if (current.right != null) queue.add(current.right);
                }

                if (first != null) System.out.print(first.data + " ");
                if (last != null && last != first) System.out.print(last.data);
                System.out.println();
            }
        }

        public static Node buildTree() {
            Node root = new Node(1);
            root.left = new Node(2);
            root.right = new Node(3);

            root.left.left = new Node(4);
            root.left.right = new Node(5);
            root.right.left = new Node(6);
            root.right.right = new Node(7);

            root.left.left.left = new Node(8);
            root.left.left.right = new Node(9);
            root.left.right.left = new Node(10);
            root.left.right.right = new Node(11);

            root.right.left.left = new Node(12);
            root.right.left.right = new Node(13);
            root.right.right.left = new Node(14);
            root.right.right.right = new Node(15);

            return root;
        }

        public static void main(String[] args) {
            Node root = buildTree();
            System.out.println("Corner nodes are:");
            printCornerNodes(root);
        }
    }