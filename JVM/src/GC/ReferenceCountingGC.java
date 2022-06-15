package GC;

/**
 * 循环引用
 * 测试 JVM 是否采用引用计数GC算法
 * 参数：-XX:+PrintGCDetails 打印 GC 日志
 */
public class ReferenceCountingGC {

    public Object instance = null;

    private static final int _2MB = 2 * 1024 * 1024;
    private byte[] bytes = new byte[_2MB];


    public static void main(String[] args) {

        ReferenceCountingGC objectA = new ReferenceCountingGC();
        ReferenceCountingGC objectB = new ReferenceCountingGC();

        objectA.instance = objectB;
        objectB.instance = objectA;

        objectA = null;
        objectB = null;

        System.gc();
    }
}
