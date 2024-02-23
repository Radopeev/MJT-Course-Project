package bg.uni.sofia.fmi.mjt.splitwise.server;

import java.nio.channels.SocketChannel;
import java.util.Objects;

public class SocketChannelWrapper {
    private final SocketChannel socketChannel;

    public SocketChannelWrapper(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocketChannelWrapper that = (SocketChannelWrapper) o;
        return Objects.equals(socketChannel, that.socketChannel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socketChannel);
    }
}
