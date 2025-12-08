package Model;

/**
 * Full implementation of a binary search tree with Lombok annotations. Binary search trees have the property that:
 * All nodes to the LHS of their parent possess a value less than their parent, while all values to the RHS possess a
 * value which is greater than their parent. Search, insert, delete, and retrieval operations all occur in O(log H)
 * time, where H is the height of the binary search tree.
 */
public class BinarySearchTree<T extends Comparable<T>> implements Tree<T> {

    private Node<T> root;

    public boolean isEmpty() {
        return root == null;
    }

    public T getMin() {
        if(isEmpty()) {
            return null;
        }
        return getMin(root);
    }

    //protected helper method - traverses the tree recursively
    protected T getMin(Node<T> node) {
        if(node.getLeftChild() != null) {
            return getMin(node.getLeftChild());
        }
        return node.getData();
    }

    public T getMax() {
        if(isEmpty()) {
            return null;
        }
        return getMax(root);
    }

    protected T getMax(Node<T> node) {
        if(node.getRightChild() != null) {
            return getMax(node.getRightChild());
        }
        return node.getData();
    }

    public void traverse() {
        traverseInOrder(root);
    }

    private void traverseInOrder(Node<T> node) {
        if(node != null) {
            traverseInOrder(node.getLeftChild());
            System.out.println(node);
            traverseInOrder(node.getRightChild());
        }
    }

    public Tree<T> insert(T data) {
        if(isEmpty()) {
            root = new Node<>(data);
        } else {
            insert(data, root);
        }
        return this;
    }

    private void insert(T data, Node<T> node) {
        if(data.compareTo(node.getData()) < 0) {
            if(node.getLeftChild() == null) {
                Node<T> newNode = new Node<>(data);
                node.setLeftChild(newNode);
            } else {
                insert(data, node.getLeftChild());
            }
        } else if(data.compareTo(node.getData()) > 0) {
            if(node.getRightChild() == null) {
                Node<T> newNode = new Node<>(data);
                node.setRightChild(newNode);
            } else {
                insert(data, node.getRightChild());
            }
        }
    }

    public void delete(T data) {
        root = delete(data, root);
    }

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
        return node;
    }
}
