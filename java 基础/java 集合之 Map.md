## Map
#### HashMap 的底层数据结构？
- jdk1.7：桶数组 & 链表（解决冲突）
- jdk1.8：桶数组 & 链表 O(N) or 红黑树O（logN）

  链表转换红黑树条件：
  - 链表长度达到 8 
  - 桶数组容量到达 64
  
  一般来说，如果链表长度大于 8，但数组容量仍小于 64，首先选择扩容，而不是树化，以减少
  搜索时间。

#### 解决 hash 冲突的方法？ HashMap 用的哪种？
- 开放定址法：即再散列法，`p = hash(key)`出现冲突时，再以 p 进行二次 hash：`p1 = hash(p)`
直到不发生冲突为止，因此该法必须要求哈希表容量大于所要存放的元素个数
- 再哈希法：即多重散列，提供多个散列函数，发生冲突时，换一种函数计算哈希值，直至没有冲突为止
- 拉链法： 将哈希值相同的元素构成一个同义词的单链表,并将单链表的头指针存放在哈希表的第i个单元中，查找、插入和删除主要在同义词链表中进行。链表法适用于经常进行插入和删除的情况。
hashMap 采用该法。

#### 为什么在解决hash冲突的时候，不直接用红黑树?而选择先用链表，再转红黑树?
因为红黑树需要进行左旋，右旋，变色这些操作来保持平衡，而单链表不需要。
当链表元素小于 8（默认树化阈值） 个的时候，此时做查询操作，链表结构已经**能保证查询性能。**
当链表元素大于 8（默认树化阈值） 个的时候， 红黑树搜索时间复杂度是 O(logn)，而链表是 O(n)，此时需要红黑树来加快查询速度，但是新增节点的效率变慢了。

因此，如果一开始就用红黑树结构，元素太少，新增效率又比较慢，无疑这是浪费性能的。
  
#### HashMap默认加载因子是多少?为什么是0.75，不是0.6或者0.8?
threshold = length(桶数组长度) * loadFactor

负载因子越大，阈值越大，发生哈希冲突的概率越大，增加插入和查询的时间。
负载因子越小，阈值越小，空间利用效率不高。

默认的loadFactor是0.75，0.75是对空间和时间效率的一个平衡选择，一般不要修改，除非在时间和空间比较特殊的情况下 ：
- 如果内存空间很多而又对时间效率要求很高，可以降低负载因子Load factor的值 
- 相反，如果内存空间紧张而对时间效率要求不高，可以增加负载因子loadFactor的值，这个值可以大于1。

#### HashMap中key的存储索引是怎么计算的?and 为什么 HashMap 容量总是 2 的幂？
key 对象的 hashCode 高16位 与 低16位 进行位异或操作（^），将这一扰动操作得到的
值 与 桶数组容量`n`减去1 进行与操作（&），这个操作等同于 扰动值除以`n`求余【要
满足该等同的条件是 `n`必须是 2 的 幂】
```
static final int hash(Object key) {   
     int h;
     return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    /* 
     h = key.hashCode() 为第一步：取hashCode值
     h ^ (h >>> 16)  为第二步：高位参与运算
    */
}
static int indexFor(int h, int length) {  //jdk1.7的源码，jdk1.8没有这个方法，但实现原理一样
     return h & (length-1);  //第三步：取模运算
}
```
这波操作的两个目的：
- 高位参与运算，增加扰动程度，避免 桶数组容量 `n` 太小，以至于等同于只能参与低位计算，
（高位全是零）
- `&` 求余操作是位操作，比`%`更快

#### HashMap 的 put 操作流程
- 1. 根据 key 计算其 hash 值
- 2. 检查桶数组容量，若为零调用 `resize()` 进行初始化。
- 3. 根据 key 的 hash 值计算存放的桶数组下标，若桶为空，直接生成节点放入；
- 4. 若桶不为空：若是链表，遍历链表，若存在相同的 key，直接覆盖就结束，若不存在将新节点
插入链表末尾(**尾插法，不同于 jdk7 的头插法**)，此时如果链表节点数大于**树化阈值**（默认位8）且数组长度大于等于 64，进行树化；若是红黑树，将节点挂
到树上
- 5. 以上完成之后，检查元素个数是否大于**阈值**，超过阈值调用 `resize()`扩容

![](http://blog-img.coolsen.cn/img/hashmap%E4%B9%8Bput%E6%96%B9%E6%B3%95.jpg)

#### HashMap 的扩容方式
会伴随着一次重新 hash 分配，并且会遍历 hash 表中所有的元素，
是非常耗时的。在编写程序中，要尽量避免 resize。

一般来说，扩容时，HashMap 的新容量是旧容量的两倍，阈值也会直接翻番，如果阈值过大，
将 阈值先置为 0，然后通过 `threshold = capacity * loadFactor` 计算

源码如下：
```
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        // 超过最大值就不再扩充了，就只好随你碰撞去吧
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        // 没超过最大值，就扩充为原来的2倍
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {
        // signifies using defaults
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    // 计算新的resize上限
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ? (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        // 把每个bucket都移动到新的buckets中
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else {
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        // 原索引
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        // 原索引+oldCap
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    // 原索引放到bucket里
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    // 原索引+oldCap放到bucket里
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
```
⚠️ 值得注意的是，元素根据其 hash 值进行再次分配时（仅以链表结构作为说明），
保留了节点原有的顺序，而且是根据条件 `(e.hash & oldCap) == 0` 分为两组元素组成的
两个链表，直接将两条链表的头节点放入对应的桶数组中。为什么会是这个条件呢？仔细动笔一算，
满足这个条件的节点不需要改变桶数组下标存储；不满足的就存入下标为 原下标 + oldCap 的位置。

#### 一般用什么作为HashMap的key?
一般用Integer、String 这种不可变类当 HashMap 当 key，而且 String 最为常用。
- 因为字符串是不可变的，所以在它创建的时候 hashcode 就被缓存了，不需要重新计算。这就是 HashMap 中的键往往都使用字符串的原因。
- 因为获取对象的时候要用到 equals() 和 hashCode() 方法，那么键对象正确的重写这两个方法是非常重要的,这些类已经很规范的重写了 hashCode() 以及 equals() 方法

#### HashMap 为什么线程不安全？
- 多线程情况下扩容出现死循环（循环链表），这种安全问题出现在 jdk 7，采用尾插法会出现该问题
```
void transfer(Entry[] newTable) {
        Entry[] src = table;                   //src引用了旧的Entry数组
        int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) { //遍历旧的Entry数组
            Entry<K,V> e = src[j];             //取得旧Entry数组的每个元素
            if (e != null) {
                src[j] = null;//释放旧Entry数组的对象引用（for循环后，旧的Entry数组不再引用任何对象）
                do {
                    Entry<K,V> next = e.next;
                    int i = indexFor(e.hash, newCapacity); //！！重新计算每个元素在数组中的位置
                    e.next = newTable[i]; //标记[1]
                    newTable[i] = e;      //将元素放在数组上
                    e = next;             //访问下一个Entry链上的元素
                } while (e != null);
            }
        }
    }
```
说明：[详细说明见此](https://coolshell.cn/articles/9606.html#Hash%E8%A1%A8%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84)

- 多线程的 put 可能导致元素的丢失：多线程同时进行 put 操作，如果计算出来的索引位置是相同的，那就会找成前一个 key 倍后一个 key 覆盖，从而导致元素的丢失。这在 jdk7 和 jdk8中都存在
- put 和 get 并发时，由于 put 执行后导致元素的个数超过 threshold 进行扩容，导致 键值对的移动，get 返回 null 值，在 jdk7 和 jdk8 都存在这个问题。

#### ConcurrentHashMap 的实现原理是什么?
**JDK7** 中的 ConcurrentHashMap 是由 `Segment` 数组结构和 `HashEntry` 数组结构组成，
即将原先的 `HashMap` 中的桶数组分成几个段，每个段都几个 `HashEntry` 数组组成。

`Segment`继承了 `ReentrantLock`，故 `Segment` 是一种可重入锁，扮演锁的角色。

![](http://blog-img.coolsen.cn/img/ConcurrentHashMap-jdk1.7.png)
首先将数据分为一段一段的存储，然后给每一段数据配一把锁，当一个线程占用锁访问其中一个段数据时，其他段的数据也能被其他线程访问，能够实现真正的并发访问。（准确的说是并行）

**JDK8** 的锁的粒度更小一点，选择采用与`HashMap`相同的 数组+链表+红黑树结构，在锁的实现上，采用 synchronized + cas 实现更低粒度的锁，将该锁级别控制在哈希桶元素的级别
，只需要锁住桶数组中这个链表 or 红黑树的头节点，大大提高了并发度

![](http://blog-img.coolsen.cn/img/ConcurrentHashMap-jdk1.8.png)

#### ConcurrentHashMap的put方法执行逻辑是什么?
- **JDK7**:
  - 1. 首先需要根据 hash 值计算出对应的 `Segment`，如果没有，则先初始化一个 `Segment`
  - 2. 找到对应的`Segment`,然后 先自旋获取锁，自旋次数达到 64 次，则调用 `lock()` 阻塞式获取锁。
  - 3. 计算获取 键 在 `Segment`的 table 中的下标，CAS 获取该下标的 键值对（即头节点），遍历该链表，如果该 key 已存在，直接覆盖。如果不存在，新建一个 `HashEntry`，头插法插入。
  - 4. 如果元素的数量大于阈值，则扩容
  - 5. 最后释放锁
  
- **JDK8**

1.根据 key 计算出 hashcode 。

2.判断是否需要进行初始化。

3.即为当前 key 定位出的 Node，如果为空表示当前位置可以写入数据，利用 CAS 尝试写入，失败则自旋保证成功。

4.如果当前位置的 `hashcode == MOVED` == -1,则需要进行扩容。

5.如果都不满足，则利用 synchronized 锁写入数据。

6.如果数量大于 TREEIFY_THRESHOLD 则要执行树化方法，在treeifyBin中会首先判断当前数组长度≥64时才会将链表转换为红黑树。

用 `synchronized`,是因为 Synchronized 锁引入锁升级策略后，性能不再是问题。

#### ConcurrentHashMap的get方法是否要加锁，为什么?
不需要加锁，因为 Node 的元素 val 和指针 next 是用 volatile 修饰的，在多线程环境下线程A修改结点的val或者新增节点的时候是对线程B可见的。

这也是它比其他并发集合比如Hashtable、用Collections.synchronizedMap()包装的HashMap安全效率高的原因之一。
```
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    //可以看到这些都用了volatile修饰
    volatile V val;
    volatile Node<K,V> next;
}
```
#### get方法不需要加锁与volatile修饰的哈希桶有关吗?
没有关系。哈希桶`table`用`volatile`修饰主要是保证在数组扩容的时候保证可见性。
```
static final class Segment<K,V> extends ReentrantLock implements Serializable {

    // 存放数据的桶
    transient volatile HashEntry<K,V>[] table;
```

#### ConcurrentHashMap的并发度是多少?
在JDK1.7中，并发度默认是16，这个值可以在构造函数中设置。如果自己设置了并发度，
ConcurrentHashMap 会使用大于等于该值的最小的2的幂指数作为实际并发度，也就是比如你设置的值是17，那么实际并发度是32。

#### ConcurrentHashMap迭代器是强一致性还是弱一致性?
与`HashMap`迭代器是强一致性不同，`ConcurrentHashMap` 迭代器是弱一致性。

`ConcurrentHashMap` 的迭代器创建后，就会按照哈希表结构遍历每个元素，但在遍历过程中，内部元素可能会发生变化，如果变化发生在已遍历过的部分，迭代器就不会反映出来，而如果变化发生在未遍历过的部分，迭代器就会发现并反映出来，这就是弱一致性。

这样迭代器线程可以使用原来老的数据，而写线程也可以并发的完成改变，更重要的，这保证了多个线程并发执行的连续性和扩展性，是性能提升的关键。

#### 快速失败和安全失败
- 快速失败（Fail-Fast）
  
  在用迭代器遍历一个集合对象时，如果遍历过程中对集合对象的内容进行了修改（增加、删除、修改），则会抛出Concurrent Modification Exception。
  - 原理：迭代器在遍历时直接访问集合中的内容，并且在遍历过程中使用一个 modCount 变量。集合在被遍历期间如果内容发生变化，就会改变modCount的值。每当迭代器使用hashNext()/next()遍历下一个元素之前，
  都会检测modCount变量是否为expectedmodCount值，是的话就返回遍历；否则抛出异常，终止遍历。
  - 注意：如果在改了modCount变量之后，同时也改了expectedmodCount，就不会触发该异常
  - java.util包下的集合类都是快速失败的
  
- 安全失败
  
  采用安全失败的集合容器，在遍历时并不直接在集合上面访问元素，而是遍历这个集合的拷贝，
  - 原理：由于迭代时是对拷贝的遍历，所以其他线程对原集合的修改不会被 迭代器检测到，不会触发 Concurrent Modification Exception
  - 注意：基于拷贝内容可以屏蔽掉异常，但是从迭代开始那一刻后原集合的改动不会被迭代器检测到
  - 场景：java.util.concurrent包下的容器都是安全失败，可以在多线程下并发使用，并发修改，比如：ConcurrentHashMap。


