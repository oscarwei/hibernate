package tw.com.oscar.concurrency;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by oscarwei168 on 2015/6/8.
 */
public class SimpleExecutorServiceServer {

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        ExecutorService pool = new ThreadPoolExecutor(0, 1000, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>
                (), new ThreadPoolExecutor.DiscardPolicy());
        while (true) {
            final Socket socket = serverSocket.accept();
            pool.submit(() -> Utils.process(socket));

        }
    }
}
