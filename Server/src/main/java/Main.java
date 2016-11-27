import java.net.BindException;

/**
 * Created by sakiir on 11/11/16.
 */

/**
 * This class is the Server main class.
 * This class is getting command line args to retriever
 * port.
 * If the port is not specified it is getting the default one (1337)
 *
 * @see JCoincheServer
 */
public class                Main {
    /**
     * Entry point
     *
     * @param args Command line args.
     */
    public static void      main(String []args) {
        int                 port = 1337;

        if (args.length < 1) {
            JCoincheUtils.logWarning(JCoincheConstants.project_usage);
            JCoincheUtils.log(JCoincheConstants.log_using_default_port);
        }

        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException e) {
            JCoincheUtils.logWarning(JCoincheConstants.project_usage);
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
