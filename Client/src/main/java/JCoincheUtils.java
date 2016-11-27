import io.netty.channel.Channel;


/**
 * Created by sakiir on 20/11/16.
 */

/**
 * This class is only containing static method.
 * They are printing a message in a different way on the console with formaters.
 * It also contains the writeAndFlush encapsulation to send message over the Socket.
 *
 * @see JCoincheClientHandler
 */
public class                JCoincheUtils {
    /**
     * Write a message on the channel
     *
     * @param ch Player Channel
     * @param obj Message Object to send
     */
    static public void      writeAndFlush(Channel ch, Object obj) { if (ch != null) ch.writeAndFlush(obj); }

    /**
     * Log a basic message to the console with var_args
     *
     * @param format Format String
     * @param params var_arg list
     */
    static public void      log(String format, Object ...params) {
        System.out.println(String.format(format, params));
    }

    /**
     * Log a basic message to the console
     *
     * @param str Message string
     */
    static public void      log(String str) {
        System.out.println(str);
    }

    /**
     * Log a blue message to the console with var_args
     *
     * @param format Format String
     * @param params var_arg list
     */
    static public void      logInfo(String format, Object ...params) {
        System.out.println(String.format("\033[34m" + format + "\033[0m", params));
    }

    /**
     * Log a blue message to the console
     *
     * @param str Message string
     */
    static public void      logInfo(String str) {
        System.out.println("\033[34m" + str + "\033[0m");
    }

    /**
     * Log a green message to the console with var_args
     *
     * @param format Format String
     * @param params var_arg list
     */
    static public void      logSuccess(String format, Object ...params) {
        System.out.println(String.format("\033[32m" + format + "\033[0m", params));
    }

    /**
     * Log a green message to the console
     *
     * @param str Message string
     */
    static public void      logSuccess(String str) {
        System.out.println("\033[32m" + str + "\033[0m");
    }

    /**
     * Log an orange message to the console with var_args
     *
     * @param format Format String
     * @param params var_arg list
     */
    static public void      logWarning(String format, Object ...params) {
        System.out.println(String.format("\033[33m" + format + "\033[0m", params));
    }

    /**
     * Log an orange message to the console
     *
     * @param str Message string
     */
    static public void      logWarning(String str) {
        System.out.println("\033[33m" + str + "\033[0m");
    }

    /**
     * Log a red message to the console with var_args
     *
     * @param format Format String
     * @param params var_arg list
     */
    static public void      logError(String format, Object ...params) {
        System.out.println(String.format("\033[31m" + format + "\033[0m", params));
    }

    /**
     * Log a red message to the console
     *
     * @param str Message string
     */
    static public void      logError(String str) {
        System.out.println("\033[31m" + str + "\033[0m");
    }

    /**
     * Log a red message to the error output of the console with var_args
     *
     * @param format Format String
     * @param params var_arg list
     */
    static public void      logStderr(String format, Object ...params) { System.err.println(String.format("\033[31m" + format + "\033[0m", params)); }

    /**
     * Log a red message to the error output of the console
     *
     * @param str Message string
     */
    static public void      logStderr(String str) {
        System.err.println("\033[31m" + str + "\033[0m");
    }
}
