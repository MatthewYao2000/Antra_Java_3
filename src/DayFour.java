import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * volatile:
 *  visibility
 *
 *  JMM: java memory model
 *
 *  that manages the communications between two or more threads
 *
 * 2 steps:
 *
 * 1): thread A flushes the updated shared variable a from local memory into main memory
 *
 * 2): thread B will go to the main memory, and read it into local memory, bc thread B knows integer A is updated by thread A
 *
 *  prevent to reordering
 *
 *
 *  synchronized
 *  #####################synchronized###################################
 *      scenario 1:
 *      we assume that we have multiple synchronized methods in one Object,
 *      As long as one thread call one synchronized method at certain time.
 *      other thread should wait the current thread to finish it.
 *      current object: this
 *      synchronized keyword will lock the current object: this
 *
 *
                 class Internet{
                 public synchronized void sendMessageWithEmail(){
                 System.out.println("sending a message with email application");
                 System.out.println("done! sendMessageWithEmail()");
                 }
                 public synchronized void sendMessageWithWechat(){
                 System.out.println("sending a message with Wechat application");
                 System.out.println("done! sendMessageWithWechat()");
                 }

                 public static void main(String[] args) {
                 Internet user1 = new Internet();// its own threadA
                 Internet user2 = new Internet();// Thread B
                 new Thread(() ->{
                 user1.sendMessageWithEmail();
                 },"thread A ").start();
                 new Thread(() -> {
                 user1.sendMessageWithWechat();
                 },"thread B").start();

                 }

                 }

 ##############################################################
  SCENARIO 2
  Assume we are trying to call synchronized method and non-synchronized method by using the SAME
 object, there is not resource competition happens
 we are trying to call synchronized method and non-synchronized method by using the TWO DIFFERNET
 object, there is not resource competition happens

 lass Internet{
 public synchronized void sendMessageWithEmail(){
 try{
 TimeUnit.SECONDS.sleep(3);
 } catch ( InterruptedException e){
 e.printStackTrace();
 }
 System.out.println("sending a message with email application");
 System.out.println("done! sendMessageWithEmail()");
 }
 public synchronized void sendMessageWithWechat(){
 System.out.println("sending a message with Wechat application");
 System.out.println("done! sendMessageWithWechat()");
 }

 public void myInternetName(){ // we do not have synchronized keyword here
 System.out.println("This is myInternetName()...");
 }

 public static void main(String[] args) {
 Internet user1 = new Internet();// its own threadA
 Internet user2 = new Internet();// Thread B
 new Thread(() ->{
 user1.sendMessageWithEmail();
 },"thread A ").start();
 //        new Thread(() -> {
 //            user1.myInternetName(); test this first
 //        }
 new Thread(() -> {
 user2.myInternetName();
 },"thread B").start();

 }

 }
 *######################static + sync########################
 * assume we are using static keyword for two methods, no matter what objects you are using,
 * you need to wait Email() to finish first. Bc static + scyn will lock class itself
 * for non - static, your locker will lock the current object,
 *
 class Internet{
 public static synchronized void sendMessageWithEmail(){
 try{
 TimeUnit.SECONDS.sleep(3);
 } catch ( InterruptedException e){
 e.printStackTrace();
 }
 System.out.println("sending a message with email application");
 System.out.println("done! sendMessageWithEmail()");
 }
 public  static synchronized void sendMessageWithWechat(){
 System.out.println("sending a message with Wechat application");
 System.out.println("done! sendMessageWithWechat()");
 }

 public void myInternetName(){ // we do not have synchronized keyword here
 System.out.println("This is myInternetName()...");
 }

 public static void main(String[] args) {
 Internet user1 = new Internet();// its own threadA
 Internet user2 = new Internet();// Thread B
 new Thread(() ->{
 user1.sendMessageWithEmail();
 },"thread A ").start();
 //        new Thread(() -> {
 //            user1.myInternetName(); test this first
 //        }
 new Thread(() -> {
 user1.sendMessageWithWechat();
 },"thread B").start();

 }

 }
 * question: when should we use static method
 * 1): when there are some code can be shared by all instance, you can use it
 * 2): when you sure about the method cannot be changed or overridden, you can use it
 * 3): when the method is not using any instance variable.
 *
 *
 *
 *##########################java 8 new features####################################
 * what is functional interface
 *
 * what is lambda expression
 *  it can make your code more readable and create an anonymous function
 *  reduce time to code
 *
 // create a thread by using lambda expression
 // before java 8, you create a thread like this
 //        new Thread(new Runnable() {
 //            @Override
 //            public void run() {
 //                System.out.println("my thread");
 //            }
 //        });
 //        //after java 8
 //        new Thread(()-> System.out.println("my thread is created by using lambda"));
 //      using lambda expression and callable to create a thread

 Callable<String> callable =  ()->{
 String myCallableName = "my name";
 return myCallableName;
 };
 FutureTask<String> futureTask = new FutureTask<>(callable);
 Thread thread = new Thread(futureTask);
 thread.start();
 System.out.println(futureTask.get());
 // to create max heap by using lambda expression
  in java, prorityQueue is minheap by default
 in c/C++ it is max heap
 PriorityQueue<Integer> pq = new PriorityQueue<>((a,b) -> b -a);
 // to create a min heap
 PriorityQueue<Integer> minHeap = new PriorityQueue<>((a,b)->a-b);
 // sorting function: collections.sort: low to high
 List<Integer> list = Arrays.asList(1,2,3,4,5,6);
 Collections.sort(list, (s1, s2) -> Integer.compare(s2, s1));
 System.out.println(list);
 //        Calculator calculator =  (a, b) -> a + b;
 *  demonstrate some cases
 *
 *  what is stream api
 *      stream api + lambda expresion
 *      Intermediate functions:
 *          you can use intermediate functions to transform the stream into another stream
 *          you donot produce result until you call ternimal functions
 *
 *          filter(): selects only the element that match a given condition
 *          map(): transform the elements of the stream into a new type
 *          flatmap(): flatten a collection
 *          distinct(): set collections,  there should not be duplicated elements
 *          limit():truncates the stream to  a specified size: limit(1), limit(100)
 *          ...
 *
 *      Terminal functions: you can produce the result
 *          forEech():performs an action on each element of the stream
 *          reduce(): aggregates the elements in the stream into a single result
 *          collect: collects elements into a collections
 *          min()/max(): find the max or min
 *          count(): returns the # of element
 *          anyMatch()/allMatch()...: returns a boolean indicating whether any/all of the elements match a given condition
 *
 *
 * what is optional: avoid null pointer exception
 *
 * what is method reference
 *
 * what is CompletableFuture
 * */
public class DayFour {
    // can you create a functional interface that calculate a + b; where a and b are integer
    public static void main(String[] args) {
        // find string contains letter c and sort it
        List<String> list = Arrays.asList("a1","a2","b2","b1","c1","c2","c4","c3");
//            list.stream()
//                    .filter(s ->s.startsWith("c"))
//                    .sorted()
//                    .forEach(System.out::println);
//            List<String> myNewList = list.stream().filter(s->s.startsWith("c")).collect(Collectors.toList());
//        System.out.println(myNewList);

        // find a string that contains lower case
//        List<String> case2 = Arrays.asList("THIS", "is","MY","List");
//        case2.stream().filter(str -> str.chars().anyMatch(Character::isLowerCase))
//                .forEach(System.out::println);
//        // find a string that contains an integer and lower case
        //you can use two filters, for one filter you need to do: str -> str.matches(".*\\d.*")
        // just print out your find
        List<String> myList = Arrays.asList("ABC111","Adc123","dfg","222","aws444");
        List<String> res = myList.stream()
                .filter(str -> str.chars().anyMatch(Character::isDigit))
                .filter(str -> str.chars().anyMatch(Character::isLowerCase))
                .collect(Collectors.toList());
        System.out.println(res);
    }
}
@FunctionalInterface
interface Calculator{
   int cal(int a, int b);
}

class Internet{
    public static synchronized void sendMessageWithEmail(){
        try{
            TimeUnit.SECONDS.sleep(3);
        } catch ( InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("sending a message with email application");
        System.out.println("done! sendMessageWithEmail()");
    }
    public   synchronized void sendMessageWithWechat(){
        System.out.println("sending a message with Wechat application");
        System.out.println("done! sendMessageWithWechat()");
    }

    public void myInternetName(){ // we do not have synchronized keyword here
        System.out.println("This is myInternetName()...");
    }

    public static void main(String[] args) {
        Internet user1 = new Internet();// its own threadA
        Internet user2 = new Internet();// Thread B
        new Thread(() ->{
            user1.sendMessageWithEmail();
        },"thread A ").start();
//        new Thread(() -> {
//            user1.myInternetName(); test this first
//        }
        new Thread(() -> {
            user2.sendMessageWithWechat();
        },"thread B").start();

    }

}
class ReoderingExample{
    private int a = 99;
    private volatile boolean flag = false;// there is not volatile keyword
    public void read(){
        a = 100;
        flag = true;
    }
    public void write(){
        if(flag){
            System.out.println(a);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReoderingExample reoderingExample  = new ReoderingExample();
        Thread threadA = new Thread(() ->{
            reoderingExample.read();
        });

        Thread threadB = new Thread(() ->
        {
            reoderingExample.write();
        });

        threadA.start();;
        threadB.start();

        threadA.join();
        threadB.join();
    }
}