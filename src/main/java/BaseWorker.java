import java.util.concurrent.CountDownLatch;

public abstract class BaseWorker extends Thread {

    protected int loop;
    protected CountDownLatch latch;

    public BaseWorker(int loop, CountDownLatch latch) {
        this.loop = loop;
        this.latch = latch;
    }

    abstract void doTest();

    @Override
    public void run() {
        for (int i  = 0; i < loop; i++) {
            System.out.print(".");
            doTest();
        }
        latch.countDown();
    }


}
