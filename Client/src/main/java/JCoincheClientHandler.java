import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by sakiir on 18/11/16.
 */
public class                JCoincheClientHandler extends ChannelInboundHandlerAdapter{

    ClientProcess           clientProcess = null;

    public                  JCoincheClientHandler(ClientProcess clientProcess) {
        this.clientProcess = clientProcess;
    }

    @Override
    public void             channelActive(ChannelHandlerContext ctx) {
        System.out.println("[>] Connected!");
    }

    @Override
    public void             channelRead(ChannelHandlerContext ctx, Object msg) {
        JCoincheProtocol.JCoincheMessage req = (JCoincheProtocol.JCoincheMessage) msg;

        System.out.println(String.format("[>] Message Received {type : \"%s\"}", req.getType()));
        /*
        if (req.getType() === TYPE_GAME_START) {
            StartClient(Process)
         }
         */
    }

    @Override
    public void             channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("[>] Channel Read Complete!");
        ctx.flush();
    }

    @Override
    public void             exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
