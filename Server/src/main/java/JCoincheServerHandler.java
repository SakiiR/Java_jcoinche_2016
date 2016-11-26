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

/**
 * This class is the ChannelInboundHandlerAdapter implementation.
 * It permit to handle events triggered by the netty base EventLoop.
 *
 * @see ChannelInboundHandlerAdapter
 * @see JCoincheServer
 */
public class                            JCoincheServerHandler extends ChannelInboundHandlerAdapter {

    static final private ChannelGroup   channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private GameHandle                  gameHandle = null;

    /**
     * JCoincheServerHandler Constructor
     *
     * @param gameHandle The GameHandle base game process handler.
     */
    public                              JCoincheServerHandler(GameHandle gameHandle) {
        this.gameHandle = gameHandle;
    }

    /**
     * The callback is triggered when a clien is disconnected.
     *
     * @param ctx Channel / Client
     * @throws Exception if the callback crash.
     */
    @Override
    public void                         channelInactive(ChannelHandlerContext ctx) throws Exception {
        JCoinchePlayer                  p = this.gameHandle.getPlayerByChannel(ctx.channel());

        if (p.getGameThread() != null) {
            this.gameHandle.stopGame(p.getGameThread());
        }
        this.gameHandle.getPlayers().remove(p);
        this.gameHandle.handleRemainingsClients();
        JCoincheUtils.logInfo("[!] Player Disconnected ! " + p.getChannel().remoteAddress() + " Connected clients : %d", this.gameHandle.getPlayersCount());
    }

    /**
     * A channel has been connected
     *
     * @param ctx Channel / Client
     */
    @Override
    public void                         channelActive(ChannelHandlerContext ctx) {
        channels.add(ctx.channel());
        JCoincheUtils.writeAndFlush(ctx.channel(), MessageForger.forgeWelcomeMessage("Welcome to the doudoune coinché, you are in the waiting queue"));
        this.gameHandle.addPlayer(ctx.channel());
        JCoincheUtils.logInfo("[^] Player Connected " + ctx.channel().remoteAddress() + " ! Connected clients : %d", this.gameHandle.getPlayersCount());
        this.gameHandle.handleGames();
    }

    /**
     * When an Object is read from the channel
     *
     * @param ctx Channel Context
     * @param msg The Received Message Object.
     */
    @Override
    public void                             channelRead(ChannelHandlerContext ctx, Object msg) {
        JCoincheProtocol.JCoincheMessage    req = (JCoincheProtocol.JCoincheMessage) msg;
        String                              token;

        JCoincheUtils.logSuccess("[+] Received Packet of Size(%d) of Type(%s)", req.getSerializedSize(), req.getType());
        if (req.hasToken()) {
            token = req.getToken();
            ArrayList<JCoinchePlayer> players = this.gameHandle.getPlayers();
            for (JCoinchePlayer p : players) {
                if (token.equals(p.getToken())) {
                    p.setMessage(req);
                    break;
                }
            }
        }
    }

    /**
     * When a channel finished to read.
     *
     * @param ctx Channel Context
     */
    @Override
    public void                 channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * When an exception is caught by netty.
     *
     * @param ctx Channel Context
     * @param cause The Exception cause object.
     */
    @Override
    public void                 exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
