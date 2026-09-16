package com.gradesystem.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager { 
    private static final String DATA_DIR = "data";
    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Warning: could not create data directory: " + e.getMessage());
        }
    }
    public List<String> readLines(String fileName) {
        Path path = Paths.get(DATA_DIR, fileName);
        List<String> lines = new ArrayList<>();
        if (!Files.exists(path)) {
            return lines;
        }
        try {
            for (String line : Files.readAllLines(path)) {
                if (!line.isBlank()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + fileName + ": " + e.getMessage());
        }
        return lines;
    }
    public void writeLines(String fileName, List<String> lines) {
        Path path = Paths.get(DATA_DIR, fileName);
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            System.err.println("Error writing " + fileName + ": " + e.getMessage());
        }
    }
    public void appendLine(String fileName, String line) {
        List<String> lines = readLines(fileName);
        lines.add(line);
        writeLines(fileName, lines);
    }
}
