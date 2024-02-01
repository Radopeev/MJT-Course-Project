package bg.uni.sofia.fmi.mjt.splitwise.server;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBuilder;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AsyncServer {
    public static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;
    private static final Map<SocketChannel, Optional<User>> loggedUsers = new HashMap<>();
    private static final Path userFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Spilit(NotSo)Wise\\users.dat");
    private static final Path groupsFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Spilit(NotSo)Wise\\groups.dat");


    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            if (!Files.exists(userFile)) {
                try {
                    Files.createFile(userFile);
                } catch (IOException e) {
                    throw new RuntimeException("Error creating users file", e);
                }
            }

            if (!Files.exists(groupsFile)) {
                try {
                    Files.createFile(groupsFile);
                } catch (IOException e) {
                    throw new RuntimeException("Error creating groups file", e);
                }
            }
            UserRepository userRepository = new UserRepository(userFile,groupsFile);
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Server is shutting down. Saving user and group data...");

                userRepository.writeDataToDatabase(userFile,groupsFile);

                System.out.println("Server shutdown complete.");
            }));


            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    // select() is blocking but may still return with 0, check javadoc
                    continue;
                };
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();

                        buffer.clear();
                        int r = sc.read(buffer);
                        if (r < 0) {
                            System.out.println("Client has closed the connection");
                            sc.close();
                            continue;
                        }
                        buffer.flip();

                        // Custom processing of received data
                        String receivedData = new String(buffer.array(), 0, buffer.limit());
                        String responseData = CommandBuilder.newCommand(receivedData,userRepository,loggedUsers.get(sc)).execute();
                        if(receivedData.contains("login") && responseData.equals("You successfully logged in")){
                            String[] parts = receivedData.split(" ");
                            loggedUsers.put(sc,Optional.of(userRepository.getUser(parts[1])));
                        }

                        // Sending the custom response back to the client
                        buffer.clear();
                        buffer.put(responseData.getBytes());
                        buffer.flip();
                        sc.write(buffer);

                    } else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = sockChannel.accept();
                        loggedUsers.put(accept, Optional.empty());
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ);
                    }

                    keyIterator.remove();
                }

            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }
}