import io.netty.channel.ChannelHandlerContext;

/**
 * Created by sakiir on 25/11/16.
 */

/**
 * This class is an implementation of Runnable which is
 * a Thread instance in the Client-size.
 * It is checking each second the connection status to
 * know if the client is still connected to the server.
 * It can be used to know if the client has been disconnected
 * from the server.
 *
 *  @see JCoincheClientHandler
 *  @see Runnable
 *  @see Thread
 */
public class                    ActivityChecker implements Runnable {

    ChannelHandlerContext       ctx;
    boolean                     isRunning = true;

    public ActivityChecker(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    /**
     * This method and infinite loop and check
     * the status of the client.
     *
     * @see JCoincheClientHandler
     * @see Thread#sleep(long)
     */
    @Override
    public void                 run() {
        while (this.isRunning) {
            if (this.ctx.isRemoved()) {
                JCoincheUtils.logWarning("[!] You have been disconnected ! May be the game is over ?");
                ctx.close();
                System.exit(0);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
