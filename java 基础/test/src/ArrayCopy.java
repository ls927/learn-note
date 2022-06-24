public class ArrayCopy {

    public static void main(String[] args) {
        Integer[] a = {1,2,3,4};
        Integer[] clone = a.clone();
        clone[1] = 100;
        for (Integer i :
                a) {
            System.out.println(i);
        }

        // 1 2 3 4

    }
}
