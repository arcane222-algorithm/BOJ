package deque;

import java.io.*;
import java.util.StringTokenizer;


/**
 * AC - BOJ5430
 * -----------------
 * Input 1
 * 4
 * RDD
 * 4
 * [1,2,3,4]
 * DD
 * 1
 * [42]
 * RRD
 * 6
 * [1,1,2,3,5,8]
 * D
 * 0
 * []
 *
 * Output 1
 * [2,1]
 * error
 * [1,2,3,5,8]
 * error
 * -----------------
 */
public class BOJ5430 {

    private static class Deque<E> {
        private boolean isReversed;  // false: forward, true: backward

        /**
         * Inner class Node
         * @param <E>
         */
        private class Node<E> {
            Node<E> prev, next;
            E element;

            public Node(Node<E> prev, Node<E> next, E element) {
                this.prev = prev;
                this.next = next;
                this.element = element;
            }

            public void clear() {
                this.element = null;
                this.prev = null;
                this.next = null;
            }
        }

        private int size;
        private Node<E> head, tail;

        /**
         * Add new element to deque
         * @param element A new element to insert into the deque
         * @param isReversed
         */
        private void add(E element, boolean isReversed)  {
            Node<E> node = new Node<E>(null, null, element);
            if(isReversed) {
                if(head == null) {
                    head = tail = node;
                } else {
                    tail.next = node;
                    node.prev = tail;
                    tail = node;
                }
            } else {
                if(head == null) {
                    head = tail = node;
                } else {
                    head.prev = node;
                    node.next = head;
                    head = node;
                }
            }
            size++;
        }

        /**
         * Add new element to head
         * @param element A new element to insert into the deque
         */
        public void addToHead(E element) {
            add(element, this.isReversed);
        }

        /**
         * Add new element to tail
         * @param element A new element to insert into the deque
         */
        public void addToTail(E element) {
            add(element, !this.isReversed);
        }

        /**
         * Remove head element from deque
         * @param isReversed
         * @return Head element of deque
         */
        private E poll(boolean isReversed) {
            if(isReversed) {
                if(tail == null)
                    return null;

                final E element = tail.element;
                final Node<E> prev = tail.prev;
                tail.clear();
                tail = prev;

                if(tail == null) head = null;
                else tail.next = null;

                size--;
                return element;
            } else {
                if(head == null)
                    return null;

                final E element = head.element;
                final Node<E> next = head.next;
                head.clear();
                head = next;

                if(head == null) tail = null;
                else head.prev = null;
                size--;

                return element;
            }
        }

        /**
         * Remove head element from deque
         * @return Head element of deque
         */
        public E pollHead() {
            return poll(this.isReversed);
        }

        /**
         * Remove tail element from deque
         * @return Head element of deque
         */
        public E pollTail() {
            return poll(!this.isReversed);
        }

        /**
         * Get the value of deque without deleting it
         * @param isReversed
         * @return Head element of deque
         */
        private E peek(boolean isReversed) {
            if(isReversed) {
                if(tail == null) return null;
                else return tail.element;
            } else {
                if(head == null) return null;
                else return head.element;
            }
        }

        /**
         * Get the value of the head without deleting it
         * @return Head element of deque
         */
        public E peekHead() {
            return peek(this.isReversed);
        }

        /**
         * Get the value of the tail without deleting it
         * @return Tail element of deque
         */
        public E peekTail() {
            return peek(!this.isReversed);
        }

        /**
         * Get the index value of element
         * @return Index value of element (If the element does not exist in the deque, return -1)
         */
        private int getIndex(E element, boolean isReversed)  {
            int idx = 0;
            Node<E> node = isReversed ? tail : head;

            while(node != null) {
                if(element == null) {
                    if(node.element == null) {
                        return idx;
                    }
                } else {
                    if(node.element.equals(element)) {
                        return idx;
                    }
                }
                node = isReversed ? node.prev : node.next;
                idx++;
            }

            return -1;
        }

        /**
         * Get the index value of element
         * @return Index value of element (If the element does not exist in the deque, return -1)
         */
        public int indexOf(E element) {
            return getIndex(element, this.isReversed);
        }

        /**
         * Get the index value of element
         * @return Index value of element (If the element does not exist in the deque, return -1)
         */
        public int lastIndexOf(E element) {
            return getIndex(element, !this.isReversed);
        }

        /**
         * reverse the deque (slow method, O(N))
         */
        @Deprecated
        private void reverseOld() {
            if(head != null) {
                Node<E> temp, node;
                temp = head;
                node = head = tail;
                tail = temp;

                while(node != null) {
                    // swap prev & tail node
                    temp = node.prev;
                    node.prev = node.next;
                    node.next = temp;
                    node = node.next;
                }
            }
        }

        /**
         * reverse the deque direction
         */
        public void reverse() {
            isReversed = !isReversed;
        }


        /**
         * If size of deque is 0, return {@code true}. Else return {@code false}.
         * @return If size of deque is 0, return {@code true}. Else return {@code false}.
         */
        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * Return size of deque
         * @return Size of deque
         */
        public int size() {
            return size;
        }

        /**
         * Remove all elements
         */
        public void removeAll() {
            Node<E> node = head;
            while(node != null) {
                Node<E> next = node.next;
                node.clear();
                node = next;
            }
            head = tail = null;
            size = 0;
        }

        @Override
        public String toString() {
            Node<E> node;
            StringBuilder sb = new StringBuilder();

            if(isReversed) {
                node = tail;
                sb.append('[');
                while(node != null) {
                    sb.append(node.element);
                    node = node.prev;
                    if(node != null) sb.append(',');
                }
                sb.append(']');
            } else {
                node = head;
                sb.append('[');
                while(node != null) {
                    sb.append(node.element);
                    node = node.next;
                    if(node != null) sb.append(',');
                }
                sb.append(']');
            }

            return sb.toString();
        }
    }
    static int T;
    static Deque<Integer> deque = new Deque<>();
    static StringBuilder result = new StringBuilder();

    public static String processInst(String inst) {
        for(int i = 0; i < inst.length(); i++) {
            switch(inst.charAt(i)) {
                case 'R':   // reverse
                    deque.reverse();
                    break;

                case 'D':   // discard
                    if(deque.isEmpty())
                        return "error";
                    else
                        deque.pollHead();
                    break;
            }
        }

        return deque.toString();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(System.out));
        StringTokenizer st;

        T = Integer.parseInt(br.readLine());
        for(int i = 0; i < T; i++) {
            String inst = br.readLine();
            int n = Integer.parseInt(br.readLine());
            st = new StringTokenizer(br.readLine(), "[,]");
            while(st.hasMoreTokens()) {
                deque.addToTail(Integer.parseInt(st.nextToken()));
            }
            result.append(processInst(inst));
            result.append('\n');
            deque.removeAll();
        }

        // write the result & close the i/o stream
        bw.write(result.toString());
        br.close();
        bw.close();
    }
}

