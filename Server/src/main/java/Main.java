/**
 * Created by sakiir on 11/11/16.
 */

public class                Main {
    public static void      main(String []args) {
        int                 port = 1337;

        if (args.length < 1) {
            System.out.println("[>] No Arguments .. Using Default Port [1337]");
        }

        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException e) {
            System.err.println("[-] Failed to parse int : " + args[0]);
            System.out.println("[>] No Arguments .. Using Default Port [1337]");
            port = 1337;
        }
        JCoincheServer server = new JCoincheServer(port);
        server.run();
    }
}
