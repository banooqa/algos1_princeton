/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    private static RandomizedQueue<String> que;
    private static int k;

    public static void main(String[] args) {
        que = new RandomizedQueue<String>();
        k = Integer.parseInt(args[0]);

        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            que.enqueue(word);
        }

        for (int i = 0; i < k; i++) {
            System.out.println(que.dequeue());
        }

    }
}
