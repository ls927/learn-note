import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 实践：java NIO
 * A simple practice for Java NIO
 * Client/Server
 */
public class NIOClient {

    private static final int serverPort = 8080;
    private static final String serverHost = "127.0.0.1";
    private Selector selector;

    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NIOClient client = new NIOClient(); //打开一个客户端
                    client.connect(serverHost,serverPort); //连接到服务器
                    client.writeAndListen(); // 传送数据并且监听事件
                }
            }).start();
        }

    }

    public void connect(String serverHost, int serverPort) {
        try {
            SocketChannel socketChannel = SocketChannel.open();  // 打开 Socket 信道
            socketChannel.configureBlocking(false); // 设置为 non-blocking
            selector = Selector.open(); // 单例模式创建一个 selector 选择器
            socketChannel.register(selector, SelectionKey.OP_CONNECT); // 将信道注册到选择器中，并指定感兴趣的事件，此处关注事件为 连接完成事件
            socketChannel.connect(new InetSocketAddress(serverHost,serverPort)); // 指定服务器套接字地址并连接

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeAndListen() {
        // 多次轮询
        while (true){
            try {

                int event = selector.select(); // 轮询信道 socketChannel,就绪事件的个数
                if(event > 0){
                    Set<SelectionKey> keys = selector.selectedKeys(); // key 对应一个就绪事件
                    Iterator<SelectionKey> iterator = keys.iterator(); // 获取迭代器
                    // 处理就绪事件
                    while (iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();

                        // 处理连接事件
                        if(selectionKey.isConnectable()){
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            if(socketChannel.isConnectionPending()){
                                socketChannel.finishConnect();
                            }
                            // 完成连接，进行 监听通道 和 写操作
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            socketChannel.write(ByteBuffer.wrap(("Hello,this is " + Thread.currentThread().getName()).getBytes()));
                        }
                        // 处理可读事件
                        else {
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            channel.read(byteBuffer);
                            byteBuffer.flip();  // 刷新缓存？
                            System.out.println("server : " + new String(byteBuffer.array()));
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
