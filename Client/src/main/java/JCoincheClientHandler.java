import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by sakiir on 18/11/16.
 */
public class                JCoincheClientHandler extends ChannelInboundHandlerAdapter{
    private final ByteBuf   firstMessage;

    public                  JCoincheClientHandler() {
        firstMessage = Unpooled.buffer(256);
        for (int i = 0; i < firstMessage.capacity(); ++i) {
            firstMessage.writeByte((byte) i);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("[>] Connected!");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("[>] Channel Read!");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("[>] Channel Read Complete!");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
