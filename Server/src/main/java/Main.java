/**
 * Created by sakiir on 11/11/16.
 */

public class Main {
    public static void main(String []args) {
        System.out.println("Hey I am the server!");

        JCoincheServer server = new JCoincheServer(1337);

        server.run();
    }
}
