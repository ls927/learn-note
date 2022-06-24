import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * 测试：反射安全问题
 * 利用反射 改变字符串对象
 */
public class ReflectChangeString {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        String s = "123";
        Class<? extends String> aClass = s.getClass();

        Field value = aClass.getDeclaredField("value"); // 访问 String 私有属性
        value.setAccessible(true); // 设置私有属性可获取并修改

        char[] chars = (char[]) value.get(s);
        chars[1] = 'a';

        System.out.println(s);  // 1a3 原来声称不可变对象 String 在 反射 一番操作下居然也变了

        // 同理，这样利用可以实现 真正的 字符串互换 swap

    }

    

}

