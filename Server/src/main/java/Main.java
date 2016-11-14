/**
 * Created by sakiir on 11/11/16.
 */

public class Main {
    public static void main(String []args) {
        Thread mainServerThread = new Thread(new JCoincheServer(1337));
        mainServerThread.start();
    }
}
