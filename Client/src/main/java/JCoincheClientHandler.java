import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;

/**
 * Created by sakiir on 18/11/16.
 */
public class                JCoincheClientHandler extends ChannelInboundHandlerAdapter{

    ClientProcess           clientProcess = null;

    public                  JCoincheClientHandler(ClientProcess clientProcess) {
        this.clientProcess = clientProcess;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
        ctx.close();
    }

    @Override
    public void             channelActive(ChannelHandlerContext ctx) {
        JCoincheUtils.logInfo(JCoincheConstants.log_client_connected);
        new Thread(this.clientProcess).start();
    }

    @Override
    public void             channelRead(ChannelHandlerContext ctx, Object msg) {
        JCoincheProtocol.JCoincheMessage req = (JCoincheProtocol.JCoincheMessage) msg;
        // Debuging
        // JCoincheUtils.logSuccess("[+] Received Packet of Size(%d) of Type(%s)", req.getSerializedSize(), req.getType());
        this.clientProcess.addMessage(req);
    }

    @Override
    public void             channelReadComplete(ChannelHandlerContext ctx) { ctx.flush(); }

    @Override
    public void             exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            JCoincheUtils.logWarning("[-] Server Timeout !");
            System.exit(84);
        }
        cause.printStackTrace();
        ctx.close();
    }
}
