public class StringInternTest {
    public static void main(String[] args) {

        StringBuilder sb = new StringBuilder();
        String jv = sb.append("ja").append("va").toString();
        System.out.println(jv.intern() == jv);  // false

        String jv2 = new String("java");
        System.out.println(jv == jv2);  // false

        StringBuilder sb2 = new StringBuilder();
        String s = sb2.append("a").append("bc").toString();
        System.out.println(s.intern() == s);   // true
    }

}
