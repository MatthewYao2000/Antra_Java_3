import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Agenda:
 * 1: stream api
 *  review:
 *      stream api: intermedia functions : filter(),map(), limit(),sorted()
 *      and terminal functions: forEach(), collect(), anyMATCH(),allMacth()
 * // sum all of integers that divide by 3 and 5
 *         List<Integer> myList = Arrays.asList(3,5,20,9,8,7,4,15);
 *
 *         int sum = myList.stream()
 *                 .filter(n -> n % 3 == 0 && n % 5 == 0)
 *                 .mapToInt(Integer::intValue).sum();
 *         System.out.println(sum);
 *
 *         //anymatch() and allmatch()
 *         boolean anyElement = myList.stream().anyMatch(n -> n >= 15);
 *         if (anyElement){
 *             System.out.println("yes");
 *         }
 *         //allMatch()
 *         boolean allElement = myList.stream().allMatch( n -> n % 2 == 0);
 *         if (allElement){
 *             System.out.println("all elements are even");
 *         }else {
 *             System.out.println("not all elements are even");
 *         }
 *
 *         int[] myArray = {1,2,3,4,5,6,7,8,9,0};
 *         Arrays.stream(myArray).parallel().forEach(System.out::println);
 *
 * 2: ###Collections vs stream api####
 * collections: it is a data structure that store and manipulate data in memory
 * stream api: it processes data in a function
 *
 * collection: can be modified by adding and removing element, whereas stream apis do not allow you to add or remove the orignal list
 *
 *
 * 3: parallel stream api
 *      it  will split the works into smaller streams that can be processed in parallel
 *      parallel processing is useful when processing large collections of data,such as data analysis, machine leaning
 * 4:optional
 * why do we should use it?
 *  short answer: to avoid null pointer exception
 *
 * 5:####Completable Future####
 *  it is a class that provides a way to perform asynchronous computation, it is a powerful tool for writing concurrent code in java
 *   so that you improve your application
 *   what is asynchronous computation?
 *      thread A --> processing a very large file--------->
 *      ----25% --------50%--------75%----------100%---->
 *     thread B->\--> email that tells you are finish 25%
 * 5.1: supplier vs consumer
 *       supplier: it is a functional interface and has a single method get(),
 *       get() takes no param and return a value
 *       consumer: it is a functional interface and has a single method accept()
 *       accept() takes one param and return no value
 *       code reusability and simplify code -> improved performance
 *
 *
 * 5.2: how to use runAsync, supplyAsync with/without thread pool
 *      runAsync no return value, supplyAsync returns value
 *      without thread pool: runAsync and supplyAsync both use forkJoin pool,
 *      with thread pool: they are using some pool else
 * 5.3: get() vs join()
 *      with get() you need to throw exceptions
 *      join() you do not need to throw exceptions
 *
 * 5.4: getNow()
 *
 * 5.5 complete(),  thenApply, thenAccept()
 *
 * 5.6: thenRun() vs thenApply() vs thenAccept
 *      thenRun(): to do task B when task A finish, task B does not need to return result: join() -> null
 *      thenAccept(): to do task B when task A finish and take the result from task A, but does not return value: joint() -> null
 *      thenApply(): to do task B when task A finish and take the result from task A, and return the result: join() -> your result
 * 5.7: thenCombine()
 *
 * 6: method reference
 *      reference to a staic method: className :: static method
 *                  an instance method of a particular object: objectName: instanceMethodName
 *                  a constructor:: className:: new objectName
 * 7: what is cas: compare and set/swep
 *
 * 8: ## concurrentHashMap in java 8 and 7
 * 8.1: discuss get() and put()
 * 9:## blocking queue
 * 9.1: the logic of put() method and get() method
 *
 * 10: drawbacks of synchronized keyword
 *
 * 11: pessimistic lock vs optimistic lock
 *
 * 12: what are common lock you should know?
 *
 *
 * */
class CompletableFutureDemo{
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //using Future task --> implments --> runnable future --> extends --> runnable and  future
//        FutureTask<String> futureTask = new FutureTask<>(new MyCallable());
//        Thread t1 = new Thread(futureTask,"thread 1");
//        t1.start();
//        System.out.println("the value is: " + futureTask.get());
        //comparing the time of tasks to finish without future/ with future
//        long startTime = System.currentTimeMillis();
        // with this case, we do not have future task
//        // suppose this you business logic1
//         try{TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e){e.printStackTrace();}
//         //suppose this you business logic2
//         try{TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e){e.printStackTrace();}
//         //suppose this you business logic 3
//         try{TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e){e.printStackTrace();}
        // using thread pool to help me create 3 threads
        // Thread thread 1 = new thread() -> this
//            ExecutorService threadPool = Executors.newFixedThreadPool(3);
//            long startTime = System.currentTimeMillis();
//            FutureTask<String> futureTask1 = new FutureTask<>(()->{
//               // logic here
//                try{TimeUnit.MILLISECONDS.sleep(5000);} catch (InterruptedException e){e.printStackTrace();}
//                 return "task1";
//            });
//            threadPool.submit(futureTask1);
//
//        FutureTask<String> futureTask2 = new FutureTask<>(()->{
//            // logic here
//            try{TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e){e.printStackTrace();}
//            return "task2";
//        });
//        threadPool.submit(futureTask2);
//        FutureTask<String> futureTask3 = new FutureTask<>(()->{
//            // logic here
//            try{TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e){e.printStackTrace();}
//            return "task3";
//        });
//        threadPool.submit(futureTask3);
//        long endTime = System.currentTimeMillis();
//        System.out.print("--- cost time: "+ (endTime - startTime));
//        threadPool.shutdown();


        // runAsync() vs supplyAsync()
        // runAsync and supplyAsync() without thread pool
//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
//            System.out.println(Thread.currentThread().getName());
//            // our task
//             try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e){e.printStackTrace();}
//
//        });
//        System.out.println(completableFuture.get());

//        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(()->{
//            System.out.println(Thread.currentThread().getName());
//             try{TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e){e.printStackTrace();}
//             return "my supplyAsync";
//        });
//        System.out.println(completableFuture.get());

        // runAsync and supplyAsync() with thread pool

        ExecutorService threadPool =  Executors.newFixedThreadPool(3);
//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
//            System.out.println(Thread.currentThread().getName());
//            // our task
//             try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e){e.printStackTrace();}
//
//        },threadPool);
//        System.out.println(completableFuture.get());
//        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(()->{
//            System.out.println(Thread.currentThread().getName());
//             try{TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e){e.printStackTrace();}
//             return "my supplyAsync with thread pool";
//        },threadPool);
//         try{TimeUnit.MILLISECONDS.sleep(2000);} catch (InterruptedException e){e.printStackTrace();}
//
//        System.out.println(completableFuture.getNow("this is my getNow()"));
//        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(()->{
//             try{TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e){e.printStackTrace();}
//             return "supplyAsync is complete";
//        },threadPool);
//         try{TimeUnit.MILLISECONDS.sleep(3000);} catch (InterruptedException e){e.printStackTrace();}
//
//        System.out.println(completableFuture.complete("complete method") + " : " + completableFuture.get());
//        threadPool.shutdown();

        // I want to have increase i by one using thenApply()
//            CompletableFuture completableFuture = CompletableFuture.supplyAsync(()->{
//                 try{TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e){e.printStackTrace();}
//                 int i = 1;
//                 return  i;
//            },threadPool).thenApply(r ->{
//                System.out.println("this is then apply 1");
//                return r + 1;
//            }).thenApply(r -> {
//                System.out.println("this is then apply 2");
//                return r  + 1;
//            }).thenApply(r -> {
//                System.out.println("this is then apply 3");
//                return r  + 1;
//            }).whenComplete((r,e) ->{// r is result, e is exception
//                if(e == null){
//                    System.out.println(" my result is: " + r);
//                }
//            });

            // compare then accept, then apply then run

//        System.out.println(CompletableFuture.supplyAsync(()->"this is my thenRun()").thenRun(()->{}).join());
//        System.out.println(CompletableFuture.supplyAsync(()->"this is my then accept").thenAccept(r -> System.out.println(r)).join());
//        System.out.println(CompletableFuture.supplyAsync(()->"this is my then apply").thenApply(r -> r).join());
//
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(()->{
             try{TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e){e.printStackTrace();}
             return 1;
        });
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(()->{
             try{TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e){e.printStackTrace();}
             return 2;
        });
        CompletableFuture res = completableFuture1.thenCombine(completableFuture2,(x,y)->{
            System.out.println(x);
            System.out.println(y);
            return x+y;
        });
        System.out.println(res.join());
       threadPool.shutdown();
    }

}
class MyCallable implements Callable<String>{

    @Override
    public String call() throws Exception {
        return "this is my callable method";
    }
}
public class DayFive {
    public static void main(String[] args) {
       BankAccount bankAccount1 = new BankAccount("111",1000.0,Optional.empty());
       BankAccount bankAccount2  = new BankAccount("222",500.0,Optional.of("myPassword"));
       Optional<String> myPassword1 = bankAccount1.getPassword();
       Optional<String> myPassword2 = bankAccount2.getPassword();
       if(myPassword1.isPresent()){
           System.out.println(myPassword1 + " is bank account 1");
       }else{
           System.out.println("we do not have password for bank account 1");
       }

       if (myPassword2.isPresent()){
           System.out.println(myPassword2 + " is bank account 2");
       }else{
           System.out.println("we do not have password for bank account 2");
       }
    }
}
class BankAccount{
    private String accountName;
    private double amount;
    private Optional<String> password;

    public BankAccount(String accountName, double amount, Optional<String> password) {
        this.accountName = accountName;
        this.amount = amount;
        this.password = password;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPassword(Optional<String> password) {
        this.password = password;
    }

    public String getAccountName() {
        return accountName;
    }

    public double getAmount() {
        return amount;
    }

    public Optional<String> getPassword() {
        return password;
    }
}
