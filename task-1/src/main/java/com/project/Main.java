package com.project;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        Path path = Paths.get(System.getProperty("user.dir") + "/root");

        DependencyManager dependencyManager = new DependencyManager(path);
        dependencyManager.processDirectory();

        List<String> sortedFiles = dependencyManager.getOrderedFiles();

        FileOperations fileOperations = new FileOperations();
        fileOperations.mergeFiles(sortedFiles, Paths.get("output.txt").toString());
    }

}