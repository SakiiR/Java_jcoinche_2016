/**
 * Created by sakiir on 09/11/16.
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.net.ConnectException;


public class            Main {
    public static void  main(String []args){
        int             port;
        String          host;

        if (args.length < 2) {
            System.out.println("[>] Usage : java -jar jcoinche-client.jar HOST PORT");
            return;
        }
        try {
            port = Integer.parseInt(args[1]);
            host = args[0];
        } catch (NumberFormatException e) {
            System.err.println("[-] Failed to parse int : " + args[1]);
            return;
        }

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
                            p.addLast(new JCoincheClientHandler());
                        }
                    });

            try {
                // Start the client.
                final ChannelFuture f = b.connect(host, port);

                try {
                    f.sync();
                } catch (Exception e) {
                    if (e instanceof ConnectException) {
                        System.err.println(String.format("[-] Failed to connect(%s, %d)", host, port));
                    }
                }
                // Wait until the connection is closed.
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }

    }
}
