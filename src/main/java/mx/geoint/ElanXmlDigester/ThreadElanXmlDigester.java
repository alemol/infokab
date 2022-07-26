package mx.geoint.ElanXmlDigester;
import java.io.IOException;
import java.util.Date;
import java.util.Queue;
import java.util.LinkedList;

public class ThreadElanXmlDigester extends Thread{
    Queue<ElanXmlDigester> elanXmlDigester = new LinkedList<>();

    public void run(){
        while(true){
            System.out.println("RUN TREAHD: "+ elanXmlDigester.size() + " : " + Thread.currentThread().getName());
            if(elanXmlDigester.isEmpty()){
                deactivate();
            } else {
                process();
            }
        }
    }

    public void add(String pathEAF, String pathMultimedia, String uuid){
        elanXmlDigester.add(new ElanXmlDigester(pathEAF, pathMultimedia, uuid));
    }

    public void deactivate(){
        synchronized (this) {
            try {
                System.out.println("Thread deactivate");
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void activate(){
        synchronized (this) {
            System.out.println("Thread activate");
            this.notify();
        }
    }

    public void process(){
        try {
            Date startDate = new Date();
            ElanXmlDigester currentElanXmlDigester = elanXmlDigester.poll();
            currentElanXmlDigester.parse_tier("oracion", true, true);

            Date endDate = new Date();
            long difference_In_Time = endDate.getTime() - startDate.getTime();
            long difference_In_Seconds = (difference_In_Time / (1000)) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            System.out.println("TIMER FINISHED THREAD: "+ difference_In_Seconds +"s " + difference_In_Minutes+"m");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
