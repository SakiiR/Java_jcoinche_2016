import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by sakiir on 18/11/16.
 */
public class                JCoincheClientHandler extends ChannelInboundHandlerAdapter{

    public                  JCoincheClientHandler() {

    }

    @Override
    public void             channelActive(ChannelHandlerContext ctx) {
        System.out.println("[>] Connected!");
    }

    public void             channelRead(ChannelHandlerContext ctx, JCoincheProtocol.JCoincheMessage req) {
        System.out.println(String.format("[>] Message Received {type : \"%s\"}", req.getType()));
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
