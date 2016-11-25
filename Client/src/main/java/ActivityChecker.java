import io.netty.channel.ChannelHandlerContext;

/**
 * Created by sakiir on 25/11/16.
 */
public class                    ActivityChecker implements Runnable {

    ChannelHandlerContext       ctx;
    boolean                     isRunning = true;

    public ActivityChecker(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void                 run() {
        while (this.isRunning) {
            if (this.ctx.isRemoved()) {
                JCoincheUtils.logWarning("[!] Server is not responding .. probably OFF :-(");
                ctx.close();
                System.exit(84);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
