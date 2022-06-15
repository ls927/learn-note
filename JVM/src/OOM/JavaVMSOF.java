package OOM;

/**
 * 测试 栈帧过多超过栈深度导致 StackOverflowError
 * 参数 -Xss128k
*/

public class JavaVMSOF {

    private static int length = 0;

    public void test(){
        length ++;
        test();
    }

    public static void main(String[] args) throws Throwable {

        JavaVMSOF javaVMSOF = new JavaVMSOF();
        try {
            javaVMSOF.test();
        }catch (Throwable e){
            System.out.println("stackLength:" + length);
            throw e;
        }
        
    }




}
