package utils;

import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class macros {
    public final static Scanner STDIN = new Scanner(System.in);

    public static String currentTime() {
        return java.time.LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
    }
    public static void log(String msg) {
        System.out.printf("[%s] %s%n", currentTime(), msg);
    }
}
