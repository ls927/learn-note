import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class demo1 {

    static int _1M = 1024 * 1024 * 1;
    static int _1G = 1024 * 1024 * 1024;
    static int _6M = 1024 * 1024 * 6;
    static int _4M = 1024 * 1024 * 4;

    // 20M
    public static void main(String[] args) throws IOException, InterruptedException {

        List<byte[]> list = new ArrayList<>();
        for(int i = 0; i < 3; i ++){
            list.add(new byte[_6M]);
            for(byte[] s : list){
                System.out.print(s + " ");
            }
            System.out.println();
        }



    }

}
