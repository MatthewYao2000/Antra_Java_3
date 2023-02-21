import java.util.ArrayList;

public class Main {
    // tomorrow: arraylist, set hashmap, list, linked list, queue

    /*
    * Agenda:
    * what is oop?
    *   what is object?
    *   attributes: state: fundamental characteristics that currently define the object
    *               behaviors: the activities associated with the object
    *   attributes:
    *       polymorphism:
    *           many forms
    *           overloading vs overriding
    *           oveloading: the same method name, but different variables: such as the # of var, types of var
    *           overrding: code reusability
    *
    *       encapsulation:
    *           protect or manage its own information
    *        inheritance:
    *           provides: code reusability
    *               Java has interface and abstract class
    *
    * Java naming principle
    *   Class name: the first one should be upper
    *   Package: all letters should be low case
    *   interface: first letter of the first word should upper
    *   constant: all should be upper
    *             : MY_FULL_NAME = "pengfei Yao"
    *   method: first letter should be lower
    *
    *
    * primitive and wrapper type
    * int, byte, char, long, float, double, boolean, short
    * Integer, Byte, ....
    * what is diff between == and equals()
    *
    *
    * String vs string builder vs string buffer
    *
    *
    * interface vs abstract class
    *
    * this vs super
    *
    * java pass by reference or pass by value
    *   int a = 99
    *   myMethod(int a){
    *       a = 999;
    *   }
    * print(a) =>99
    * array: the value of parameter constains a reference to an array;
    * int[] myArray = new int[1];
    * myArray[0] = 99
    * method(int[] array){
    *   array[0] = 1
    * }
    * print(myArray) = 1
    * shallow copy vs deep copy
    * deep
    * shallow copy: creates anew object on the heap. but if the internal properties of the original object
    * are reference type. the shallow copy just copies the referene address of the internal object
    *
    *                ------heap-------
    * student1 ->   [student]
    *                   |
    *                  name
    *                     |
    * student2 ->   [student]
    *
    * deep copy: makes an exact copy of the entire object, including the internal objects contained in this object
    *               ------heap-------
     * student1 ->   [[student] -> name]
     *
     *
     * student2 ->   [[student] -> name]
    * exceptions
    *      throwable
    * /                       \
    * exception               error
    * /           \                \
    * checked exc unchecked        outofmemory
    *    checked: happens in compile time.
    *       unchecked exception: happens in runtime
    *           indexOutofBounds
    *               int[] a = new int[99];
    *               for(){
    *                       a[100] // index outofBounds
    *           }
    * generic
    *   there are three differences generic: class generic, interface generic, method generic
    *
    * */
    class MyClassGeneric<T>{
        private T myName;
        MyClassGeneric(T myName){
            this.myName = myName;
        }
        private  <T,K> void out(T myPrint, K myAge){
            System.out.println( myPrint);
        }
    }


}
class TestExpcetion {
    public static void main(String[] args) throws ClassNotFoundException {
        Class clazz = Class.forName("com.my.package.address");
    }
}

// overriding vs overloading EXA
class People{
     int id;
     String name;

    People(int id, String name){
       this.id = id;
       this.name = name;
    }
    public void write(){
        System.out.println("I am writing ");
    }
}
class Employee extends People{
    private int salary = 10000;
    Employee(int id, String name, int salary){
        super(id, name);// using super to call parent constructor
        super.write();// using super to call parent's method
        this.salary = salary;
    }
    void out(){
        System.out.println(id + " " + name  + " " + salary );
    }

    public static void main(String[] args) {
        new Employee(1,"Pengfei Yao", 10000).out();
    }
}
//this keyword vs super keyword
// "this" can be used to reference current object
// this() can be used for call constructor
// this can be used as parameter
// this can be used as constructor parameter
// super call parent constructor
//super can call parent method
class Student{
    private int age = 18;
    private String myName;
    public Student(int age, String myName){

        this.age = age;// if there is no this, then the value of name and age will assign to itself, not current object
        this.myName = myName;

    }
    public void printOut(){
        System.out.println(" my age is "+ age + "my name is " + myName);
    }
    // this() can be used for call constructor

    public static void main(String[] args) {
        Student student = new Student(20, " pengfei yao");
        student.printOut();
    }
}
class ThisTest{
    ThisTest(){
        System.out.println("This is ThisTest constructor");
    }
    ThisTest(int count){
        this();
        System.out.println(count);
        //this();
    }

    public static void main(String[] args) {
        ThisTest thisTest = new ThisTest(100);
    }
}
// this can be used as parameter
class ThisAsParameter{
    void myMethod(ThisAsParameter thisAsParameter){
        System.out.println(thisAsParameter);
    }
    void mySecondMethod(){
        myMethod(this);
    }

    public static void main(String[] args) {
        ThisAsParameter thisAsParameter = new ThisAsParameter();
        System.out.println(thisAsParameter);
        thisAsParameter.mySecondMethod();
        ThisAsParameter thisAsParameter1 = new ThisAsParameter();
        System.out.println(thisAsParameter1);
        thisAsParameter1.mySecondMethod();
    }
}
// this can be used as constructor parameter
class ThisAsConstructor{
     int count = 10;
    ThisAsConstructor(){
        TestThisAsContructor testThisAsContructor = new TestThisAsContructor(this);
        testThisAsContructor.out();
    }

    public static void main(String[] args) {
        new ThisAsConstructor();
    }
}
class TestThisAsContructor{
    ThisAsConstructor param;
    TestThisAsContructor(ThisAsConstructor param){
        this.param = param;
    }
    void out(){
        System.out.println(param.count);
    }
}
//encapsulation: private
class EncapTest{
    private String myName;

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyName() {
        return myName;
    }
}
//interface and abstract class
// interface vs abstract class
// interface using interface keyword, abstract class using abstract keyword
// var defineded in interface should be public static final, but abstract lass
// one class can has multiple interfaces but you can have ONLY one abstract class.

interface MyInterfaceClass{
    public static final int myVar = 0;

}
abstract class MyabstractClass{
    int myVar = 0;
}
interface MySecond{

}
class A {

    static void myPrimitive(int a){
        a = 100;
    }
    static  void myWrapper(Integer a ){
        a = 2;
    }
    public static void main(String[] args) {
        // can i have new keyword to initial abstract class and interface class
        // MyabstractClass myclass = new MyabtractClass
        //MyabstractClass myabstractClass = new MyabstractClass();
        //MyInterfaceClass myInterfaceClass = new MyInterfaceClass() ;
//        Integer a = 200; // => Integer a = Integer.valueOf(200)
//        Integer b = 200;// => Integer a = Integer.valueOf(200)
//        System.out.println(a == b);
//        Integer c = 100;
//        Integer d = 100;
//        System.out.println(c == d);
//        System.out.println(a.equals(b));
//        int f = 200;
//        int g = 200;
//        System.out.println(f == g);
        Integer myInteger = new Integer(12);// range(-128, 127)
        Integer mySecondInteger = new Integer(12);
        System.out.println(myInteger == mySecondInteger);//false,
        System.out.println(myInteger.equals(mySecondInteger));//true
        //String vs String builder vs String buffer
        String first = "pengfei";
        String last = " Yao";
        String full = first + last;// sugar syntax
        // Stringbuilder myNewfisrt = new Stringbuilder(first)
        // Stringbuilder mylast = new Stringbuilder(last)
        // String full =
        // toString()
        // string builder
        int a = 1;
        myPrimitive(a);
        System.out.println(a);

        Integer b = 999;// to define a new integer wrapper type, it still using int primitive type
        myPrimitive(b);
        System.out.println(b);
    }
}