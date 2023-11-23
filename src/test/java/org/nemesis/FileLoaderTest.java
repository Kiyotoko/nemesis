package org.nemesis;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

public class FileLoaderTest {
    @Test
    void load() throws IOException {
        System.out.println(ClassLoader.class.getResource("test.txt"));
        System.out.println();
        try (ReadableByteChannel channel = Channels.newChannel(Objects.requireNonNull(FileLoaderTest.class.getResource("test.txt")).openStream())) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);
            System.out.println(new String(buffer.array()));
        }
        System.out.println(FileLoaderTest.class.getResourceAsStream("test.txt"));
    }
}
