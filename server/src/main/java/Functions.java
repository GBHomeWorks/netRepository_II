import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Functions {
    //    foo = {
//
//    }
//
//    bar = foo
//
//    bar(param) foo(param)
    public static void main(String[] args) {
        Consumer<String> c = s -> {
            System.out.println("Hello " + s);
        };
        Consumer<String> c1 = System.out::println;
        // c.andThen(c1).andThen().andThen();

        Predicate<String> predicate = s -> s.length() > 3;
        predicate = predicate.and(s -> s.startsWith("OK"));
        System.out.println(predicate.test("OKLOL2"));

        Supplier<ArrayList<String>> supplier = ArrayList::new;

        // mapper
        Function<String, Integer> function = String::length;
        System.out.println(function.apply("OK"));
    }
}
