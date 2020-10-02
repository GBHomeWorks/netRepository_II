import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Monads {
    public Monads() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        // flatMap, get, apply
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filter(val -> val % 2 == 0)
                .map("a"::repeat)
                .map(s -> s + " " + s)
                .forEach(System.out::println);

        List<String> data = Files.lines(Path.of("server/data.txt"))
                .filter(line -> !line.isEmpty())
                .flatMap(line -> Arrays
                        .stream(line.split(" +")))
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
        System.out.println(data);

        System.out.println(
                Files.lines(Path.of("server/data.txt"))
                        .filter(line -> !line.isEmpty())
                        .flatMap(line -> Arrays
                                .stream(line.split(" +")))
                        .filter(line -> !line.isEmpty())
                        .collect(Collectors.toMap(
                                key -> key,
                                val -> 1,
                                Integer::sum)
                        )
        );
        System.out.println(
                Files.lines(Path.of("server/dict.txt"))
                        .map(line -> line.split(" "))
                        .collect(Collectors.toMap(
                                key -> key[0],
                                key -> key[1])
                        )
        );
        System.out.println(fileNames());
    }

    static List<String> fileNames() throws IOException {
        List<String> names = new ArrayList<>();
        Files.walkFileTree(Path.of("./server"),
                new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        names.add(file.toString().replace("./server", ""));
                        return super.visitFile(file, attrs);
                    }
                });
        return names;
    }

    int foo(List<Integer> list) {
        return list.stream()
                .filter(x -> x % 2 == 0)
                .findAny()
                .orElse(-1);
    }

}
