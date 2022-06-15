package OOM;

/**
 * 测试 栈帧过大导致 StackOverFlowError
 * 参数 -Xss128k
 */
public class JavaVMSOF2 {

    private static int length = 0;

    public void test(){
        long unset0,unset1,unset2,unset3,unset4,
                unset5,unset6,unset7,unset8,unset9,
                unset10,unset11,unset12,unset13,unset14,
                unset15,unset16,unset17,unset18,unset19,
                unset20,unset21,unset22,unset23,unset24,
                unset25,unset26,unset27,unset28,unset29,
                unset30,unset31,unset32,unset33,unset34,
                unset35,unset36,unset37,unset38,unset39,
                unset40,unset41,unset42,unset43,unset44,
                unset45,unset46,unset47,unset48,unset49,
                unset50,unset51,unset52,unset53,unset54,
                unset55,unset56,unset57,unset58,unset59,
                unset60,unset61,unset62,unset63,unset64,
                unset65,unset66,unset67,unset68,unset69,
                unset70,unset71,unset72,unset73,unset74,
                unset75,unset76,unset77,unset78,unset79,
                unset80,unset81,unset82,unset83,unset84,
                unset85,unset86,unset87,unset88,unset89,
                unset90,unset91,unset92,unset93,unset94,
                unset95,unset96,unset97,unset98,unset99,unset100;

        length ++;
        test();

        unset0=unset1=unset2=unset3=unset4=
        unset5=unset6=unset7=unset8=unset9=
        unset10=unset11=unset12=unset13=unset14=
        unset15=unset16=unset17=unset18=unset19=
        unset20=unset21=unset22=unset23=unset24=
        unset25=unset26=unset27=unset28=unset29=
        unset30=unset31=unset32=unset33=unset34=
        unset35=unset36=unset37=unset38=unset39=
        unset40=unset41=unset42=unset43=unset44=
        unset45=unset46=unset47=unset48=unset49=
        unset50=unset51=unset52=unset53=unset54=
        unset55=unset56=unset57=unset58=unset59=
        unset60=unset61=unset62=unset63=unset64=
        unset65=unset66=unset67=unset68=unset69=
        unset70=unset71=unset72=unset73=unset74=
        unset75=unset76=unset77=unset78=unset79=
        unset80=unset81=unset82=unset83=unset84=
        unset85=unset86=unset87=unset88=unset89=
        unset90=unset91=unset92=unset93=unset94=
        unset95=unset96=unset97=unset98=unset99=unset100=0;

    }

    public static void main(String[] args) {
        JavaVMSOF2 javaVMSOF2 = new JavaVMSOF2();
        try {
            javaVMSOF2.test();
        }catch (Throwable e){
            System.out.println("stackLength:" + length);
            throw e;
        }

    }
}
