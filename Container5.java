
import java.util.ArrayList;
import java.util.List;
/*这种写法是先在t2执行，然后由于不满足条件就让它先等待，所以就去执行了t1,在t1到达条件的时候唤醒T2,此时如果只唤醒但是不会把锁解开
* 如果不让在t1中wait就会出现先让t1执行完再去执行t2，所以我们现在是唤醒之后再让t1等待，等t2执行结束再唤醒t1*/
public class Container5 {

    volatile List lists = new ArrayList();
    public void  addObj(Object o) {lists.add(o);}
    public int  getSize() { return lists.size();}

    public static void main(String[] args) {
        Container5 container5 = new Container5();
        Object lock = new Object();
        new Thread(()->{
            System.out.println("T2启动1");
            synchronized (lock) {

                if(container5.getSize()!=5) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("T2结束");
                lock.notify();
            }
        }).start();


        new Thread(()->{
            synchronized (lock) {
                System.out.println("T1启动");
                for(int i=1;i<10;i++) {
                    container5.addObj(new Object());
                    System.out.println(i);
                    if(i==5) {
                        lock.notify();

                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }

}
