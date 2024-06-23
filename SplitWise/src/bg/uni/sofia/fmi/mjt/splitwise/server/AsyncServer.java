package bg.uni.sofia.fmi.mjt.splitwise.server;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBuilder;
import bg.uni.sofia.fmi.mjt.splitwise.database.DatabaseSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.ExceptionSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.InvalidCommandException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.SplitWiseException;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AsyncServer {
    private static  boolean isRunning = true;
    public static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 4096;
    private static Map<SocketChannelWrapper, Optional<User>> loggedUsers = new HashMap<>();
    private static final Path USERS_FILE =
        Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\users.dat");
    private static final Path GROUPS_FILE =
        Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\groups.dat");

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = setupServerSocketChannel()) {
            UserRepository userRepository = new UserRepository(USERS_FILE, GROUPS_FILE);

            Selector selector = registerAndConfigureSelector(serverSocketChannel);

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            saveDataWhenServerIsShutdown(userRepository);

            while (isRunning) {
                handleSelections(selector, buffer, userRepository);
            }
        } catch (IOException e) {
            ExceptionSaver.saveException(new SplitWiseException(e, "No user", e.getStackTrace()));
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }

    private static ServerSocketChannel setupServerSocketChannel() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
        serverSocketChannel.configureBlocking(false);
        return serverSocketChannel;
    }

    private static Selector registerAndConfigureSelector(ServerSocketChannel serverSocketChannel) throws IOException {
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        return selector;
    }

    private static void handleSelections(Selector selector, ByteBuffer buffer,
                                         UserRepository userRepository) throws IOException {
        int readyChannels = selector.select();
        if (readyChannels == 0) {
            return;
        }
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            handleKey(key, buffer, userRepository);

            keyIterator.remove();
        }
    }

    private static void handleKey(SelectionKey key, ByteBuffer buffer,
                                  UserRepository userRepository) throws IOException {
        if (key.isReadable()) {
            handleReadableKey(key, buffer, userRepository);
        } else if (key.isAcceptable()) {
            handleAcceptableKey(key);
        }
    }

    private static void handleReadableKey(SelectionKey key, ByteBuffer buffer,
                                          UserRepository userRepository) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();

        buffer.clear();
        int r = sc.read(buffer);
        if (r < 0) {
            System.out.println("Client has closed the connection");
            sc.close();
        }
        handelReceivedData(buffer, userRepository, new SocketChannelWrapper(sc));
    }

    private static void handelReceivedData(ByteBuffer buffer, UserRepository userRepository, SocketChannelWrapper sc)
        throws IOException {
        buffer.flip();
        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        String receivedData = new String(byteArray, StandardCharsets.UTF_8);

        String responseData = executeCommand(receivedData, userRepository, sc);
        StringBuilder responseDataBuilder = new StringBuilder(responseData);

        handleLoginResponse(receivedData, responseDataBuilder, userRepository, sc);
        responseData = responseDataBuilder.toString();

        handleLogoutUser(receivedData, responseData, sc);

        buffer.clear();
        buffer.put(responseData.getBytes(StandardCharsets.UTF_8));
        buffer.flip();

        try {
            sc.getSocketChannel().write(buffer);
        } catch (IOException e) {
            ExceptionSaver.saveException(new SplitWiseException(e, "No user", e.getStackTrace()));
            System.out.println("Client has closed the connection");
            //throw new IOException();
        }
    }

    private static void handleAcceptableKey(SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();
        loggedUsers.put(new SocketChannelWrapper(accept), Optional.empty());
        accept.configureBlocking(false);
        accept.register(key.selector(), SelectionKey.OP_READ);
    }

    private static void handleLoginResponse(String receivedData, StringBuilder responseDataBuilder,
                                            UserRepository userRepository, SocketChannelWrapper sc) {
        if (receivedData.contains("login")
            && responseDataBuilder.toString().contains("You have successfully logged in")) {
            String[] parts = receivedData.split(" ");
            Optional<User> userOptional = Optional.of(userRepository.getUser(parts[1]));
            loggedUsers.put(sc, userOptional);
            responseDataBuilder.append(System.lineSeparator())
                .append(userOptional.map(User::seeNotifications).orElse(""));
        }
    }

    private static void handleLogoutUser(String receivedData, String responseData, SocketChannelWrapper sc) {
        if (receivedData.contains("logout") && responseData.contains("You have successfully logged out")) {
            loggedUsers.put(sc, Optional.empty());
        }
    }

    private static String executeCommand(String receivedData, UserRepository userRepository, SocketChannelWrapper sc) {
        try {
            return CommandBuilder.newCommand(receivedData, userRepository, loggedUsers.get(sc)).execute();
        } catch (InvalidCommandException e) {
            if (loggedUsers.get(sc).isPresent()) {
                ExceptionSaver.saveException(new SplitWiseException(e, loggedUsers.get(sc).get().getUsername(),
                    e.getStackTrace()));
            } else {
                ExceptionSaver.saveException(new SplitWiseException(e, "No user",
                    e.getStackTrace()));
            }
            return e.getMessage();
        }
    }

    private static void saveDataWhenServerIsShutdown(UserRepository userRepository) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Server is shutting down. Saving user and group data...");

            DatabaseSaver.writeDataToDatabase(USERS_FILE, GROUPS_FILE, userRepository);

            System.out.println("Server shutdown complete.");
            isRunning = false;
        }));
    }
}