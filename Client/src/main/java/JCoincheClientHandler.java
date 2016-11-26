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
     * @param clientProcess ClientProcess Object
     * @see ClientProcess
     */
    public                  JCoincheClientHandler(ClientProcess clientProcess) {
        this.clientProcess = clientProcess;
    }

    /**
     * This callback is called when the
     * client is disconnected from server.
     *
     * @param ctx Channel Context
     * @throws Exception When disconnection is crashing
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
     * @param ctx Channel Context
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
     * @param ctx Channel Context
     * @param msg Message Received
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
     * @param ctx Channel Context
     */
    @Override
    public void             channelReadComplete(ChannelHandlerContext ctx) { ctx.flush(); }

    /**
     * This callback is called when a Client
     * relative exception is caught.
     *
     * @param ctx Channel Context
     * @param cause The cause object
     * @throws Exception when netty throws it
     */
    @Override
    public void             exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
