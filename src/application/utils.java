package application;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class utils {
    public final static Scanner STDIN = new Scanner(System.in);

    public static String currentTime() {
        return java.time.LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
    }
    public static void log(String msg) {
        System.out.printf("[%s] %s%n", currentTime(), msg);
    }
    public static String toTitleCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        return Arrays
                .stream(text.split(" "))
                .map(word -> word.isEmpty()
                        ? word
                        : Character.toTitleCase(word.charAt(0)) + word
                        .substring(1)
                        .toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
