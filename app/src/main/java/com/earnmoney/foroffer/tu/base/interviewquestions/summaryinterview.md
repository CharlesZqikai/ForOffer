1.java中==和equals和hashCode的区别
   == 比较的是两个对象的 hashCode  equals 比较的是两个对象的值

2.int、char、long各占多少字节数
  字节：byte：用来计量存储容量的一种计量单位；位：bit
  一个字节等于8位  1byte = 8bit
  
  char占用的是2个字节 16位，所以一个char类型的可以存储一个汉字。
  
  整型：
  
  byte:1个字节 8位 -128~127
  
  short ：2个字节 16位
  
  int ：4个字节 32位
  
  long：8个字节 64位
  
  浮点型：
  
  float：4个字节 32 位
  
  double ：8个字节 64位
  
  注：默认的是double类型，如3.14是double类型的，加后缀F（3.14F）则为float类型的。
  
  char类型：
  
  char：2个字节。
  
  Boolean 类型
  
  boolean: （true or false）（并未指明是多少字节  1字节  1位 4字节）
  
3. int与integer的区别
   基本数据类型与对象的区别, 他们之间可以自由的拆箱装箱,但是对效率有一定的影响
   
4.谈谈对java多态的理解

  实现多态的技术称为：动态绑定（dynamic binding），是指在执行期间判断所引用对象的实际类型，根据其实际的类型调用其相应的方法。
  
  多态的作用：消除类型之间的耦合关系。
  
  多态的实现的必要条件：继承，重写，父类引用指向子类对象（即，声明是父类，实际指向的是子类的一个对象）
  
5.String、StringBuffer、StringBuilder区别
  1、用来处理字符串常用的类有3种：String、StringBuffer和StringBuilder
  2、三者之间的区别：都是final类，都不允许被继承；String类长度是不可变的，StringBuffer和StringBuilder类长度是可以改变的；
  StringBuffer类是线程安全的，StringBuilder不是线程安全的；
  String 和 StringBuffer：
  1、String类型和StringBuffer类型的主要性能区别：
  String是不可变的对象，因此每次在对String类进行改变的时候都会生成一个新的string对象，然后将指针指向新的string对象，所以经常要改变字符串长度的话不要使用string，
  因为每次生成对象都会对系统性能产生影响，特别是当内存中引用的对象多了以后，JVM的GC就会开始工作，性能就会降低；
  2、使用StringBuffer类时，每次都会对StringBuffer对象本身进行操作，而不是生成新的对象并改变对象引用，所以多数情况下推荐使用StringBuffer，
  特别是字符串对象经常要改变的情况；3、在某些情况下，String对象的字符串拼接其实是被Java Compiler编译成了StringBuffer对象的拼接
  ，所以这些时候String对象的速度并不会比StringBuffer对象慢，
  例如： String s1 = “This is only a” + “ simple” + “ test”;
  StringBuffer Sb = new StringBuilder(“This is only a”).append(“ simple”).append(“ test”);
  生成 String s1对象的速度并不比 StringBuffer慢。其实在Java Compiler里，自动做了如下转换：
  Java Compiler直接把上述第一条语句编译为：
  String s2 = “This is only a”;
  String s3 = “ simple”;
  String s4 = “ test”;
  String s1 = s2 + s3 + s4;
  这时候，Java Compiler会规规矩矩的按照原来的方式去做，String的concatenation（即+）操作利用了StringBuilder（或StringBuffer）的append方法实现，此时，
  对于上述情况，若s2，s3，s4采用String定义，拼接时需要额外创建一个StringBuffer（或StringBuilder），之后将StringBuffer转换为String；
  若采用StringBuffer（或StringBuilder），则不需额外创建StringBufferStringBuilderStringBuilder是5.0新增的，
  此类提供一个与 StringBuffer 兼容的 API，但不保证同步。该类被设计用作 StringBuffer 的一个简易替换，用在字符串缓冲区被单个线程使用的时候（这种情况很普遍）。
  如果可能，建议优先采用该类，因为在大多数实现中，它比 StringBuffer 要快。
  两者的方法基本相同使用策略1、基本原则：如果要操作少量的数据，用String ；
  单线程操作大量数据，用StringBuilder ；
  多线程操作大量数据，用StringBuffer。
  2、不要使用String类的”+”来进行频繁的拼接，因为那样的性能极差的，应该使用StringBuffer或StringBuilder类，这在Java的优化上是一条比较重要的原则，例如：
  String result = "";
  for (String s : hugeArray) {
  result = result + s;
  }
  // 使用StringBuilder
  StringBuilder sb = new StringBuilder();
  for (String s : hugeArray) {
  sb.append(s);
  }
  String result = sb.toString();
  当出现上面的情况时，显然我们要采用第二种方法，因为第一种方法，每次循环都会创建一个String result用于保存结果，
  除此之外二者基本相同3、 StringBuilder一般使用在方法内部来完成类似”+”功能，因为是线程不安全的，所以用完以后可以丢弃。
  StringBuffer主要用在全局变量中4、相同情况下使用 StirngBuilder 相比使用 StringBuffer 仅能获得 10%~15% 左右的性能提升，
  但却要冒多线程不安全的风险。而在现实的模块化编程中，负责某一模块的程序员不一定能清晰地判断该模块是否会放入多线程的环境中运行，
  因此：除非确定系统的瓶颈是在 StringBuffer 上，并且确定你的模块不会运行在多线程模式下，才可以采用StringBuilder；否则还是用StringBuffer
  
6.什么是内部类？内部类的作用   请仔细阅读 ☆☆☆☆☆☆☆☆
  参考链接:https://juejin.im/post/5a903ef96fb9a063435ef0c8
   回答点: 多继承, 隐藏实现, 静态非静态, 匿名内部类,内存泄漏
   
7.抽象类和接口区别
 
  抽象类可以有默认的方法实现完全是抽象的。接口根本不存在方法的实现。
  实现 抽象类使用extends关键字来继承抽象类。如果子类不是抽象类的话，它需要提供抽象类中所有声明的方法的实现。
  子类使用关键字implements来实现接口。它需要提供接口中所有声明的方法的实现。
  
  抽象类可以有构造器，而接口不能有构造器.
  抽象方法可以有public、protected和default这些修饰符 
  接口方法默认修饰符是public。你不可以使用其它修饰符。
  抽象类在java语言中所表示的是一种继承关系，一个子类只能存在一个父类，但是可以存在多个接口。
  
  抽象方法比接口速度要快
  接口是稍微有点慢的，因为它需要时间去寻找在类中实现的方法。
  
  如果你往抽象类中添加新的方法，你可以给它提供默认的实现。因此你不需要改变你现在的代码。 如果你往接口中添加方法，那么你必须改变实现该接口的类。
  
  ☆☆☆☆☆☆☆☆
  1.语法层面上的区别
  
  　　1）抽象类可以提供成员方法的实现细节，而接口中只能存在public abstract 方法；
  
  　　2）抽象类中的成员变量可以是各种类型的，而接口中的成员变量只能是public static final类型的；
  
  　　3）接口中不能含有静态代码块以及静态方法，而抽象类可以有静态代码块和静态方法；
  
  　　4）一个类只能继承一个抽象类，而一个类却可以实现多个接口。
  
  2.设计层面上的区别  ☆☆☆☆☆☆☆☆
  
  　　1）抽象类是对一种事物的抽象，即对类抽象，而接口是对行为的抽象。抽象类是对整个类整体进行抽象，包括属性、行为，但是接口却是对类局部（行为）进行抽象。
       举个简单的例子，飞机和鸟是不同类的事物，但是它们都有一个共性，就是都会飞。那么在设计的时候，可以将飞机设计为一个类Airplane，将鸟设计为一个类Bird，
       但是不能将 飞行 这个特性也设计为类，因此它只是一个行为特性，并不是对一类事物的抽象描述。此时可以将 飞行 设计为一个接口Fly，包含方法fly( )，
       然后Airplane和Bird分别根据自己的需要实现Fly这个接口。然后至于有不同种类的飞机，比如战斗机、民用飞机等直接继承Airplane即可，对于鸟也是类似的，
       不同种类的鸟直接继承Bird类即可。从这里可以看出，继承是一个 "是不是"的关系，而 接口 实现则是 "有没有"的关系。如果一个类继承了某个抽象类，则子类必定是抽象类的种类，
       而接口实现则是有没有、具备不具备的关系，比如鸟是否能飞（或者是否具备飞行这个特点），能飞行则可以实现这个接口，不能飞行就不实现这个接口。
  
  　　2）设计层面不同，抽象类作为很多子类的父类，它是一种模板式设计。
        而接口是一种行为规范，它是一种辐射式设计。
        什么是模板式设计？最简单例子，大家都用过ppt里面的模板，如果用模板A设计了ppt B和ppt C，ppt B和ppt C公共的部分就是模板A了，如果它们的公共部分需要改动，
        则只需要改动模板A就可以了，不需要重新对ppt B和ppt C进行改动。而辐射式设计，比如某个电梯都装了某种报警器，一旦要更新报警器，就必须全部更新。
        也就是说对于抽象类，如果需要添加新的方法，可以直接在抽象类中添加具体的实现，子类可以不进行变更；
        而对于接口则不行，如果接口进行了变更，则所有实现这个接口的类都必须进行相应的改动。
  
8.抽象类的意义  模板设计模式

9.抽象类与接口的应用场景 
  请参考区别里面的设置层面作答
  
10.接口的意义
   请参考区别里面的设置层面作答
   
11.泛型中extends和super的区别   这个理解作答
   PECS原则
   如果要从集合中读取类型T的数据，并且不能写入，可以使用 ? extends 通配符；(Producer Extends)
   如果要从集合中写入类型T的数据，并且不需要读取，可以使用 ? super 通配符；(Consumer Super)
   如果既要存又要取，那么就不要使用任何通配符。
   
12.父类的静态方法能否被子类重写
  不能撒.
  因为静态方法从程序开始运行后就已经分配了内存，也就是说已经写死了。所有引用到该方法的对象（父类的对象也好子类的对象也好）所指向的都是同一块内存中的数据，也就是该静态方法。
  子类中如果定义了相同名称的静态方法，并不会重写，而应该是在内存中又分配了一块给子类的静态方法，没有重写这一说。

13.进程和线程的区别


14.final，finally，finalize的区别

15.Android 序列化

序列化 : 把Java对象转换为字节序列,并存储至一个储存媒介的过程。
什么是反序列化？
把字节序列恢复为Java对象的过程。
简单说法是：序列化把当前对象信息保存下来。反序列化刚好相反的操作，即读取信息设置到当前对象上。
序列化作用
    1）永久性保存对象，保存对象的字节序列到本地文件中；
　　2）通过序列化对象在网络中传递对象；
　　3）通过序列化在进程间传递对象。
安卓里面的两种序列化的方式：
    Serializable 和 Parcelable 接口可以完成对象的序列化过程：
  
  具体作用：

1、通过Intent 和 Binder 传输数据时就需要使用
2、需要将对象持久化到存储设备上或者通过网络传输给其他客户端
两者的区别
android上应该尽量采用Parcelable，效率至上
编码上：
Serializable代码量少，写起来方便
Parcelable代码多一些
效率上：
Parcelable的速度比Serializable 高十倍以上
serializable的迷人之处在于你只需要对某个类以及它的属性实现Serializable 接口即可。Serializable 接口是一种标识接口（marker interface），这意味着无需实现方法，
Java便会对这个对象进行高效的序列化操作。
这种方法的缺点是使用了反射，序列化的过程较慢。这种机制会在序列化的时候创建许多的临时对象，容易触发垃圾回收。
Parcelable方式的实现原理是将一个完整的对象进行分解，而分解后的每一部分都是Intent所支持的数据类型，这样也就实现传递对象的功能了
  1）在使用内存的时候，Parcelable比Serializable性能高，所以推荐使用Parcelable。

  2）Serializable在序列化的时候会产生大量的临时变量，从而引起频繁的GC。
  3）Parcelable不能使用在要将数据存储在磁盘上的情况，因为Parcelable不能很好的保证数据的持续性在外界有变化的情况下。
  尽管Serializable效率低点，但此时还是建议使用Serializable。
  4）Serializable的实现，只需要implements Serializable 即可。这只是给对象打了一个标记，系统会自动将其序列化。
  5）Parcelabel的实现，不仅需要implements Parcelabel，还需要在类中添加一个静态成员变量CREATOR，这个变量需要实现 Parcelable.Creator 接口。
  6)  Parcelable的性能比Serializable好，在内存开销方面较小，所以在内存间数据传输时推荐使用Parcelable，如activity间传输数据，
  而Serializable可将数据持久化方便保存，所以在需要保存或网络传输数据时选择Serializable，因为android不同版本Parcelable可能不同，所以不推荐使用Parcelable进行数据持久化

16.静态内部类的设计意图

17.string 转换成 integer的方式及原理
 parseInt(String s)--内部调用parseInt(s,10)（默认为10进制）
 正常判断null，进制范围，length等
 判断第一个字符是否是符号位
 循环遍历确定每个字符的十进制值
 通过*= 和-= 进行计算拼接
 判断是否为负值 返回结果
 
 
 java 进阶
 
 1.哪些情况下的对象会被垃圾回收机制处理掉？
 ???
 
 2.utf-8编码中的中文占几个字节；int型几个字节？
 3个或者4个,
                       1个
 3.静态代理和动态代理的区别，什么场景使用？
 静态: 解耦,代理对象少,接口不会变动的.
 动态代理: AOP
 
 4.谈谈你对解析与分派的认识。
  要从jvm虚拟机原理去解释????????
 1.方法在程序真正运行之前就有一个可确定的调用版本，并且这个方法的调用版本在运行期间是不可变的，即“编译时可知，运行不可以变”，这类目标的方法的调用称之为解析
 2.解析调用一定是个静态的过程，在编译期就完全确定，在类加载的解析阶段就将涉及的符号引用全部转变为可以确定的直接引用，不会延迟到运行期再去完成。
 而分派（Dispatch）调用则可能是静态的也可能是动的。于是分派方式就有静态分派和动态分派。
 静态分派的最直接的解释是在重载的时候是通过参数的静态类型而不是实际类型作为判断依据的。因此在编译阶段，Javac编译器会根据参数的静态类型决定使用哪个重载版本。
 显然这里不可能根据静态类型来决定调用那个方法。导致这个现象很明显的原因是因为这两个变量的实际类型不一样，jvm根据实际类型来分派方法执行版本。

5.修改对象A的equals方法的签名，那么使用HashMap存放这个对象实例的时候，会调用哪个equals方法？
 会调用对象对象的equals方法。
 “==”如果是基本类型的话就是看他们的数据值是否相等就可以。
 如果是引用类型的话，比较的是栈内存局部变量表中指向堆内存中的指针的值是否相等
 “equals”如果对象的equals方法没有重写的话，equals方法和“==”是同一种。
 hashcode是返回对象实例内存地址的hash映射。

6.Java中实现多态的机制是什么？
  参考上面多态的理解.
  
7.如何将一个Java对象序列化到文件里？
    1、在Java中，只要一个类实现了java.io.Serializable接口，那么它就可以被序列化。
    2、通过ObjectOutputStream和ObjectInputStream对对象进行序列化及反序列化
    3、虚拟机是否允许反序列化，不仅取决于类路径和功能代码是否一致，一个非常重要的一点是两个类的序列化 ID 是否一致（就是 private static final long serialVersionUID）
    4、序列化并不保存静态变量。
    5、要想将父类对象也序列化，就需要让父类也实现Serializable 接口。
    6、Transient 关键字的作用是控制变量的序列化，在变量声明前加上该关键字，可以阻止该变量被序列化到文件中，在被反序列化后，transient 变量的值被设为初始值，如 int 型的是 0，
    对象型的是 null。
    7、服务器端给客户端发送序列化对象数据，对象中有一些数据是敏感的，比如密码字符串等，希望对该密码字段在序列化时，进行加密，而客户端如果拥有解密的密钥，
    只有在客户端进行反序列化时，才可以对密码进行读取，这样可以一定程度保证序列化对象的数据安全。
 