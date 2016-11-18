/**
 * Created by sakiir on 09/11/16.
 */

public class            Main {
    public static void  main(String []args){
        int             port;
        String          host;

        if (args.length < 2) {
            System.out.println("[>] Usage : java -jar jcoinche-client.jar HOST PORT");
            return;
        }
        try {
            port = Integer.parseInt(args[1]);
            host = args[0];
        } catch (NumberFormatException e) {
            System.err.println("[-] Failed to parse int : " + args[1]);
            return;
        }
    }
}
