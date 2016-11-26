import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;

/**
 * Created by sakiir on 18/11/16.
 */

/**
 * This class is the main client controller netty one.
 * It contains callback that is called when an event is throwed
 * by the netty main EventLoop.
 *
 * @see JCoincheClient
 * @see ChannelInboundHandlerAdapter
 */
public class                JCoincheClientHandler extends ChannelInboundHandlerAdapter {

    ClientProcess           clientProcess = null;
    ActivityChecker         activityChecker = null;

    /**
     * Handler constructor.
     * ClientProcess as parameters.
     *
     * @param clientProcess
     * @see ClientProcess
     */
    public                  JCoincheClientHandler(ClientProcess clientProcess) {
        this.clientProcess = clientProcess;
    }

    /**
     * This callback is called when the
     * client is disconnected from server.
     *
     * @param ctx
     * @throws Exception
     * @see ActivityChecker
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
        ctx.close();
    }

    /**
     * This callback is called when the
     * client is connected to the server.
     * This is the method where the
     * ClientProcess Thread is started.
     *
     * @param ctx
     * @see ClientProcess
     */
    @Override
    public void             channelActive(ChannelHandlerContext ctx) {
        JCoincheUtils.logInfo(JCoincheConstants.log_client_connected);
        this.activityChecker = new ActivityChecker(ctx);
        new Thread(this.activityChecker).start(); // Check for server activity
        new Thread(this.clientProcess).start();
    }

    /**
     * This callback is called when a Message Object is received.
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void             channelRead(ChannelHandlerContext ctx, Object msg) {
        JCoincheProtocol.JCoincheMessage req = (JCoincheProtocol.JCoincheMessage) msg;
        // Debuging
        // JCoincheUtils.logSuccess("[+] Received Packet of Size(%d) of Type(%s)", req.getSerializedSize(), req.getType());
        this.clientProcess.addMessage(req);
    }

    /**
     * This callback is called when the client
     * has finished to read on the socket.
     *
     * @param ctx
     */
    @Override
    public void             channelReadComplete(ChannelHandlerContext ctx) { ctx.flush(); }

    /**
     * This callback is called when a Client
     * relative exception is caught.
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void             exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
