# 集合

## 集合概述
Java 集合，也称作容器。主要由两大接口派生：`Collection` 和 `Map`；`Collection` 主要
用来存储单一元素，`Map` 主要用于存储 键值对。`Collection` 有三大子接口：`List`,`Set`
和`Queue`。

![](https://javaguide.cn/assets/java-collection-hierarchy.f0b5a55e.png)

## List, Set, Queue 和 Map 四者的区别？
- `List`（对付顺序的好帮手）：存储的元素是有序的，可重复的。
- `Set`（存储的不重复的元素）：存储的元素无序，不可重复。
- `Queue`（实现排队功能的叫号机）：按特定的排队规则来确定先后顺序，存储的元素是有序的，可重复的
- `Map` （存储键值对，用 key 来检索）：key 无序，不可重复的；而 value 是无序的，可重复的。一个
每个键最多映射到一个值。

## 集合框架底层数据结构总结
`Collection`

🚩 List
- `ArrayList`: `Object[ ]`数组
- `Vector`: `Object[ ]`数组
- `LinkedList`: 双向链表

🚩 Set
- `HashSet`: 基于`HashMap`实现，底层`HashMap`的 key 来存储元素
- `LinkedHashSet`: `LinkedHashSet` 是 `HashSet` 的子类，并且其内部是通过 `LinkedHashMap 来实现的。有点类似于我们之前说的 LinkedHashMap` 其内部是基于 `HashMap` 实现一样，不过还是有一点点区别的
- `TreeSet`: 有序且唯一，基于红黑树（自平衡的排序二叉树） [红黑树介绍]

🚩 Queue
- `PriorityQueue`: `Object[]` 数组来实现二叉堆 [二叉堆]
- `ArrayQueue` : `Object[]` 数组 + 双指针

`Map`

🚩 `HashMap`: `Object[]`数组 + 链表 or 红黑树，当链表的长度大于 8，且数组长度大于 64
（若不满足，则增大数组的容量），链表转化为红黑树，以减少搜索时间。

🚩 `LinkedHashMap`: 继承自`HashMap`，在这之上加了双向链表，
使得上面的结构可以保持键值对的插入顺序。同时通过对链表进行相应的操作，实现了访问顺序相关逻辑

🚩 `HashTable`: 数组加链表实现，数组为主体，链表解决哈希冲突

🚩 `TreeMap`: 红黑树

#### 为什么要使用集合?
优点：
- 可存储多种类型
- 可变长
- 可保存具有映射关系的数据

## Collection 子接口之 List
#### ArrayList 和 Vector 的区别？
`ArrayList` 和 `Vector` 都是基于 `Object[]` 存储，`ArrayList` 适用于频繁查找的工作，但是线程不安全。`Vector`比较古老，线程安全。在扩容时，`ArrayList` 是0.5倍，`Vector` 是 1 倍。


#### ArrayList 与 LinkedList 的区别？
1.是否保证线程安全： `ArrayList` 和 `LinkedList` 都是线程不安全的

2.底层数据结构：`ArrayList` 底层使用的是`Object[]`；而 `LinkedList`  使用的双向链表数据结构

3.插入和删除是否受元素位置影响：
- `ArrayList` 会受元素的位置影响
- `LinkedList` 头尾插入不受位置影响，其他都受

4.是否支持随机访问：`LinkedList` 不支持，`ArrayList`支持。

5.内存空间占用：`ArrayList` 的空间浪费主要体现在会预留一定的容量空间，而`LinkedList` 主要在于存储前后节点的引用。

一般来说，第一选择是 `ArrayList`，`LinkedList`？它的作者都不用！

> 补充内容：RandomAccess 接口
> RandomAccess 接口中无定义内容，ArrayList 实现了它，只是为了标识实现该接口的支持随机访问，为了其他方法在调用 Collection 时，能够区别处理

#### ArrayList 的扩容机制？


#### 






