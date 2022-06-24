/**
 * 获取 Class 对象的四种方法
 */
public class GetClass {

    int i;

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        // 1.
        Class<GetClass> getClassClass = GetClass.class;

        // 2.
        Class<?> getClass = Class.forName("GetClass");

        //3.
        GetClass g = new GetClass();
        Class<? extends GetClass> aClass = g.getClass();

        //4.
        Class<?> getClass1 = ClassLoader.getSystemClassLoader().loadClass("GetClass");
    }

}
