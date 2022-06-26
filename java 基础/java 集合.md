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



