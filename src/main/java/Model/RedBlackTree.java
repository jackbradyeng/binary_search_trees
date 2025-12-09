package Model;

import java.awt.*;

/**
 * Full implementation of a red-black tree, a type of self-balancing search tree. Red-Blacks Trees must satisfy three
 * core properties:
 * 1. The root must be black.
 * 2. Every red node must have a black parent and black child.
 * 3. The number of black nodes in each subtree must be equal.
 */
public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {

    private Node<T> root;

    @Override
    public Tree<T> insert(T data) {
        Node<T> node = new Node<>(data);
        root = insert(root, node);
        recolorAndRotate(node);
        return this;
    }

    private Node<T> insert(Node<T> node, Node<T> nodeToInsert) {
        if(node == null) {
            return nodeToInsert;
        }
        if(nodeToInsert.getData().compareTo(node.getData()) < 0) {
            node.setLeftChild(insert(node.getLeftChild(), nodeToInsert));
            node.getLeftChild().setParent(node);
        }
        if(nodeToInsert.getData().compareTo(node.getData()) > 0) {
            node.setRightChild(insert(node.getRightChild(), nodeToInsert));
            node.getRightChild().setParent(node);
        }
        return node;
    }

    @Override
    public void delete(T data) { root = delete(data, root); }

    private Node<T> delete(T data, Node<T> node) {
        if(node == null) {
            return null;
        }
        //traverse the LHS of the tree
        if(data.compareTo(node.getData()) < 1) {
            node.setLeftChild(delete(data, node.getLeftChild()));
        }
        //traverse the RHS of the tree
        else if(data.compareTo(node.getData()) > 1) {
            node.setRightChild(delete(data, node.getRightChild()));
        } else {
            //when a match is found
            if(node.getLeftChild() == null) {
                return node.getRightChild();
            } else if(node.getRightChild() == null) {
                return node.getLeftChild();
            }
            node.setData(getMax(node.getLeftChild()));
            node.setLeftChild(delete(node.getData(), node.getLeftChild()));
        }
        recolorAndRotate(node);
        updateHeight(node);
        return node;
    }

    private void recolorAndRotate(Node<T> node) {
        Node<T> parent = node.getParent();
        if(node != root && parent.getColor() == Color.RED) {
            Node<T> grandParent = node.getGrandParent();
            Node<T> uncle = parent.isLeftChild() ? grandParent.getRightChild() : grandParent.getLeftChild();
            if(uncle != null && uncle.getColor() == Color.red) {
                handleRecoloring(parent, uncle, grandParent);
            }
            else if(parent.isLeftChild()) {
                handleLeftSituations(node, parent, grandParent);
            }
            else if(!parent.isLeftChild()) {
                handleRightSituations(node, parent, grandParent);
            }
        }
        root.setColor(Color.BLACK);
    }

    private void handleRecoloring(Node<T> parent, Node<T> uncle, Node<T> grandParent) {
        uncle.flipColor();
        parent.flipColor();
        grandParent.flipColor();
        recolorAndRotate(grandParent);
    }

    private void handleLeftSituations(Node<T> node, Node<T> parent, Node<T> grantParent) {
        if(!node.isLeftChild()) {
            rotateLeft(parent);
        }
        parent.flipColor();
        grantParent.flipColor();
        rotateRight(grantParent);
        recolorAndRotate(parent);
    }

    private void handleRightSituations(Node<T> node, Node<T> parent, Node<T> grandParent) {
        if(!node.isRightChild()) {
            rotateRight(parent);
        }
        parent.flipColor();
        grandParent.flipColor();
        rotateLeft(grandParent);
        recolorAndRotate(parent);
    }

    //returns the height of the tree
    private int height(Node<T> node) {
        return node != null ? node.getHeight() : 0;
    }

    private void updateHeight(Node<T> node) {
        int maxHeight = Math.max(height(node.getLeftChild()), height(node.getRightChild()));
        node.setHeight(maxHeight + 1);
    }

    //rebalances the tree in right-heavy situations
    private void rotateLeft(Node<T> node) {
        Node<T> rightNode = node.getRightChild();
        //left child of the right node becomes the right child of the parent node
        node.setRightChild(rightNode.getLeftChild());
        //to avoid null pointer exceptions
        if(node.getRightChild() != null) {
            node.getRightChild().setParent(node);
        }
        //right node becomes the new parent
        rightNode.setLeftChild(node);
        //update the right node's parent
        rightNode.setParent(node.getParent());
        //update the right node's parent to point to its new child
        updateChildrenOfParentNode(node, rightNode);
        //update the original parent node to point to the right node
        node.setParent(rightNode);
    }

    //rebalances the tree in left-heavy situations
    private void rotateRight(Node<T> node) {
        Node<T> leftNode = node.getLeftChild();
        node.setLeftChild(leftNode.getRightChild());
        if(node.getLeftChild() != null) {
            node.getLeftChild().setParent(node);
        }
        leftNode.setRightChild(node);
        leftNode.setParent(node.getParent());
        updateChildrenOfParentNode(node, leftNode);
        node.setParent(leftNode);
    }

    //updates the child pointers of a given parent node
    private void updateChildrenOfParentNode(Node<T> node, Node<T> tempNode) {
        if(node.getParent() == null) {
            root = tempNode;
        } else if(node.isLeftChild()) {
            node.getParent().setLeftChild(tempNode);
        } else {
            node.getParent().setRightChild(tempNode);
        }
    }
}
