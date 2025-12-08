package Model;

import lombok.*;
import java.awt.*;

/** Model.Node class with Lombok annotations. */
@Data
public class Node<T extends Comparable<T>> {

    @NonNull private T data;
    private int height = 1;
    private Color color;
    private Node<T> leftChild;
    private Node<T> rightChild;
    @ToString.Exclude private Node<T> parent;

    /// general utilities
    public Node<T> getGrandParent() {
        return parent != null ? parent.getParent() : null;
    }

    public boolean isLeftChild() {
        return this == parent.getLeftChild();
    }

    public boolean isRightChild() {
        return this == parent.getRightChild();
    }

    /// red-black tree utilities
    public void flipColor() {
        setColor(color == Color.RED ? Color.BLACK : Color.RED);
    }
}
