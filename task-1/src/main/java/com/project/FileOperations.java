package com.project;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileOperations {
    public void mergeFiles(List<String> files, String outputFileName) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFileName))) {

            for (String filePath : files) {
                Path path = Paths.get(System.getProperty("user.dir") + "/root/" + filePath);
                List<String> lines = Files.readAllLines(path);

                for (String line : lines) {
                    if (!line.startsWith("require")) {
                        writer.write(line);
                        writer.newLine();
                    }
                }

            }

        }
    }
}
