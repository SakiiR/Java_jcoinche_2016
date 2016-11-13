import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultMaxBytesRecvByteBufAllocator;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by sakiir on 13/11/16.
 */

public class JCoincheServerHandler extends SimpleChannelInboundHandler<String> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
        System.out.println("Clients :" + (channels.size())+ "/4");
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        if (channels.size() < 4) {
            System.out.println("Clients :" + (channels.size() + 1 )+ "/4");
            channels.add(ctx.channel());
            if (channels.size() == 4) {
                // Start Game Process !
                System.out.println("Starting Game Process ! :D");
            }
        } else {
            System.out.println("Sorry there is a game in progress ..");
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println("Received [" + msg.toLowerCase() + "]");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String in = (String) msg;
        System.out.println("[>] Received : [" + in + "]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
