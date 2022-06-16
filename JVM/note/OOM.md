# JVM 内存区域及 OOM
## 内存区域
- 线程公有：
  
  - 方法区（Method Area）:

    用来存放类的静态变量，常量及类基本信息等。属于概念模型中的一部分，JDK8前以 永久代（Permanent Generation）实现；JDK8 后用 元空间（Metaspace）实现，与之前相比，方法区的串池被移出到堆中，并且元空间位于本地内存，与操作系统直接相关。
    
  - 堆（heap）
   
    唯一作用是存储对象实例。

- 线程私有：

  - 程序计数器（PCR）：
    
    指向下一条指令所在地址。用于控制分支，判断等以及 线程恢复。

  - 虚拟机栈（VM Stack）：

    在线程的方法执行时，对应生成一个栈帧，存储方法的 局部变量表（基本数据类型，引用类型对象的引用以及 returnAddress），方法出口，动态连接等。

  - 本地方法栈（Native Method Stack）：

    同虚拟机栈，但存储的是本地方法的信息。

## OOM
  详见 src

  唯一不会出现 OOM 的是 PCR

  - 堆 OOM：
    
    1.内存泄漏（leak）：由于某种原因，导致某些对象不能被回收

    2.内存溢出：对象实例创建过多

  - 方法区 OOM：

    一般是由于动态生成的对象过多，需要加载很多类，导致类信息过多，或是因为常量池不足，方法区溢出。

    - 运行时常量池:

      注意String:intern() 导致的内存溢出
  
  - 栈 SOF：
   
    1. 栈帧过大
    
    2. 栈帧数过多

    3. 线程创建过多，导致分配不足的 OOM

  - DirectMemory：

    直接内存分配不足导致 OOM

## 拓展主题——对象的创建，内存布局和访问

1. new 指令：

在常量池查找，是否存在该指令中参数——类的符号引用，若无（即类未被加载），则先加载，解析及初始化类

2. 为对象分配内存

对象所占用的内存是确定的——分配一块固定的堆内存存储对象。分配方式由两种：其一，指针碰撞法，保证内存的规整性，指针置于有对象存储和无存储的交界之处，分配好内存后，将指针移动相应的距离。这种方法需要注意并发问题，一个好的解决方法是 为每个线程分配专有内存；其二，空闲列表法，这种方式需要维护一个空闲列表，以便对象创建时分配，在物理上对象存储区域可能不是连续的。

然后在初始化类的各个字段值，0，null，false

3. 程序的初始化

如构造函数被调用。

### 对象的布局
1. Object Header：

  a.对象运行时存储的数据：HashCode，锁状态，线程持有锁

  b.类型指针：指向存储在方法区的类型信息等

2. Instance Data

3. Padding：补充规整

### 对象的访问

1. 句柄池：两次访问，但易于对象移动和改变

2. 直接访问：一次访问，但不易改变

