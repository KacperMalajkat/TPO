package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.*;

public class Futil {

    public static void processDir(String dirName, String resultFileName) {

        Charset csIn = Charset.forName("Cp1250"); // Kodowanie plików to: Cp1250
        Charset csOut = StandardCharsets.UTF_8; // Kodowanie pliku TPO1res.txt to: UTF-8
        List<Path> list = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(dirName))) {
            list = paths.filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileChannel fcOut = FileChannel.open(Paths.get(resultFileName), CREATE, TRUNCATE_EXISTING, WRITE)) {

            for (Path path:list){
                try (FileChannel fcIn = FileChannel.open(path)){
                    ByteBuffer bb = ByteBuffer.allocateDirect((int) fcIn.size());
                    fcIn.read(bb);
                    bb.flip();
                    CharBuffer cb = csIn.decode(bb);
                    fcOut.write(csOut.encode(cb));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
