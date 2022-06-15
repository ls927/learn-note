package OOM;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * JDK8 测试*直接内存*所导致的 OOM
 * 参数： -Xmx20M -XX:MaxDirectMemorySize=10M
 * 采用 Unsafe:allocateMemory() 直接分配内存
 */
public class DirectMemoryOOM {

    private static long _1M = 1024 * 1024 * 1;

    public static void main(String[] args) throws Exception{
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe)unsafeField.get(null);
        while (true){
            unsafe.allocateMemory(_1M);
        }
    }
}
