package Model;

/**
 * Full implementation of an Adelson-Velsky-Landis (AVL) tree. AVL trees are self-balancing binary search trees that
 * maintain a balanced structure to ensure efficient operations in O(log n) time. They achieve this by ensuring that the
 * height difference between any two neighbouring subtrees is bound by [-1, 1].
 */
public class AVLTree<T extends Comparable<T>> extends BinarySearchTree<T> {

    private Node<T> root;

    @Override
    public Tree<T> insert(T data) {
        root = insert(data, root);
        return this;
    }

    private Node<T> insert(T data, Node<T> node) {
        if(node == null) {
            return new Node<>(data);
        }
        if(data.compareTo(node.getData()) < 0) {
            node.setLeftChild(insert(data, node.getLeftChild()));
        } else if(data.compareTo(node.getData()) > 0) {
            node.setRightChild(insert(data, node.getRightChild()));
        } else {
            return node;
        }
        updateHeight(node);
        return applyRotation(node);
    }

    @Override
    public void delete(T data) {
        root = delete(data, root);
    }

    //
    private Node<T> delete(T data, Node<T> node) {
        if(node == null) {
            return null;
        }
        if(data.compareTo(node.getData()) < 0) {
            node.setLeftChild(delete(data, node.getLeftChild()));
        } else if(data.compareTo(node.getData()) > 0) {
            node.setRightChild(delete(data, node.getRightChild()));
        } else {
            if(node.getLeftChild() == null) {
                return node.getRightChild();
            } else if(node.getRightChild() == null) {
                return node.getLeftChild();
            }
            node.setData(getMax(node.getLeftChild()));
            node.setLeftChild(delete(node.getData(), node.getLeftChild()));
        }
        updateHeight(node);
        return applyRotation(node);
    }

    private void updateHeight(Node<T> node) {
        int maxHeight = Math.max(height(node.getLeftChild()), height(node.getRightChild()));
        node.setHeight(maxHeight + 1);
    }

    private int height(Node<T> node) {
        return node != null ? node.getHeight() : 0;
    }

    private int balance(Node<T> node) {
        return node != null ? height(node.getLeftChild()) - height(node.getRightChild()) : 0;
    }

    //we apply right rotations in the case of positive balances and left rotations in the case of negative balances
    private Node<T> applyRotation(Node<T> node) {
        int balance = balance(node);
        //left heavy case
        if(balance > 1) {
            if(balance(node.getLeftChild()) < 0) {
                node.setLeftChild(rotateLeft(node.getLeftChild()));
            }
            return rotateRight(node);
        }
        //right heavy case
        if(balance < -1) {
            if(balance(node.getRightChild()) > 0) {
                node.setRightChild(rotateRight(node.getRightChild()));
            }
            return rotateLeft(node);
        }
        return node;
    }

    /// these methods do not adjust the parent pointer after rotation
    private Node<T> rotateRight(Node<T> node) {
        Node<T> leftNode = node.getLeftChild();
        Node<T> centerNode = leftNode.getRightChild();
        leftNode.setRightChild(node);
        node.setLeftChild(centerNode);
        updateHeight(node);
        updateHeight(leftNode);
        return leftNode;
    }

    private Node<T> rotateLeft(Node<T> node) {
        Node<T> rightNode = node.getRightChild();
        Node<T> centerNode = rightNode.getLeftChild();
        rightNode.setLeftChild(node);
        node.setLeftChild(centerNode);
        updateHeight(node);
        updateHeight(rightNode);
        return rightNode;
    }
}
