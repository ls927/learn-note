package OOM;

/**
 * JDK8 测试 String:intern()
 */
public class StringInternTest {

    public static void main(String[] args) {

        /**
         * test1
         */
//        String s = "lso";
//        String intern = s.intern();
//        System.out.println(s == intern); // true

        /**
         * test2
         */
//        String str = new String("lso");
//        String str_intern = str.intern();
//        String string = "lso";
//
//        System.out.println(str == str_intern); // false
//        System.out.println(str_intern == string); //true

        /**
         * test3
         */
//        String s = "ja" + "va";
//        String s1 = "java";
//        System.out.println(s == s1); // true
//
//        String s2 = new String("java");
//        System.out.println(s1 == s2); //false
//
//        String intern = s.intern();
//        System.out.println(intern == s); // true

        /**
         * test4
         * JDK8 String:append()
         */
        String str = new StringBuilder("计算机").append("软件").toString();
        System.out.println(str.intern() == str); // true

        String str1 = new StringBuilder("ja").append("va").toString();
        System.out.println(str1.intern() == str1); //false

    }
}
