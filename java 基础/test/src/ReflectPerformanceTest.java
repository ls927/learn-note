import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 测试 反射性能：创建对象
 */
public class ReflectPerformanceTest {


    static long getTimeDiff(long start){
        return System.currentTimeMillis() - start;
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        long num = 100000000;

        // reflect
        Class bClass = B.class;
        long reflectStart = System.currentTimeMillis();
        for(long i = 0L; i < num; i ++){
            bClass.newInstance();
        }
        System.out.println("reflect 耗时" + getTimeDiff(reflectStart) + "ms");

        long normalStart = System.currentTimeMillis();
        for(long j = 0L; j < num; j ++){
            new B();
        }
        System.out.println("normal 耗时" + getTimeDiff(normalStart) + "ms");


    }

}

class B{
}