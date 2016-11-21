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

    static public void      logErr(String format, Object ...params) { System.err.println(String.format(format, params)); }

    static public void      logErr(String str) {
        System.out.println(str);
    }
}
