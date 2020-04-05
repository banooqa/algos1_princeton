/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int count;
    private Item[] s;

    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[1];
        count = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return count;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (count == s.length) {
            resize(2 * s.length);
        }
        s[count] = item;
        count++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (count == 0) {
            throw new NoSuchElementException();
        }
        int idx = StdRandom.uniform(count);
        Item randval = s[idx];
        s[idx] = s[count - 1];
        s[count - 1] = null;
        count--;
        if (count > 0 && count == s.length / 4) {
            resize(s.length / 2);
        }
        return randval;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];

        for (int i = 0; i < count; i++) {
            copy[i] = s[i];
        }
        s = copy;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int idx = StdRandom.uniform(count);
        return s[idx];

    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int current = 0;
        private int[] order;

        public RandomizedQueueIterator() {
            if (count > 0) {
                order = new int[count];
                for (int i = 0; i < count; i++)
                    order[i] = i;
                StdRandom.shuffle(order, 0, count - 1);
            }
        }

        public boolean hasNext() {
            return current < count;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return s[order[current++]];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> newR = new RandomizedQueue<>();
        System.out.println("Is empty? " + newR.isEmpty());
        newR.enqueue(2);
        newR.enqueue(3);
        newR.enqueue(1);
        newR.enqueue(4);
        newR.enqueue(5);
        newR.enqueue(6);
        System.out.println("Is empty? " + newR.isEmpty());
        for (int i = 0; i < 3; i++) {
            System.out.println(newR.dequeue());
            System.out.println("Size is " + newR.size());
        }
        for (int i = 0; i < 3; i++) {
            System.out.println(newR.dequeue());
            System.out.println("Size is " + newR.size());
        }
        System.out.println("Is empty? " + newR.isEmpty());
    }
}
