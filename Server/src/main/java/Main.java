import java.net.BindException;

/**
 * Created by sakiir on 11/11/16.
 */

public class                Main {
    public static void      main(String []args) {
        int                 port = 1337;

        if (args.length < 1) {
            JCoincheUtils.log(JCoincheConstants.log_using_default_port);
        }

        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException e) {
//            JCoincheUtils.logErr(JCoincheConstants.log_failed_parse_int, args[0]);
            JCoincheUtils.log(JCoincheConstants.log_using_default_port);
            port = 1337;
        }


        JCoincheServer server = new JCoincheServer(port);
        try {
            server.run();
        } catch (Exception e) {
            JCoincheUtils.logStderr("[-] Received Exception From Server returned message is : " + e.getMessage());
            if (e instanceof BindException) {
                JCoincheUtils.logStderr(JCoincheConstants.log_failed_bind, port);
            }
            System.exit(84);
        }
    }
}
