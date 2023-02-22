import java.lang.reflect.Field;
import java.util.*;

public class DayTwo {
    //Agenda: collections in java

    /*
    * arrayList
    *   random access:
    *       1 million data in arraylist, we can to get 10th element and 999th element
    *   modcount: the number of times this list has been structurally modified
    *       if mod count is not the same, it will throws modification exception
    *
    *   add method
    *       case 1: when we add first element into list, and the capacity is 10
    *
                private static int calculateCapacity(Object[] elementData, int minCapacity) {
                    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
                        return Math.max(DEFAULT_CAPACITY, minCapacity);// if you add first element, you need go to here
                    }
                    return minCapacity;// if you add second, 3rd... you need go to here
                }
    *   grow method
            *  if (minCapacity - elementData.length > 0) when you add the 10th element into the list, grow() mehtod should be called
            *
                    grow(minCapacity);
            }
       private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);// newCap = oldcap + 0.5 * oldcap = 1.5 oldcap
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity; // current cap > integer.max
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);// size = new cap, new element should be added to the list
    }
    *
    *   time complexity
    *   add(O object) O(1), wrose case O(N): when a new arraylist  has to be created and all element copied to new arraylist
    *   add(int index, E element): O(N)
    *   remove(): O(1)
    *       get() O(1) always
    *
    * vector and stack
    *   this is list: public class ArrayList<E> extends AbstractList<E>
                implements List<E>, RandomAccess, Cloneable, java.io.Serializable
    *   This is vector: public class Vector<E>
            extends AbstractList<E>
            implements List<E>, RandomAccess, Cloneable, java.io.Serializable
    *   grow method
        *    private void grow(int minCapacity) {
            // overflow-conscious code
            int oldCapacity = elementData.length;
            int newCapacity = oldCapacity + ((capacityIncrement > 0) ? when you donot assgin your own cap increment,
            *                                                           it just increases by 2 times
                                             capacityIncrement : oldCapacity);
            if (newCapacity - minCapacity < 0)
                newCapacity = minCapacity;
            if (newCapacity - MAX_ARRAY_SIZE > 0)
                newCapacity = hugeCapacity(minCapacity);
            elementData = Arrays.copyOf(elementData, newCapacity);
        }
    *   time complexity
    *   vector and stack:
    *   add() O(1) the best case
    *   remove O(1)
    *   size() O(1)
    * queue + linked list
    *   public class LinkedList<E>
        extends AbstractSequentialList<E>
        implements List<E>, Deque<E>, Cloneable, java.io.Serializable
        * it does not implement random access, which means can not get an element by index
    *   add method
    *  public boolean add(E e) {
        linkLast(e);// call linkLast(e)
        return true;
    }
    *
    * void linkLast(E e) {
        final Node<E> l = last;// old last
        final Node<E> newNode = new Node<>(l, e, null);// l is previous element, the current element is e, null is last element
            [old last, l]<-[new element]->null
        * last = newNode;// update new last
        if (l == null)// when we have the empty linked list, l == null is true
            first = newNode;// first is the last node: our linked list: [new node]->null
        else
            l.next = newNode;//if we have something in out linked list, we need go to here
        size++;
        modCount++;
    }
        TC:
        *   add() O(1)
        *   add(index) O(N) worst case, the best case is O(1)
                    * Node<E> node(int index) {
                    // assert isElementIndex(index);

                    if (index < (size >> 1)) {
                        Node<E> x = first;
                        for (int i = 0; i < index; i++)
                            x = x.next;
                        return x;
                    } else {
                        Node<E> x = last;
                        for (int i = size - 1; i > index; i--)
                            x = x.prev;
                        return x;
                    }
                }
        *
        *   get() O(N)
        * remvoe() O(N)
        *
        *
    *
    * deque
    *   deque: it is double ended queue, which means we can use deque as stack(FILO) and queue(FIFO)
    *   add(){
    *       offer();
    *   }
    * offer(){
    *   implement something here
    * }
    * priority queue: java using min heap by default,
    *   you are given parent index
    *   left child index = parent index * 2 + 1
    *   right child index = parent index * 2 + 2
    *  if we donot given parent index
    *   parent index = (index -1) / 2
    *       [3,5,10,7,9,15,11,13,20,12] 15 is at the index 5,
    *       parent index = (5 -1) /2 = 2 10 is at the index 2
    *                       3
    *                       /\
    *                       5 10
    *                      /\  /\
    *                   7   9  15 11
    *                  /\   /\
    *                13 20 12
    *       siftup()
    *       private void siftUpComparable(int k, E x) {
                Comparable<? super E> key = (Comparable<? super E>) x;
                while (k > 0) {
                    int parent = (k - 1) >>> 1; //parent = (index - 1) / 2
                    Object e = queue[parent]; // this is parent node
                    if (key.compareTo((E) e) >= 0) // to find X where x should >= queue[parent]
                        break;
                    queue[k] = e;// put x at the index k
                    k = parent;
                }
                queue[k] = key;
    }//
    *   key = something
    *   key1 = something else  => hash(key1 : 1) = hash(key : 12) = 0
    * [key][key1][2][3][][][][][][][] ->
    *  |
    *   1
    *  |
    * 2
    *   |
    * 3
    * ...
    * the len of linked list greater than and equal
    * hashmap
    *   how to compute hashing in hashmap
    *     static final int hash(Object key) {
            int h;
            return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        }
        *  h = key.hashCode() = 1111 1111 1111 1111 1111 0000 1110 1010 = h
        *  h >>> 16             0000 0000 0000 0000 1111 1111 1111 1111
    * hashcode ^ (h >>> 16)     1111 1111 1111 1111 0000 1111 0001 0101
    * (n - 1) & hash            0000 0000 0000 0000 0000 1111 0001 0101
    *   0101 = 5 = 2^3 * 0 + 2^2 * 1 + 2^1 * 0 + 2^0 * 1 = 5
    * where n is table length
    *   resize()
    *
            * final Node<K,V>[] resize() {
                Node<K,V>[] oldTab = table;
                // recording the current capacity,
                int oldCap = (oldTab == null) ? 0 : oldTab.length;
                * record the # of elements map allows to store
                int oldThr = threshold; // capacity * 0.75
                int newCap, newThr = 0;
                if (oldCap > 0) { // not 0, we have element(s) in the table
                    if (oldCap >= MAXIMUM_CAPACITY) {
                        threshold = Integer.MAX_VALUE;// check the overflow
                        return oldTab; // to determine whether the current capacity has exceeded the max allowed capacity
                        * // max allowed capacity Integer.MAX_VALUE - 8
                    }
                    else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                             oldCap >= DEFAULT_INITIAL_CAPACITY)
                        newThr = oldThr << 1; // double threshold
                }
                else if (oldThr > 0) // initial capacity was placed in threshold // if you donot have any elements in the hashmap,
                    newCap = oldThr;
                else {               // zero initial threshold signifies using defaults
                    newCap = DEFAULT_INITIAL_CAPACITY;
                    newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
                }
                if (newThr == 0) {
                    float ft = (float)newCap * loadFactor;
                    newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                              (int)ft : Integer.MAX_VALUE);
                }
                threshold = newThr;
                @SuppressWarnings({"rawtypes","unchecked"})
                Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
                table = newTab;
                if (oldTab != null) {
                    for (int j = 0; j < oldCap; ++j) {
                        Node<K,V> e;
                        if ((e = oldTab[j]) != null) {
                            oldTab[j] = null;
                            if (e.next == null)
                                newTab[e.hash & (newCap - 1)] = e;
                            else if (e instanceof TreeNode)
                                ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                            else { // preserve order
                                Node<K,V> loHead = null, loTail = null;
                                Node<K,V> hiHead = null, hiTail = null;
                                Node<K,V> next;
                                do {
                                    next = e.next;
                                    if ((e.hash & oldCap) == 0) {
                                        if (loTail == null)
                                            loHead = e;
                                        else
                                            loTail.next = e;
                                        loTail = e;
                                    }
                                    else {
                                        if (hiTail == null)
                                            hiHead = e;
                                        else
                                            hiTail.next = e;
                                        hiTail = e;
                                    }
                                } while ((e = next) != null);
                                if (loTail != null) {
                                    loTail.next = null;
                                    newTab[j] = loHead;
                                }
                                if (hiTail != null) {
                                    hiTail.next = null;
                                    newTab[j + oldCap] = hiHead;
                                }
                            }
                        }
                    }
                }
                return newTab;
            }
    *   when hashmap starts to transfer from linked list to red-black tree?
    *   the len of array is greater than and equal to 64, and the linked list is 8
    * when hashmap starts to transfer red-black  tree to linked list
    *
    *   the size of tree is less than 6
    *
    *the reason we need transfer linked list to red-black tree is that we want to reduce the Time complexity
    *   from O(N) to O(log n)
    *
    *   put()
    *   1 : calculating hash value and find the index where we should put
    *   2: if there is null, then we just put it at the index
    *   3: if there exits hash collision, hashmap will do the following steps:
    *      a): if hashmap is using red-black tree, then call red-black tree to insert it
    *       b) if hashmap does not use red-black tree, then put the value. after this, hashmap will check
    *           the len of linked list, if the hashmap meets the requirements, then hashmap will transfer to
    *           red-black tree
    *           requirements: the len of array is greater than and equal to 64, and the linked list is 8
    *  4: if there exits duplicate key, then replace the value
    *   5: check the size, if the size is greater than threshold , then resizing
    *
    * hashset: there is not duplicate value in the hashset
    *
    * */

    public static void main(String[] args) throws Exception {
//        List<Integer> list = new ArrayList<>();
//        System.out.println(getCapacity(list));
//        list.add(1);
//        System.out.println("add 1 to list, and capacity is: " + getCapacity(list));

        Deque<String> deque = new LinkedList<>();
        deque.offer("apple");
        deque.offer("banana");
        deque.offerFirst("orange");
        deque.offerLast("peach");
        System.out.println(deque);
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

//        String firstElement = deque.removeFirst();
//        String lastElement = deque.removeLast();
//        System.out.println(deque);

        Iterator<String> it = deque.iterator();
        while(it.hasNext()){
            String element = it.next();
            System.out.println(element);
        }
        // stack  by using deque
         // public void push(int x)
        // public int pop()
        //public int top()
        // public boolean empty()
    }
    static int getCapacity(List al) throws Exception{//checked exception
        Field field = ArrayList.class.getDeclaredField("elementData");
        field.setAccessible(true);
        return ((Object[]) field.get(al)).length;
    }

}
