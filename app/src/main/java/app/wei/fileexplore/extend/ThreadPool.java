package app.wei.fileexplore.extend;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wei on 2017/7/19.
 */
public class ThreadPool {
    public final static int  STATE_RUN=1,STATE_STOP=0,STATE_SUSPEND=2;
    private final static int DEFAULT_SIZE=1;
    private LinkedBlockingQueue<pThread> ThreadQueue;
    private LinkedBlockingQueue<Runnable> workerQueue;
    private int size;
    private int pos;
    public ThreadPool(){
        this(DEFAULT_SIZE);
    }
    public ThreadPool(int size){
        if (size<1||size>16){
            size=DEFAULT_SIZE;
        }
        pos=0;
        this.size=size;
        workerQueue=new LinkedBlockingQueue<>();
        ThreadQueue=new LinkedBlockingQueue<>();
        for (int i = 0; i < size; i++) {
            pThread thread=new pThread();
            thread.start();
            ThreadQueue.add(thread);
        }
    }

    public  void execute(Runnable runnable){
        workerQueue.add(runnable);
        if (ThreadQueue.size()!=0){
            ThreadQueue.remove().execute();
        }
    }

    public boolean isFree(){
        return workerQueue.isEmpty();
    }

    public void stop(){
        for (pThread thread:ThreadQueue){
            thread.setState(STATE_STOP);
        }
    }

    public class pThread extends Thread{
        private int state;// 0退出,1运行,2挂起

        public pThread(){
            state=STATE_SUSPEND;
        }

        public void setState(int newstate){
            synchronized (this) {
                this.notifyAll();
                state=newstate;
            }
        }

        public boolean isFree(){
            return state==STATE_SUSPEND;
        }

        public int State(){
            return state;
        }

        public void execute(){
            this.setState(STATE_RUN);
        }

        @Override
        public void run() {
            try {
            while (state!=STATE_STOP){
                if (workerQueue.isEmpty()||state==STATE_SUSPEND){
                    state=STATE_SUSPEND;
                    synchronized (this){
                        ThreadQueue.add(this);
                        this.wait();
                    }
                }
                if (workerQueue.size()!=0) {
                    workerQueue.remove().run();
                }
            }
            workerQueue=null;
            } catch (Exception e) {
                Log.e("Tag","thread pool error");
            }
        }
    }
}
