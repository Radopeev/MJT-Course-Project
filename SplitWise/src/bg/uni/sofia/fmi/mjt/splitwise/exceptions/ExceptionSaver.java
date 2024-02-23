package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

public class ExceptionSaver {
    private static final Path EXCEPTIONSFILE =
        Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\exceptions.txt");

    public static void saveException(SplitWiseException exception) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EXCEPTIONSFILE.toFile()))) {
            writer.println(exception.toString());
            writer.println(System.lineSeparator());
        } catch (IOException e) {
            throw new ExceptionSaverException("There was a problem with saving the exception");
        }
    }
}
