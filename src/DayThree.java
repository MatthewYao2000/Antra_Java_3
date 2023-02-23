import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HASHMAP - GETNODE METHOD
 *
 * SHALLOW COPY VS DEEP COPY
 * [student 1: pengfei yao -> Matt yao] -> --- memeory---
 *                      [student: name]
 * [student2: pengfei yao,] ->
 *
 * Agenda:
 * What is JVM?
 *  it is Java virtual machine, and it will run complied java code(bytecode) and execute it on
 *  the computer's operating system
 *  [System.out.println("Today is Feb 23 2023")]
 *          |
 *          V
 *         [bytecode: yourClassName.class file]
 *                      |
 *                      V
 *                      [ your JVM]
 *                      [  class loader <--> runtime data areas]
 *                      [     execution engine  ]
 *                          |
 *                          V[bytecode to machine code: 100011]
 *                          [ print: Today is Feb 23 2023]
 *
 * JVM components:
 *  1: class loader: it will load .class file into memory area so that the JVM will do further stpes
 *   2: execution engine: running bytecode that is loaded by class loader
 *   3: runtime data area: to provide memory to store bytecode, objects, paramerters,local var, return values
 *   4: memory area: store class and method definitions, constants, static var, all threads have permession to access this area
 *   5: heap: stores objects or arrays on the heap. we also have garbage collection and all threads will access this area
 *   6: stack: stores method invocation, you will need local variables, method arguments, return values
 *          call stack:
 *          stack can be grow and shrink dependes on how many methods you call and return
 *          Error: stackOverflowError
 *
 * HEAP in details
 * [  Eden                space] * hint: eden space is way greater than survivor spaces [8:1:1]
 *
 * [survivor 1][   survivor 2  ]
 * [ tenured generation ]
 * [mete space]
 * // -> run config -> add VM config -> XX:+PrintGCDetails -> apply -> execute your code
 *  metaspace: store method and class information
 *[0.003s][warning][gc] -XX:+PrintGCDetails is deprecated. Will use -Xlog:gc* instead.
 * [0.012s][info   ][gc,heap] Heap region size: 1M
 * [0.014s][info   ][gc     ] Using G1
 * [0.014s][info   ][gc,heap,coops] Heap address: 0x0000000700000000, size: 4096 MB, Compressed Oops mode: Zero based, Oop shift amount: 3
 * [0.142s][info   ][gc,heap,exit ] Heap
 * [0.142s][info   ][gc,heap,exit ]  garbage-first heap   total 262144K, used 3072K [0x0000000700000000, 0x0000000800000000)
 * [0.142s][info   ][gc,heap,exit ]   region size 1024K, 4 young (4096K), 0 survivors (0K)
 * [0.142s][info   ][gc,heap,exit ]  Metaspace       used 6230K, capacity 6319K, committed 6528K, reserved 1056768K
 * [0.142s][info   ][gc,heap,exit ]   class space    used 537K, capacity 570K, committed 640K, reserved 1048576K
 *
 *  Eden space: every new created objects are stored in this area
 *  survivor space: "from space, to space, S0, S1"
 *
 *  tenured generation: some lived object from survivor spaces after garbage collection, they will be
 *  moved into this generation
 *
 *  metaspace: store method and class information
 *
 *  how to determine the object is alive or dead?
 *   1: the referencing count method:  the reason we do not need this, bc there is a problem called circular reference
 *   Student studnet = new Student();
 *   Student studnet1 = nnew Student();
 *   student.instance = student1 // count  == 1
 *   student1.instance = studnet // count always 1
 *   the garbage collection roots:
 *      The GC roots are an object that are considers as alive or dead.
 *   local var and method params:
 *   static var:
 *   thread stack:
 *   class loader:
 *
 *
 *
 *
 * A few sample garbage collection algorithms(GC)
 *  keyword you need to know:
 *      stop-the-world(STW): stop your application during the garbage collection
 *  mark-and-sweep algorithm
 *      normal deletion:
 *        before the garbage collection:  [[ob1][ob2][ob5]...[obN] ]
 *        after the garbage collection:  [[ob1][][ob5].[][][]..[ob100][][][][][][obN]] we have scatters here
 *        what if you will create a big object
 *      deletion and compacting
 *         before the garbage collection:  [[ob1][ob2][ob5]...[obN] ]
 *  *      after the garbage collection:  [[ob1][ob5][ob100][obN][][][][][][]] we do not have scatters here
 *  ###G1 algorithm:##
 *  []: region. Each region can be marked as young generation(further divided into eden space and survivor space)
 *  and old generation;
 *  the size of each region: 2048 (***)
 *  advantages of G1:
 *   low-pause: we do not need to long time for stop-the-world
 *   region-based: the algorithm does not need to scan every regions
 *
 *   steps:
 *    initial mark: the GC will first identifies the roots of the object, and then marks all objects
 *    directly reachable from these roots. the reachable object is alive. during this process, you need stop the wold
 *    concurrent marking: it marks all objects that are reachable from the initial set of marked object. you do not need stop-the-world, which means some objects can be dead during this process
 *    remark: you need stop the world for this. you will double check if there is a dead object
 *    clean-up: clean up the dead objects
 *  [][][][][][][][]
 *  [][][][][][][][]
 *  [][][][][][][][]
 *  [][][][][][][][]
 *  [][][][][][][][]
 *  [][][][][][][][]
 *  multi-threading
 *      3 ways to define a thread
 *      1: extends thread
 *      2: implement runnable
 *      3: implement callable
 *      #####callable vs runnable####
 *      callable            ||                      runnable
 *      you need have a type                     you do not need type
 *      call() you need to return the type      run() you do not
 *      if you try to get the result            you do not
 *      you need try catch
 *   thread states
 *      new : create a new thread, but do not start
 *      runnable : start a new thread, a thread is able to running
 *      blocked : a thread is blocked, bc other thread is using the resource
 *      waiting + timed waiting
 *      terminated
 *                                  new state
 *                                    |
 *         terminated: finish the task  <-runnable <---> blocked
 *                      ^
 *                      || notify(), notifyall()
 *                      V
 *                      wait
 *
 *
 *   common functions you need to know
 *   wait(): is a method that can be called by a thread to give up some shared resources and can be wake up by other thread using notify()
 *   notify(): wake up other thread that is waiting on some shared resources
 *   sleep(int time): to suspend thread's execution for a specified amount of time.
 *   notifyAll(): wake up all threads in your application and all threads will try to get the shared resource.
 *
 *   ######wait() vs sleep()#####
     *  1 11:55:02	Thread-0 we are in test method, I want to sleep 1 s // thread 0 first enter the test method and sleep for 1 s, at this moment, thread 1 is waiting outside
     * 2 11:55:03	Thread-0 I wake up, and I want to wait 2 s // thread 0  is waiting for  2s, thread 1 still waiting
     * 3 11:55:03	Thread-1 we are in test method, I want to sleep 1 s //
     * 4 11:55:04	Thread-1 I wake up, and I want to wait 2 s
     * 5 11:55:05	Thread-0 times up for waiting, but I still want to sleep for 10s
     * 6 11:55:15	Thread-0 I am done
     * 7 11:55:15	Thread-1 times up for waiting, but I still want to sleep for 10s
     * 8 11:55:25	Thread-1 I am done
 *  wait(): a thread is waiting for other thread  to release the shared resource, can be wake uo by other thread
 *          the important thing is that, if a thread uses wait() function, it will release the shared resource
 *   sleep(): a thread will sleep for a specified amount of time, and still hold the shared resource
 *  ###volatile keyword###
 *
 *  ##what is synchronized ## -> everyone know synchronized locks some resource, but what resource locked?
 *  SCENARIO 1
 *
 *  SCENARIO 2
 *
 *  SCENARIO 3
 *
 *
 *
 * */
public class DayThree {
    public static void main(String[] args) {
//        MyThread myThread = new MyThread();
//        myThread.setName("my first thread");
//        myThread.start();
//
//        // runnable thread
//        MyRunnableThread myRunnableThread = new MyRunnableThread();
//        myRunnableThread.run();
//        //lambda expression
//        FutureTask<String> task = new FutureTask<>(new MyCallableThread());
//        new Thread(task).start();
//        try{
//            String res = task.get();
//            System.out.println(res);
//        }catch (InterruptedException | ExecutionException e){
//            e.printStackTrace();
//        }



    }
}
class MyThread extends Thread{
    public void run(){
        System.out.println(getName());
    }
}
class MyRunnableThread implements Runnable{

    @Override
    public void run() {
        System.out.println("this is my runnable thread");
    }
}
class MyCallableThread implements Callable<String>{

    @Override
    public String call() throws Exception {
        return "Hello, I am callable thread";
    }
}

class MyThreadFunctions{
    private AtomicInteger atomicInteger = new AtomicInteger();
    public static void main(String[] args) {
        MyThreadFunctions myThreadFunctions = new MyThreadFunctions();
        new Thread(myThreadFunctions::test).start();
        new Thread(myThreadFunctions::test).start();
    }
    private synchronized void test(){
        try{
            log("we are in test method, I want to sleep 1 s");
            Thread.sleep(1000);
            log("I wake up, and I want to wait 2 s");
            wait(2000);
            log("times up for waiting, but I still want to sleep for 10s");
            Thread.sleep(10000);
            log("I am done");
            notify();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void log(String s){
        System.out.println(atomicInteger.incrementAndGet() + " " + new Date().toString().split(" ")[3]
        + "\t" + Thread.currentThread().getName() + " " + s);
    }
}