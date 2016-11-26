import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.ConnectException;

/**
 * Created by sakiir on 19/11/16.
 */
public class                JCoincheClient {

    private String          host;
    private int             port;
    private ClientProcess   clientProcess = null;

    public                  JCoincheClient(String host, int port) {
        this.port = port;
        this.host = host;
        this.clientProcess = new ClientProcess();
    }

    public void             run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            ch.pipeline().addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                            ch.pipeline().addLast("protobufDecoder", new ProtobufDecoder(JCoincheProtocol.JCoincheMessage.getDefaultInstance()));
                            ch.pipeline().addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                            ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());
                            p.addLast(new JCoincheClientHandler(clientProcess));
                        }
                    });
            try {
                final ChannelFuture f = b.connect(this.host, this.port);
                try {
                    f.sync();
                } catch (Exception e) {
                    if (e instanceof ConnectException) {
                        JCoincheUtils.logStderr("[-] Failed to connect(%s, %d)", host, port);
                        System.exit(84);
                    }
                }

                Channel serverChannel = f.channel();
                clientProcess.getPlayerInformations().setChannel(serverChannel);
                serverChannel.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            group.shutdownGracefully();
        }

    }
}
