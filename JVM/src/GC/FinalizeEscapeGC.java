package GC;

/**
 * 对象生存或死亡？
 * 需要多次标记，判别是否死亡
 * 1.可达性分析
 * 2.判断是否执行或没有覆盖 finalize(),若是，直接回收；若否，则丢进 F-Queue 执行他们的 finalize() 并标记
 * 3. F-Queue 执行完 finalize()后，第二次就将他们加入 即将移出 的集合待移出
 *
 * 这里展示一个对象在被 GC 时的一次自救：how does it save itself ? 覆盖 finalize 方法，实现再次被 GCRoots Chain 引用
 */
public class FinalizeEscapeGC {

    // 抓住 救命稻草！
    private static FinalizeEscapeGC SAVE_HOOK = null;

    // 存活信号
    public void isAlive(){
        System.out.println("i am alive :)");
    }

    // 覆盖 finalize ，实现一次自救
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize executed !");
        // 实现自救的关键步骤
        SAVE_HOOK = this;
    }


    public static void main(String[] args) throws InterruptedException {

        SAVE_HOOK = new FinalizeEscapeGC();

        //第一次回收时的自救
        SAVE_HOOK = null;
        System.gc();

        // Finalizer 线程优先度低，所以主线程休息 0.5 s
        Thread.sleep(500);

        if(SAVE_HOOK != null){
            SAVE_HOOK.isAlive();
        }else {
            System.out.println("i am dead :(");
        }

        // 第二次回收因为先前已经执行了finalize方法，无法自救
        SAVE_HOOK = null;
        System.gc();

        // Finalizer 线程优先度低，所以主线程休息 0.5 s
        Thread.sleep(500);

        if(SAVE_HOOK != null){
            SAVE_HOOK.isAlive();
        }else {
            System.out.println("i am dead :(");
        }

    }
}
