package io.github.pingao777.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import io.github.pingao777.common.enums.GCType;


/**
 * Created by pingao on 2020/1/17.
 */
public class ParseUtils {
    private static final long MILLION = 1000000L;
    private static final long BILLION = 1000000000L;
    private static final long TRILLION = 1000000000000L;
    private static final long THOUSAND = 1000L;

    public static double parseByte(String value, String unit) {
        double bytes = Double.parseDouble(value);
        switch (unit) {
            case "b":
            case "B":
                return bytes;
            case "k":
            case "K":
                return bytes * 1024;
            case "m":
            case "M":
                return bytes * 1024 * 1024;
            case "g":
            case "G":
                return bytes * 1024 * 1024 * 1024;
            default:
                throw new IllegalArgumentException("Unknown unit " + unit);
        }
    }

    public static String simple(double number) {
        return number < THOUSAND ? String.valueOf(number) :
            number < MILLION ? (int) (number / THOUSAND) + "K" :
                number < BILLION ? (int) (number / MILLION) + "M" :
                    number < TRILLION ? (int) (number / BILLION) + "G" :
                        (int) (number / TRILLION) + "T";
    }

    public static String simple(long number) {
        return number < THOUSAND ? String.valueOf(number) :
            number < MILLION ? (int) (number / THOUSAND) + "K" :
                number < BILLION ? (int) (number / MILLION) + "M" :
                    number < TRILLION ? (int) (number / BILLION) + "G" :
                        (int) (number / TRILLION) + "T";
    }

    public static GCType whichGC(String path) {
        try {
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("CommandLine flags:")) {
                    if (line.contains("UseG1GC")) {
                        return GCType.G1;
                    }
                    if (line.contains("UseConcMarkSweepGC")) {
                        return GCType.CMS;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return GCType.UNKNOWN;
    }
}
