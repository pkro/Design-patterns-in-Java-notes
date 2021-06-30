package de.pkro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    Node<Character> node = new Node<>('a', new Node<>('b', new Node<>('c'), new Node<>('d')), new Node<>('e'));
    StringBuilder sb = new StringBuilder();
    Iterator<Node<Character>> it = node.preOrder();
    while (it.hasNext()) {
      sb.append(it.next().value);
    }

    System.out.println(sb.toString()); // "abcde"
  }
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

  private List<Node<T>> traverse() {
    List<Node<T>> s = new ArrayList<>();
    List<Node<T>> out = new ArrayList<>();
    s.add(this);
    while(!(s.size()==0)) {
      Node<T> node = s.remove(s.size()-1);
      out.add(node);
      if(node.right != null) {
        s.add(node.right);
      }
      if(node.left != null) {
        s.add(node.left);
      }

    }
    return out;
  }
  // https://en.wikipedia.org/wiki/Tree_traversal#Pre-order
  public Iterator<Node<T>> preOrder() {
    return traverse().iterator();
  }
}