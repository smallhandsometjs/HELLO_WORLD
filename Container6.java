import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
/*使用门阀来限制，当countdownLatch的count=0或者说调用catdown的时候解除所有限制，比如门只有一个钥匙，一开始给T2,后来转交给T1，当
* 到达某个条件时，我直接把门给拆了，所有的线程都不受了限制。*/
public class Container6 {

    volatile List lists = new ArrayList();
    public void  addObj(Object o) {lists.add(o);}
    public int  getSize() { return lists.size();}

    public static void main(String[] args) {
        Container6 container6 = new Container6();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(()->{
            System.out.println("T2启动1");

                if(container6.getSize()!=5) {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("T2结束");

        }).start();


        new Thread(()->{
                System.out.println("T1启动");
                for(int i=1;i<10;i++) {
                    container6.addObj(new Object());
                    System.out.println(i);
                    if(i==5) {
                        latch.countDown();

                }
                }

        }).start();

    }

}
