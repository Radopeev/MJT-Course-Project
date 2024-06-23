package bg.uni.sofia.fmi.mjt.splitwise.server;

import bg.uni.sofia.fmi.mjt.splitwise.exceptions.ExceptionSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.SplitWiseException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 4096;
    private static boolean isLogged = false;

    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            connectToServer(socketChannel);

            while (true) {
                String message = readUserInput(scanner);

                if ("quit".equals(message)) {
                    if (isLoggedOut()) {
                        break;
                    } else {
                        System.out.println("You have to logout before exiting the app" + System.lineSeparator());
                    }
                } else {
                    communicateWithServer(socketChannel, message);
                }
            }

        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            ExceptionSaver.saveException(new SplitWiseException(e, "No user", e.getStackTrace()));
            throw new RuntimeException();
        }
    }

    private static boolean isLoggedOut() {
        return !isLogged;
    }

    private static void connectToServer(SocketChannel socketChannel) throws IOException {
        socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
        System.out.println("Connected to the server.");
    }

    private static String readUserInput(Scanner scanner) {
        System.out.print("$");
        return scanner.nextLine();
    }

    private static void communicateWithServer(SocketChannel socketChannel, String message) throws IOException {
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        socketChannel.write(buffer);

        buffer.clear();
        socketChannel.read(buffer);
        buffer.flip();

        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        String reply = new String(byteArray, StandardCharsets.UTF_8);
        if (reply.equals("You have successfully logged in" + System.lineSeparator())) {
            isLogged = true;
        }
        if (reply.equals("You have successfully logged out" + System.lineSeparator())) {
            isLogged = false;
        }

        System.out.println(reply);
    }
}
