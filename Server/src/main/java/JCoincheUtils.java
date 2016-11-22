import io.netty.channel.Channel;

/**
 * Created by sakiir on 20/11/16.
 */
public class                JCoincheUtils {
    static public void      writeAndFlush(Channel ch, Object obj) { if (GameThread.isRunning) ch.writeAndFlush(obj); }

    static public void      log(String format, Object ...params) {
        System.out.println(String.format(format, params));
    }

    static public void      log(String str) {
        System.out.println(str);
    }

    static public void      logInfo(String format, Object ...params) {
        System.out.println(String.format("\033[34m" + format + "\033[0m", params));
    }

    static public void      logInfo(String str) {
        System.out.println("\033[34m" + str + "\033[0m");
    }

    static public void      logSuccess(String format, Object ...params) {
        System.out.println(String.format("\033[32m" + format + "\033[0m", params));
    }

    static public void      logSuccess(String str) {
        System.out.println("\033[32m" + str + "\033[0m");
    }

    static public void      logWarning(String format, Object ...params) {
        System.out.println(String.format("\033[33m" + format + "\033[0m", params));
    }

    static public void      logWarning(String str) {
        System.out.println("\033[33m" + str + "\033[0m");
    }

    static public void      logError(String format, Object ...params) {
        System.out.println(String.format("\033[31m" + format + "\033[0m", params));
    }

    static public void      logError(String str) {
        System.out.println("\033[31m" + str + "\033[0m");
    }

    static public void      logStderr(String format, Object ...params) { System.err.println(String.format("\033[31m" + format + "\033[0m", params)); }

    static public void      logStderr(String str) {
        System.err.println("\033[31m" + str + "\033[0m");
    }
}
