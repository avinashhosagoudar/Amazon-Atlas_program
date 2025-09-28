package DAY18;
class TreeNode {
    char val;
    TreeNode left, right;

    TreeNode(char val) {
        this.val = val;
    }
}

class Solution {


    public void reverseAlternateLevels(TreeNode root) {
        if (root == null) return;
        reverseUtil(root.left, root.right, 1);
    }


    private void reverseUtil(TreeNode left, TreeNode right, int level) {
        if (left == null || right == null) return;

        if (level % 2 == 1) {
            char temp = left.val;
            left.val = right.val;
            right.val = temp;
        }

        reverseUtil(left.left, right.right, level + 1);
        reverseUtil(left.right, right.left, level + 1);
    }


    public void printLevelOrder(TreeNode root) {
        if (root == null) return;

        java.util.Queue<TreeNode> queue = new java.util.LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int size = queue.size();

            while (size-- > 0) {
                TreeNode node = queue.poll();
                System.out.print(node.val + " ");

                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        Solution sol = new Solution();


        TreeNode root = new TreeNode('A');
        root.left = new TreeNode('B');
        root.right = new TreeNode('C');

        root.left.left = new TreeNode('D');
        root.left.right = new TreeNode('E');
        root.right.left = new TreeNode('F');
        root.right.right = new TreeNode('G');

        root.left.left.left = new TreeNode('H');
        root.left.left.right = new TreeNode('I');
        root.left.right.left = new TreeNode('J');
        root.left.right.right = new TreeNode('K');
        root.right.left.left = new TreeNode('L');
        root.right.left.right = new TreeNode('M');
        root.right.right.left = new TreeNode('N');
        root.right.right.right = new TreeNode('O');

        System.out.println("Before reversal:");
        sol.printLevelOrder(root);

        sol.reverseAlternateLevels(root);

        System.out.println("\nAfter alternate level reversal:");
        sol.printLevelOrder(root);
    }
}