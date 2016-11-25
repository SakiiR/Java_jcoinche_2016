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
    }

    /**
     * A channel has been disconnected
     * @param ctx
     * @throws Exception
     */
    @Override
    public void                         channelInactive(ChannelHandlerContext ctx) throws Exception {
        JCoinchePlayer                  p = this.gameHandle.getPlayerByChannel(ctx.channel());

        /* if the player is already in the waiting queue */
        if (this.gameHandle.getPlayers().indexOf(p) > 3) {
            this.gameHandle.getPlayers().remove(p);
            ctx.close();
            JCoincheUtils.logInfo("[^] Disconnected Client ! Connected clients : %d", this.gameHandle.getPlayersCount());
            return;
        }
        /* if the game is already stopped and player is not in game */
        if (!GameThread.isRunning) {
            this.gameHandle.getPlayers().remove(p);
            ctx.close();
            JCoincheUtils.logInfo("[^] Disconnected Client ! Connected clients : %d", this.gameHandle.getPlayersCount());
            return;
        }
        p.setChannel(null);
        ctx.fireChannelInactive();
        channels.remove(ctx.channel());
        ctx.close();
        JCoincheUtils.logInfo("[^] Disconnected Client ! Connected clients : %d", this.gameHandle.getPlayersCount());
        this.gameHandle.stopGame();
        if (this.gameHandle.getPlayersCount() >= 4) {
            this.gameHandle.startGame();
            return;
        }
    }

    /**
     * A channel has been connected
     * @param ctx
     */
    @Override
    public void                         channelActive(ChannelHandlerContext ctx) {
        if (this.gameHandle.getPlayersCount() > 10) {
            // Queue is too big .. sorry
            ctx.close();
            return;
        }
        channels.add(ctx.channel());
        this.gameHandle.addPlayer(ctx.channel());
        JCoincheUtils.logInfo("[^] New Client ! Connected clients : %d", this.gameHandle.getPlayersCount());
        if (this.gameHandle.getPlayersCount() <= 4) {
            JCoincheUtils.writeAndFlushWithoutChecks(ctx.channel(), MessageForger.forgeWelcomeMessage("Welcome to the doudoune coinchée ! Waiting for others players ..."));
            if (this.gameHandle.getPlayersCount() == 4) {
                this.gameHandle.startGame();
            }
        } else {
            JCoincheUtils.writeAndFlushWithoutChecks(ctx.channel(), MessageForger.forgeWelcomeMessage("Welcome to the doudoune coinchée ! A game is in progress, waiting for a new game ..."));
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

        JCoincheUtils.logSuccess("[+] Received Packet of Size(%d) of Type(%s)", req.getSerializedSize(), req.getType());
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
