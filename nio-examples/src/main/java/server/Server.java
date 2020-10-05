package server;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class Server {

    public static String path = "nio-examples/src/main/resources";
    static final ByteBuffer buffer = ByteBuffer.allocate(256);

    public static void main(String[] args) throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8189));
        server.configureBlocking(false);
        Selector selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("server path: " + path);
        while (server.isOpen()) {
            selector.select();
            Iterator<SelectionKey> iterator
                    = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey current = iterator.next();
                if (current.isAcceptable()) {
                    handleAccept(current, selector);
                }
                if (current.isReadable()) {
                    handleRead(current, selector);
                }
                iterator.remove();
            }
        }
    }

    private static void handleRead(SelectionKey current, Selector selector) throws IOException {
        SocketChannel channel = (SocketChannel) current.channel();
        System.out.println("Message handled!");
        StringBuilder s = new StringBuilder();
        int x;
        while (true) {
            x = channel.read(buffer);
            if (x == -1) {
                channel.close();
                System.out.println("Client leave");
                break;
            }
            if (x == 0) break;
            buffer.flip();
            while (buffer.hasRemaining()) {
                s.append((char) buffer.get());
            }
//            FileChannel aFile = new RandomAccessFile(path, "rw").getChannel();
//            while (buffer.hasRemaining()){
//            System.out.print((char) buffer.get());
//                aFile.write(buffer);
//        }
            buffer.clear();
        }
        if (current.isValid()) {
            for (SelectionKey key : selector.keys()) {
                if (key.isValid() && key.channel() instanceof SocketChannel) {
                    ((SocketChannel) key.channel())
                            .write(ByteBuffer.wrap(
                                    (current.attachment() + ": " + s.toString()).getBytes())
                            );
                }
            }
        }

    }

    static int cnt = 1;

    private static void handleAccept(SelectionKey current, Selector selector) throws IOException {
        SocketChannel channel = ((ServerSocketChannel) current.channel()).accept();
        channel.configureBlocking(false);
        System.out.println("Client accepted!");
        channel.register(selector, SelectionKey.OP_READ, "user" + (cnt++));
    }
}
