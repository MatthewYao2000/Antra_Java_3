import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Agenda:
 *
 * what is cas?
 *  atomic operation： is an unit of work/Java code always execute together or none of them executes
 *   atomic operation start here:
     *   code1
     *   code2
     *   code3 -> failed
     *   ..
     *   codeN
 *   END here
 *   CAS: compare and set/swap
 *      V: the value you want to change, it is in the memory
 *      E: expected value: it is in the local memory
 *      N: the new value you want to update.
 *      int i = 1;
 *      thread A wants to change i to 2
 *      thread A --> [int i = 1: in the main memory ; V]
 *      E = i = 1 in the thread A's memory
 *      after the business logic
 *      i = 2 -> thread A compares V and E, E = 1 and V = 1 -> i = 2 in main memory
 *      Thread B = update i = 3 during thread A business logic
 *      V =3, E = 1. i = 2
 *
 *      ABA problem:
 *      supposes there are two threads want to change the same value
 *      ------------time line------------------------------------
 *      thread A --> V = 1, E = 1--------------------------------------> business logic here-----------thread A changes v from 1 to 2->>>
 *      thread B --> V = 1, E = 1-->V from 1 to 2, change change back: 2 = 1-thread B finish here--->
 * thread safe collections
 *
 * ##concurrent hashMap
 * comparing concurrent hashMap in java 1.7 and java  1.8
 * in java 7: concurrent hashmap uses array + linked list
 * in java 8: it uses node array + linked list / red-black tree
 *  get():
 *      public V get(Object key) {
 *         Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
 *         int h = spread(key.hashCode());// find the hash value
 *         if ((tab = table) != null && (n = tab.length) > 0 &&
 *             (e = tabAt(tab, (n - 1) & h)) != null) {
 *             if ((eh = e.hash) == h) {
 *                 if ((ek = e.key) == key || (ek != null && key.equals(ek)))
 *                     return e.val; // if the first node in the linked list is what we are looking for
 *                     // return the first node's value here
 *             }
 *             else if (eh < 0)
 *                 return (p = e.find(h, key)) != null ? p.val : null;
 *             while ((e = e.next) != null) {// if the first node is not what we are looking for
 *             // find the next
 *                 if (e.hash == h &&
 *                     ((ek = e.key) == key || (ek != null && key.equals(ek))))
 *                     return e.val;
 *             }
 *         }
 *         return null;
 *     }
 *
 *
 *  putVal():
 *      synchronized
 *      |
 *      V
 *  [node1][][][][][] -> node array
 *      |
 *      V
 *      linked list /red-black
//final V putVal(K key, V value, boolean onlyIfAbsent) {
        // step 0: check key == null, which means concurrent hashmap does not allow you have key == null or value == null
//        if (key == null || value == null) throw new NullPointerException();
            step1: computes hash value
//        int hash = spread(key.hashCode());
//        int binCount = 0;
//        for (Node<K,V>[] tab = table;;) {
//        Node<K,V> f; int n, i, fh; // f is targeted Node
//        if (tab == null || (n = tab.length) == 0)// step 2: initail table if the table is null
//        tab = initTable();
//        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {// step 3: if the node is null, then add node to the hashmap
//        if (casTabAt(tab, i, null,
//        new Node<K,V>(hash, key, value, null)))
//        break;                   // no lock when adding to empty bin
//        }
//        else if ((fh = f.hash) == MOVED)
//        tab = helpTransfer(tab, f);
//        else {
//        V oldVal = null;
//synchronized (f) { // step 4: add  synchronized to the node,
//        if (tabAt(tab, i) == f) {
//        if (fh >= 0) { // when fh >= 0, the structure is linked list
//        binCount = 1;
//        for (Node<K,V> e = f;; ++binCount) {
//        K ek;
//        if (e.hash == hash &&
//        ((ek = e.key) == key ||
//        (ek != null && key.equals(ek)))) {
//        oldVal = e.val;
//        if (!onlyIfAbsent)
//        e.val = value;
//        break;
//        }
//        Node<K,V> pred = e;
//        if ((e = e.next) == null) {
//        pred.next = new Node<K,V>(hash, key,
//        value, null);
//        break;
//        }
//        }
//        }
//        else if (f instanceof TreeBin) { // if fh < 0, it is a  red-black tree
//        Node<K,V> p;
//        binCount = 2;
//        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
//        value)) != null) {
//        oldVal = p.val;
//        if (!onlyIfAbsent)
//        p.val = value;
//        }
//        }
//        }
//        }
//        if (binCount != 0) {
//        if (binCount >= TREEIFY_THRESHOLD)
//        treeifyBin(tab, i);
//        if (oldVal != null)
//        return oldVal;
//        break;
//        }
//        }
//        }
//        addCount(1L, binCount);
//        return null;
//        }

 /**       *
 * ##blocking queue
 *      put():
 *      count = 0->1..10
 *      takeIndex = 0 -> 1 .. 9->  count 9 ->8 .. 0
 *      |
 *      V
 *      [A]  [B]  [C]  [D]  [E]  [..]  []  [O]  last index= 10
 *      ^                                   ^
 *      |                                   |
 *      putIndex = 0 ->1 -> 2 ->.. 9 -> 0, if you are trying to add 11th element, you have to wait util the queue has slot for you
 *      take()
 *      count = 3 - > 2
 *      takeIndex = 0 -> 1
 *      [A] [S] [D] [] [] []
 *      putIndex = 3
 *
 *               thread A ->                     ->thread Q
 * producers    thread B ->[ this is our queue]   -> thread F ->consumers
 *
 *              thread C ->                         -> thread R
 *
 * the drawbacks of synchronized keyword
 *  decreased the performance of your application
 *  deadlock
 *  hard to debugging
 *
 *
 * pessimistic lock vs optimistic locker
 *  pessimistic lock :
 *      it is a strategy  where a lock is obtained on a shared resource for the entire execution
 *      during this execution, the other thread does allow to get the shared resource, which means the thread hs to wait
 *      synchronized keyword is pessimistic lock
 *  optimistic  lock:
 *      it is also a strategy where a lock is obtained on a shared resource only when a execution/transaction is about to modify  resource(s)
 *      however, other thread allows to read the resource concurrently. but the other thread wants to change or modify the resource, they have to get the lock
 *      reentrant lock
 * reentrant lock
 *  it is a class that provides a mechanism for mutual exclusion and synchronization in a multi-threaded environment.
 *      mutual exclusion: only one thread can hold the lock at a time, preventing multiple threads from accessing the same code or data concurrently
 *
 * thread pool
 *  thread pool - it is pre-defined thread(s) that is available for perform a set of works
 *  thread -> new -> the new thread is created in the heap - > perform GC -> application will be slowed
 *
 *  why do we need it?
 *  fixedThreadpool = 3 alive
 *  fixed Thread pool = you will give the number of threads
 *  cached thread pool =  it is an unbounded thread pool that created new thread(s) as needed
 *                       this one has a impact on performance of your application
 *  single thread pool = allows only one thread alive
 *  fork join pool = it is used for dividing large task(s) into smaller sub-tasks that can be executed in parallel
 *                  large task A -> sub task B -> thread B
 *                                 _> sub task C -> thread C
 *                   fork join pool uses work - stealing algorithm
 *                                          -  it is used to hold some tasks assigned to that thread
 *                                          when this thread finishes it own task it may "steal" tasks form the another thread's queue
 *                                          we want to make sure that all cpu resources are being used as efficiently as possible
 *
 * executor vs executorService vs executors
 * executor: it is an interface, only has one function that takes one input: runnable -> executes runnable task asynchronously on a new thread
 * executorService: it is an interface -> allows you to control the lifecycle of the threads and the task(s) being executed
 * executors: it is a class -> provides methods for creating different type thread pool -> simplifies the process
 *                          -> of creating / managing thread pool by providing a set of pre-defined functions
 *
 *
 * */
public class Day6 {
    public static void main(String[] args) {

//        AtomicInteger atomicInteger = new AtomicInteger(3);//V =3
//        int expectedValue = 0; // E = 0
//        int newValue = 1; // N
//        System.out.println("the result is: " + atomicInteger.compareAndSet(expectedValue,newValue));
//        System.out.println(atomicInteger.get());

        // ABA demo
//
//        AtomicInteger atomicInteger = new AtomicInteger(3);
//        new Thread(()->{//thread B
//            atomicInteger.compareAndSet(3,4);
//             try{TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e){e.printStackTrace();}
//             atomicInteger.compareAndSet(4,3);
//        }).start();
//        new Thread(()->{//thread A
//             try{TimeUnit.MILLISECONDS.sleep(2000);} catch (InterruptedException e){e.printStackTrace();}
//
//            atomicInteger.compareAndSet(3,4);
//            System.out.println(atomicInteger.get()+ " thread a");
//        }).start();




    }
    // producer - consumer model
    // our task: producer put elements in the queue. when the queue is full, consumer starts
    // to take elements from the queue
}


class ProContest{
    private BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);
    public void producer() throws InterruptedException{
        int value = 0;
        while(true){
            System.out.println("the value is: " + value +" in the producer method");
            blockingQueue.put(value++);
        }
    }
    public void consumer() throws  InterruptedException{
        while (true){
            if(blockingQueue.size() == 10){
                int value = blockingQueue.take();
                System.out.println("the value is: " + value + "in the consumer method");
                Thread.sleep(1000);
            }
        }
    }
    public static void main(String[] args) {
        ProContest proContest = new ProContest();
        Thread producerThread = new Thread(()->{
            try{
                proContest.producer();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });
        Thread consumerThread = new Thread(()->{
            try{
                proContest.consumer();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });
        producerThread.start();
        consumerThread.start();

    }
}