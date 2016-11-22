import io.netty.channel.Channel;


/**
 * Created by sakiir on 20/11/16.
 */
public class                JCoincheUtils {
    static public void      writeAndFlush(Channel ch, Object obj) { ch.writeAndFlush(obj); }

    static public void      log(String format, Object ...params) {
        System.out.println(String.format(format, params));
    }

    static public void      log(String str) {
        System.out.println(str);
    }

    static public void      logInfo(String format, Object ...params) {
        System.out.println(String.format("\\x1B[34m" + format + "\\x1B[0m", params));
    }

    static public void      logInfo(String str) {
        System.out.println("\\x1B[34m" + str + "\\x1B[0m");
    }

    static public void      logSuccess(String format, Object ...params) {
        System.out.println(String.format("\\x1B[32m" + format + "\\x1B[0m", params));
    }

    static public void      logSuccess(String str) {
        System.out.println("\\x1B[32m" + str + "\\x1B[0m");
    }

    static public void      logWarning(String format, Object ...params) {
        System.out.println(String.format("\\x1B[33m" + format + "\\x1B[0m", params));
    }

    static public void      logWarning(String str) {
        System.out.println("\\x1B[33m" + str + "\\x1B[0m");
    }

    static public void      logError(String format, Object ...params) {
        System.out.println(String.format("\\x1B[31m" + format + "\\x1B[0m", params));
    }

    static public void      logError(String str) {
        System.out.println("\\x1B[31m" + str + "\\x1B[0m");
    }

    static public void      logStderr(String format, Object ...params) { System.err.println(String.format("\\x1B[31m" + format + "\\x1B[0m", params)); }

    static public void      logStderr(String str) {
        System.err.println("\\x1B[31m" + str + "\\x1B[0m");
    }
}
