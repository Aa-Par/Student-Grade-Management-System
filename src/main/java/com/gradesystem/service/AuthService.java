package com.gradesystem.service;

import com.gradesystem.exception.GradeSystemException.AuthenticationException;
import com.gradesystem.util.FileManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthService {
    private static final String CREDENTIALS_FILE = "credentials.txt";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";
    private final FileManager fileManager = new FileManager();
    private final Map<String, String> credentials = new HashMap<>();

    public AuthService() {
        loadCredentials();
        if (credentials.isEmpty()) {
            credentials.put(DEFAULT_USERNAME, DEFAULT_PASSWORD);
            persistCredentials();
        }
    }
    private void loadCredentials() {
        for (String line : fileManager.readLines(CREDENTIALS_FILE)) {
            String[] parts = line.split("\\|", -1);
            if (parts.length == 2) {
                credentials.put(parts[0], parts[1]);
            }
        }
    }
    private void persistCredentials() {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<String, String> entry : credentials.entrySet()) {
            lines.add(entry.getKey() + "|" + entry.getValue());
        }
        fileManager.writeLines(CREDENTIALS_FILE, lines);
    }
    public void login(String username, String password) throws AuthenticationException {
        String stored = credentials.get(username);
        if (stored == null || !stored.equals(password)) {
            throw new AuthenticationException("Invalid username or password.");
        }
    }
    public String getDefaultUsername() {
        return DEFAULT_USERNAME;
    }
    public String getDefaultPassword() {
        return DEFAULT_PASSWORD;
    }
}
