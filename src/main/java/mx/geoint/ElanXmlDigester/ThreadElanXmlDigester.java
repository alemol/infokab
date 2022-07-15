package mx.geoint.ElanXmlDigester;
import java.io.IOException;
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

    public void add(String pathEAF, String uuid){
        elanXmlDigester.add(new ElanXmlDigester(pathEAF, uuid));
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
            ElanXmlDigester currentElanXmlDigester = elanXmlDigester.poll();
            currentElanXmlDigester.parse_tier("oracion", true, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
