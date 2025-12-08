package Model;

/**
 * Full implementation of a splay tree. Splay trees are a variant of binary search trees that implement an update
 * method called "splaying". Splaying a node is analogous to a move-to-front operation inside a linked list. Splayed
 * nodes are rotated up the tree in such a way that the most frequently accessed items are ordered at the top. This is
 * effective for operations which require quick access to frequently queried items.
 */
public class SplaySearchTree<T extends Comparable<T>> extends BinarySearchTree<T> {

    private Node<T> root;

    @Override
    public void delete(T data) {
        root = delete(data, root);
    }

    private Node<T> delete(T data, Node<T> node) {
        if(node == null) {
            return null;
        }
        if(data.compareTo(node.getData()) < 0) {
            //continue probing the LHS of the tree
            node.setLeftChild(delete(data, node.getLeftChild()));
            node.getLeftChild().setParent(node);
        } else if(data.compareTo(node.getData()) > 0) {
            //continue probing the RHS of the tree
            node.setRightChild(delete(data, node.getRightChild()));
            node.getRightChild().setParent(node);
        } else {
            //node has one child or leaf node (no children)
            if(node.getLeftChild() == null) {
                return node.getRightChild();
            } else if(node.getRightChild() == null) {
                return node.getLeftChild();
            }
            //node has two children
            node.setData(getMax(node.getLeftChild()));
            //delete the left child after transferring its data to the parent
            node.setLeftChild(delete(node.getData(), node.getLeftChild()));
            //update the new child's pointer
            node.getLeftChild().setParent(node);
        }
        return node;
    }

    public Node<T> find(T data) {
        return find(root, data);
    }

    //private helper method - recursively traverses the tree
    private Node<T> find(Node<T> node, T data) {
        if(node != null) {
            if(node.getData().compareTo(data) == 0) {
                splay(node);
                return node;
            }
            Node<T> nextNode = data.compareTo(node.getData()) > 0 ? node.getRightChild() : node.getLeftChild();
            find(nextNode, data);
        }
        return null;
    }

    //"splays" a node until it has reached the top of the tree
    private void splay(Node<T> node) {
        while(node != null) {
            Node<T> parent = node.getParent();
            if(node.getGrandParent() == null) {
                if(node.isLeftChild())
                    rotateRight(parent);
                else
                    rotateLeft(parent);
            }
            else if(node.isLeftChild() && parent.isLeftChild()) {
                rotateRight(node.getGrandParent());
                rotateRight(parent);
            }
            else if(node.isRightChild() && parent.isRightChild()) {
                rotateLeft(node.getGrandParent());
                rotateLeft(parent);
            }
            else if(node.isRightChild() && parent.isLeftChild()) {
                rotateRight(parent);
                rotateLeft(parent);
            }
            else if(node.isLeftChild() && parent.isRightChild()) {
                rotateLeft(parent);
                rotateRight(parent);
            }
         }
    }

    /** Rebalances the binary tree in right-heavy situations - ie. where the height of a given RHS subtree exceeds its
     * LHS by more than one. **/
    private void rotateLeft(Node<T> node) {
        Node<T> rightNode = node.getRightChild();
        //left child of the right node becomes the right child of the parent node
        node.setRightChild(rightNode.getLeftChild());
        //update pointer of new right child - to avoid null pointer exceptions
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

    /** Rebalances the binary tree in left-heavy situations - ie. where the height of a given LHS subtree exceeds its
     * RHS by more than one. **/
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
