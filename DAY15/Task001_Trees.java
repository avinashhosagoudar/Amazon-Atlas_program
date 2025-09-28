package DAY15;

class  TreeNode{
    int data;
    TreeNode right;
    TreeNode left;

    TreeNode(int value){
        data = value;
        left = null;
        right = null;
    }
}
public class Task001_Trees {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(20);
        root.right = new TreeNode(30);

        System.out.println("Root: " + root.data);
        System.out.println("Left: " + root.left.data);
        System.out.println("Right: " + root.left.data);
    }
}
