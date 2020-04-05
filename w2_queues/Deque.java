/* ************************************************************************
 *  Name:
 *  Date:
 *  Description:
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int count = 0;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;

    }

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (count == 0);
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        else {
            if (isEmpty()) {
                first = new Node();
                last = first;
                first.item = item;
            }
            else {
                Node oldfirst = first;
                first = new Node();
                first.item = item;
                first.next = oldfirst;
            }
            count++;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        else {
            if (isEmpty()) {
                first = new Node();
                last = first;
                first.item = item;
            }
            else {
                Node newlast = new Node();
                newlast.item = item;
                newlast.prev = last;
                last.next = newlast;
                last = newlast;
            }
            count++;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = first.item;
        first = first.next;
        count--;
        if (!isEmpty()) {
            first.prev = null;
        }
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = last.item;
        last = last.prev;
        count--;
        if (!isEmpty()) {
            last.next = null;
        }

        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            Item item = current.item;
            if (item == null)
                throw new NoSuchElementException();

            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();

        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> newD = new Deque<Integer>();
        System.out.println("Is empty? " + newD.isEmpty());
        newD.addFirst(2);
        newD.addLast(3);
        newD.addFirst(1);
        newD.addLast(4);
        newD.addLast(5);
        newD.addFirst(0);
        System.out.println("Is empty? " + newD.isEmpty());
        for (int i = 0; i < 3; i++) {
            System.out.println(newD.removeFirst());
            System.out.println("Size is " + newD.size());
        }
        for (int i = 0; i < 3; i++) {
            System.out.println(newD.removeLast());
            System.out.println("Size is " + newD.size());
        }
        System.out.println("Is empty? " + newD.isEmpty());

    }
}

