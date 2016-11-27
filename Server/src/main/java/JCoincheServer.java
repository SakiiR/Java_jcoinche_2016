import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import java.net.BindException;

/**
 * Created by sakiir on 12/11/16.
 */

/**
 * This class is describing the main doudoune coinché netty server.
 * It is called by the Main Class.
 *
 * @see Main
 */
public class                        JCoincheServer {

    protected int                   port;
    protected GameHandle            gameHandle = null;

    /**
     * JCoincheServer Constructor
     *
     * @param port the command line port specified in the Main Class.
     * @see Main
     */
    public JCoincheServer(int port){
        this.port = port;
        this.gameHandle = new GameHandle();
    }

    /**
     * This method is introducing the netty boilerplate TCP server.
     *
     * @see Main
     */
    public void                     run() {
        JCoincheUtils.log(JCoincheConstants.log_server_starting);

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                            ch.pipeline().addLast("protobufDecoder", new ProtobufDecoder(JCoincheProtocol.JCoincheMessage.getDefaultInstance()));
                            ch.pipeline().addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                            ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());
                            ch.pipeline().addLast(new JCoincheServerHandler(gameHandle));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            try {

                ChannelFuture f = b.bind(this.port).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            if (future.cause() instanceof BindException) {  }
                        }
                    }
                }).sync();
                JCoincheUtils.log(JCoincheConstants.log_server_started, this.port);
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(84);
            }
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
