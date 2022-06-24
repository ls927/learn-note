import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 测试：反射的不安全性
 * 在 ArrayList<Integer> 插入 String 类型的元素，逃避编译期的泛型检查
 */
public class UnsafeReflect {


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        ArrayList<Integer> arrayList = new ArrayList<>();

        // arrayList.add("String");  常规插入，无法通过泛型安全检查。

        // 利用反射插入，逃避泛型安全检查,实现不合法的插入
        Class<? extends ArrayList> aClass = arrayList.getClass();
        Method method = aClass.getMethod("add", Object.class);
        method.invoke(arrayList,"String");
        System.out.println(arrayList);

    }



}
