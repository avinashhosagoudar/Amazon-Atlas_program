package DAY17;

class Node {
    int data;
    Node parent, left, right;
    boolean isRed;

    public Node(int data) {
        this.data = data;
        this.isRed = true;
    }
}

class RedBlackTree {
    private Node root;

    public void insert(int data) {
        Node newNode = new Node(data);
        root = bstInsert(root, newNode);
        fixInsert(newNode);
    }

    private Node bstInsert(Node root, Node node) {
        if (root == null)
            return node;

        if (node.data < root.data) {
            root.left = bstInsert(root.left, node);
            root.left.parent = root;
        } else if (node.data > root.data) {
            root.right = bstInsert(root.right, node);
            root.right.parent = root;
        }
        return root;
    }

    private void fixInsert(Node node) {
        Node parent, grandparent;

        while (node != root && node.parent.isRed) {
            parent = node.parent;
            grandparent = parent.parent;

            if (parent == grandparent.left) {
                Node uncle = grandparent.right;

                if (uncle != null && uncle.isRed) {
                    parent.isRed = false;
                    uncle.isRed = false;
                    grandparent.isRed = true;
                    node = grandparent;
                } else {
                    if (node == parent.right) {
                        node = parent;
                        rotateLeft(node);
                    }
                    parent.isRed = false;
                    grandparent.isRed = true;
                    rotateRight(grandparent);
                }
            } else {
                Node uncle = grandparent.left;

                if (uncle != null && uncle.isRed) {
                    parent.isRed = false;
                    uncle.isRed = false;
                    grandparent.isRed = true;
                    node = grandparent;
                } else {
                    if (node == parent.left) {
                        node = parent;
                        rotateRight(node);
                    }
                    parent.isRed = false;
                    grandparent.isRed = true;
                    rotateLeft(grandparent);
                }
            }
        }
        root.isRed = false;
    }

    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;

        if (rightChild.left != null)
            rightChild.left.parent = node;

        rightChild.parent = node.parent;

        if (node.parent == null)
            root = rightChild;
        else if (node == node.parent.left)
            node.parent.left = rightChild;
        else
            node.parent.right = rightChild;

        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;

        if (leftChild.right != null)
            leftChild.right.parent = node;

        leftChild.parent = node.parent;

        if (node.parent == null)
            root = leftChild;
        else if (node == node.parent.right)
            node.parent.right = leftChild;
        else
            node.parent.left = leftChild;

        leftChild.right = node;
        node.parent = leftChild;
    }

    public void printTree() {
        inorder(root);
        System.out.println();
    }

    private void inorder(Node node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.data + (node.isRed ? "R " : "B "));
            inorder(node.right);
        }
    }
}

public class Task04_Red_Black_Insrt {
    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(15);
        tree.insert(25);
        tree.insert(5);
        System.out.println("Red-Black Tree (inorder):");
        tree.printTree();
    }
}