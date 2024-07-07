package com.project;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class DependencyManager {
    private static final Pattern REQUIRE_PATTERN = Pattern.compile("require ‘(.+?)’");

    private final Path rootDirectory;
    private final Map<String, Set<String>> dependencyGraph;

    public DependencyManager(Path rootDirectory) {
        this.rootDirectory = rootDirectory;
        this.dependencyGraph = new HashMap<>();
    }

    public void processDirectory() throws IOException {
        buildDependencyGraph(rootDirectory);
    }

    private void buildDependencyGraph(Path currentDirectory) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDirectory)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    buildDependencyGraph(entry);
                } else if (entry.toString().endsWith(".txt")) {
                    String relativePath = rootDirectory.relativize(entry).toString().replace("\\", "/");
                    dependencyGraph.putIfAbsent(relativePath, new HashSet<>());
                    List<String> dependencies = getDependencies(entry);
                    for (String dependency : dependencies) {
                        dependencyGraph.get(relativePath).add(dependency);
                    }
                }
            }
        }
    }

    private List<String> getDependencies(Path file) throws IOException {
        List<String> dependencies = new ArrayList<>();
        List<String> lines = Files.readAllLines(file);
        for (String line : lines) {
            Matcher matcher = REQUIRE_PATTERN.matcher(line);
            if (matcher.find()) {
                dependencies.add(matcher.group(1));
            }
        }
        return dependencies;
    }

    public List<String> getOrderedFiles() {
        return getTopologicalOrder(dependencyGraph);
    }

    private List<String> getTopologicalOrder(Map<String, Set<String>> graph) {
        Map<String, Integer> inDegree = new HashMap<>();
        for (String node : graph.keySet()) {
            inDegree.put(node, 0);
        }
        for (String node : graph.keySet()) {
            for (String neighbor : graph.get(node)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        Queue<String> queue = new LinkedList<>();
        for (String node : inDegree.keySet()) {
            if (inDegree.get(node) == 0) {
                queue.add(node);
            }
        }

        List<String> sortedList = new ArrayList<>();
        while (!queue.isEmpty()) {
            String node = queue.poll();
            sortedList.add(node);
            for (String neighbor : graph.get(node)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (sortedList.size() != graph.size()) {
            throw new RuntimeException("Cyclic dependency detected.");
        }

        return sortedList;
    }
}
