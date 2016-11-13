/**
 * Created by sakiir on 11/11/16.
 */

public class Main {
    public static void main(String []args) {
        System.out.println("Hey I am the server!");

        Thread mainServerThread = new Thread(new JCoincheServer(1339));

        mainServerThread.start();
    }
}
