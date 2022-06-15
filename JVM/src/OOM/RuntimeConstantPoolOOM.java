package OOM;

import java.util.HashSet;
import java.util.Set;

/**
 * JDK8 方法区由元空间实现，运行时常量池被移出方法区
 * JDK8 运行时常量池位于 堆 heap 中，限制 堆 最大容量，测试运行时常量池溢出
 * 参数; -Xmx6M
 */
public class RuntimeConstantPoolOOM {

    public static void main(String[] args) {
        // 使用 set 保持常量池的引用，避免 Full GC 回收常量池行为
        Set<String> stringSet = new HashSet<>();

        // 在short 范围足以让 6 mb的 PermSize 产生 OOM
        short i = 0;
        while (true){
            // intern 将 字符串常量放入常量池中
            stringSet.add(String.valueOf(i++).intern());
        }


    }
}
