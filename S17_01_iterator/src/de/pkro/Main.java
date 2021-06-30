package de.pkro;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Main {

  public static void main(String[] args) {


    //  1
    // / \
    // 2   3

    // 213
    Node<Integer> root = new Node<>(1, new Node<>(2), new Node<>(3));

    // direct use of iterator and Node (c++ style)
    InOrderIterator<Integer> it = new InOrderIterator<>(root);

    while(it.hasNext()) {
      System.out.print(it.next()+", ");
    }
    System.out.println();

    // using foreach with iterable BinaryTree
    BinaryTree<Integer> tree = new BinaryTree<Integer>(root);
    for (int n : tree) {
      System.out.print(n + ", ");
    }
    System.out.println();
  }
}

class BinaryTree<T> implements Iterable<T> {

  private Node<T> root;

  public BinaryTree(Node<T> root) {
    this.root = root;
  }

  @Override
  public Iterator<T> iterator() {
    return new InOrderIterator<>(root);
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    for(T item: this) { // uses the iterator method above
      action.accept(item);
    }
  }

  /*@Override
  public Spliterator<T> spliterator() {
    return Iterable.super.spliterator();
  }*/
}

class Node<T> {
  public T value;
  public Node<T> left, right, parent;

  public Node(T value) {
    this.value = value;
  }

  public Node(T value, Node<T> left, Node<T> right) {
    this.value = value;
    this.left = left;
    this.right = right;

    left.parent = right.parent = this;
  }
}

class InOrderIterator<T> implements Iterator<T> {

  private Node<T> current, root;
  // binary tree doesn't start from root but from the leftmost element
  // (in our desired in-order traversal case)
  private boolean yieldedStart;

  public InOrderIterator(Node<T> root) {
    this.root = current = root;

    // go to leftmost element, where the iteration should start
    while (current.left != null) {
      current = current.left;
    }
  }

  private boolean hasRightmostParent(Node<T> node) {
    if (node.parent == null) {
      return false;
    }
    return (node == node.parent.left) || hasRightmostParent(node.parent);
  }

  @Override
  public boolean hasNext() {
    // why current.left? wouldn't this go in the wrong direction?
    return current.left != null || current.right != null || hasRightmostParent(current);
  }

  // complicated because not recursive implementation
  // because next() cant' suspend and continue (?)
  // In c# it would work
  @Override
  public T next()
  {
    if (!yieldedStart)
    {
      yieldedStart = true;
      return current.value;
    }

    if (current.right != null)
    {
      current = current.right;
      while (current.left != null)
        current = current.left;
      return current.value;
    }
    else
    {
      Node<T> p = current.parent;
      while (p != null && current == p.right)
      {
        current = p;
        p = p.parent;
      }
      current = p;
      return current.value;
    }
  }
}
