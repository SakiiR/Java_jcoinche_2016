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
        server.run();
    }
}
