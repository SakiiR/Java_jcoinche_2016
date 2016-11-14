import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultMaxBytesRecvByteBufAllocator;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by sakiir on 13/11/16.
 */

public class                    JCoincheServerHandler extends SimpleChannelInboundHandler<String> {

    static final ChannelGroup   channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private GameHandle          gameHandle = null;

    public                      JCoincheServerHandler(GameHandle gameHandle) {
        this.gameHandle = gameHandle;
    }
    @Override
    public void                 channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
        if (this.gameHandle.isRunning() && channels.size() < 4) {
            System.out.println("Clients : " + (channels.size()) + "/4");
            this.gameHandle.stopGame();
        }
    }

    @Override
    public void                 channelActive(final ChannelHandlerContext ctx) {
        if (channels.size() < 4) {
            System.out.println("Clients : " + (channels.size() + 1 )+ "/4");
            ctx.writeAndFlush("Welcome!\r\nYou are in the waiting queue\r\n");
            channels.add(ctx.channel());
            if (channels.size() == 4) {
                System.out.println("Starting Game Process ! :D");
                this.gameHandle.startGame(channels);
            }
        } else {
            ctx.writeAndFlush("Sorry there is a game in progress ..");
            ctx.close();
        }
    }

    @Override
    public void                 channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println("channelRead0 : " + msg);
    }

    @Override
    public void                 channelRead(ChannelHandlerContext ctx, Object msg) {
        String in = (String) msg;
        in = in.trim();
        System.out.println("[>] Sending to game Thread : " + in);
        this.gameHandle.getGameThread().addMessage(in);
    }

    @Override
    public void                 channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void                 exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
