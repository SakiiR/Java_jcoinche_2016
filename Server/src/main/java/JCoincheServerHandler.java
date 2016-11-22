import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;

/**
 * Created by sakiir on 13/11/16.
 */

public class                            JCoincheServerHandler extends ChannelInboundHandlerAdapter {

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
    public void                         channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
        JCoincheUtils.log(JCoincheConstants.log_client_disconnected);
        JCoincheUtils.log(JCoincheConstants.log_client_count, channels.size());
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
        if (channels.size() <= 4) {
            channels.add(ctx.channel());
            JCoincheUtils.log(JCoincheConstants.log_client_count, channels.size() + 1);
            JCoincheUtils.writeAndFlush(ctx.channel(), MessageForger.forgeWelcomeMessage("Welcome to the doudoune coinchée ! Waiting for others players ..."));
            if (channels.size() == 4) {
                JCoincheUtils.log(JCoincheConstants.log_game_process_starting);
                this.gameHandle.startGame();
            }
        } else {
            JCoincheUtils.writeAndFlush(ctx.channel(), MessageForger.forgeWelcomeMessage("Welcome to the doudoune coinchée ! A game is in progress, waiting for a new game ..."));
            ctx.close();
        }
    }

    /**
     * When an Object is read
     * @param ctx
     * @param msg
     */
    @Override
    public void                 channelRead(ChannelHandlerContext ctx, Object msg) {

        JCoincheProtocol.JCoincheMessage req = (JCoincheProtocol.JCoincheMessage) msg;
        String                           token;

        if (this.gameHandle.getGameThread() != null) {
            if (req.hasToken()) {
                token = req.getToken();
                ArrayList<JCoinchePlayer> players = this.gameHandle.getGameThread().getAllPlayers();
                for (JCoinchePlayer p : players) {
                    if (token.equals(p.getToken())) {
                        p.setMessage(req);
                        JCoincheUtils.log("[>] Received Message [%s]", p.getMessage().getType());
                        break;
                    }
                }
            }
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
        ctx.close();
    }
}
