import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 实践：java NIO
 * A simple practice for Java NIO
 * Client/Server
 */
public class NIOServer {

    private final static int port = 8080;
    private Selector selector;
    // 线程池，用来处理事件
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        new NIOServer().start();
    }

    public void start() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); //打开信道
            serverSocketChannel.configureBlocking(false); // non-blocking
            serverSocketChannel.bind(new InetSocketAddress(port)); // 绑定段口号
            selector = Selector.open();
            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT); // 注册，关注事件：可接受的连接

            System.out.println("server start...");

            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        while (true){
            try {
                int event = selector.select();
                if(event > 0){
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if(key.isAcceptable()){
                            accept(key);
                        }else if(key.isReadable()){
                            executorService.submit(new NIOServerHandler(key));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // 接收
    public void accept(SelectionKey key) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        try {
            // 接收连接，生成 一个 socket channel 用来 read
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector,SelectionKey.OP_READ);

            System.out.println("server accept a client: " + socketChannel.socket().getInetAddress().getHostName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class NIOServerHandler implements Runnable{

    private SelectionKey key;

    public NIOServerHandler(SelectionKey key){
        this.key = key;
    }

    // 处理可读事件
    @Override
    public void run() {

        try {

            if(key.isReadable()){
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                socketChannel.read(byteBuffer);
                byteBuffer.flip();
                System.out.println("client: " + new String(byteBuffer.array()));

                // 回复客户端
                socketChannel.write(ByteBuffer.wrap(byteBuffer.array()));
                key.cancel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
