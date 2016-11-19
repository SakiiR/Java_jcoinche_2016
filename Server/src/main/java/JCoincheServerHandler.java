import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by sakiir on 13/11/16.
 */

public class                            JCoincheServerHandler extends SimpleChannelInboundHandler<String> {

    static final private ChannelGroup   channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private GameHandle                  gameHandle = null;

    /**
     * Channel handler consrcutor : store gameHandle and give it channels list
     * @param gameHandle
     */
    public                              JCoincheServerHandler(GameHandle gameHandle) {
        this.gameHandle = gameHandle;
        this.gameHandle.setChannels(channels);
    }

    /**
     * A channel has been disconnected
     * @param ctx
     * @throws Exception
     */
    @Override
    public void                 channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
        System.out.println(String.format(JCoincheConstants.log_client_count, channels.size()));
        if (this.gameHandle.isRunning() && channels.size() < 4) {
            this.gameHandle.stopGame();
        }
    }

    /**
     * A channel has been connected
     * @param ctx
     */
    @Override
    public void                 channelActive(final ChannelHandlerContext ctx) {
        if (channels.size() < 4) {
            System.out.println(String.format(JCoincheConstants.log_client_count, channels.size() + 1));
            ctx.writeAndFlush(MessageForger.forgeWelcomeMessage("Welcome to the doudoune coinchée ! Waiting for others players ..."));
            channels.add(ctx.channel());
            if (channels.size() == 4) {
                System.out.println(JCoincheConstants.log_game_process_starting);
                this.gameHandle.startGame();
            }
        } else {
            //todo: ctx.writeAndFlush();
            ctx.writeAndFlush(MessageForger.forgeWelcomeMessage("Welcome to the doudoune coinchée ! A game is in progress, waiting for a new game ..."));
            ctx.close();
        }
    }

    /**
     * When a String is read
     * @param ctx
     * @param msg
     */
    @Override
    public void                 channelRead0(ChannelHandlerContext ctx, String msg) {  }

    /**
     * When an Object is read
     * @param ctx
     * @param msg
     */
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

    /**
     * When a channel finished to read
     * @param ctx
     */
    @Override
    public void                 channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * When an exception is caught
     * @param ctx
     * @param cause
     */
    @Override
    public void                 exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
