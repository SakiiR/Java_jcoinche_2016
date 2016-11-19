/**
 * Created by sakiir on 09/11/16.
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
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
        new JCoincheClient(host, port).run();
    }
}
