import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by sakiir on 13/11/16.
 */

// Todo: Use JCoincheConstants to log info in the console (System.out.println)
public class                    JCoincheServerHandler extends SimpleChannelInboundHandler<String> {

    static final ChannelGroup   channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private GameHandle          gameHandle = null;

    public                      JCoincheServerHandler(GameHandle gameHandle) {
        this.gameHandle = gameHandle;
        this.gameHandle.setChannels(channels);
    }
    @Override
    public void                 channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
        if (this.gameHandle.isRunning() && channels.size() < 4) {
            System.out.println(String.format(JCoincheConstants.log_client_count, channels.size()));
            this.gameHandle.stopGame();
        }
    }

    @Override
    public void                 channelActive(final ChannelHandlerContext ctx) {
        if (channels.size() < 4) {
            System.out.println(String.format(JCoincheConstants.log_client_count, channels.size() + 1));
            ctx.writeAndFlush("Welcome!\r\nYou are in the waiting queue\r\n");
            channels.add(ctx.channel());
            if (channels.size() == 4) {
                System.out.println(JCoincheConstants.log_game_process_starting);
                this.gameHandle.startGame();
            }
        } else {
            ctx.writeAndFlush("Sorry there is a game in progress ..");
            ctx.close();
        }
    }

    @Override
    public void                 channelRead0(ChannelHandlerContext ctx, String msg) {  }

    @Override
    public void                 channelRead(ChannelHandlerContext ctx, Object msg) {
        String in = (String) msg;
        in = in.trim();
        if (this.gameHandle.getGameThread() != null) {
            System.out.println(String.format(JCoincheConstants.log_sending_data_to_game_process, in));
            this.gameHandle.getGameThread().addMessage(in);
        } else {
            ctx.writeAndFlush("You Can't send message right about now\r\n");
        }
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
