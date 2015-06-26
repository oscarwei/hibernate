package tw.com.oscar.concurrency;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by oscarwei168 on 2015/6/9.
 */
public class NewIOServer {

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));
        ExecutorService pool = Executors.newFixedThreadPool(100);
        while (true) {
            SocketChannel sc = serverSocketChannel.accept();
            pool.submit(() -> Utils.process(sc));
        }
    }
}
