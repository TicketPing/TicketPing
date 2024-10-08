package com.ticketPing.queue_manage.infrastructure.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YmlLoader {

    private static YmlLoader instance;
    private Map<String, String> configMap;
    private final String contextPath;

    /**
     * 싱글톤 인스턴스를 반환
     */
    public static YmlLoader ymlLoader() {
        if (instance == null) {
            synchronized (YmlLoader.class) {
                if (instance == null) {
                    instance = new YmlLoader();
                }
            }
        }
        return instance;
    }

    /**
     * application.yml 파일에서 설정 로드
     */
    private YmlLoader() {
        this.configMap = new HashMap<>();
        try {
            this.configMap = loadYml("src/main/resources/application.yml");
        } catch (IOException e) {
            try {
                this.configMap = loadYml("services/queue-manage/src/main/resources/application.yml");
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load configuration from both default and fallback paths", ex);
            }
        }
        this.contextPath = this.configMap.getOrDefault("server.servlet.context-path", "");
    }

    public static String getConfigMap(String key) {
        YmlLoader loader = YmlLoader.ymlLoader();
        return loader.configMap.getOrDefault(key, null);
    }

    public static Map<String, String>  getConfigMap() {
        YmlLoader loader = YmlLoader.ymlLoader();
        return loader.configMap;
    }

    private static Map<String, String> loadYml(String filePath) throws IOException {
        Map<String, String> configMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentPath = "";
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.trim().startsWith("#")) {
                    int indentLevel = getIndentLevel(line);
                    String[] parts = line.trim().split(":", 2);
                    if (parts.length == 2) {
                        currentPath = updateCurrentPath(currentPath, indentLevel, parts[0].trim());
                        String key = currentPath;
                        String value = parts[1].trim();
                        configMap.put(key, value);
                    }
                }
            }
        }
        return configMap;
    }

    private static int getIndentLevel(String line) {
        int indent = 0;
        while (line.charAt(indent) == ' ') {
            indent++;
        }
        return indent / 2;
    }

    private static String updateCurrentPath(String currentPath, int indentLevel, String newSegment) {
        String[] pathSegments = currentPath.split("\\.");
        StringBuilder newPath = new StringBuilder();
        for (int i = 0; i < indentLevel && i < pathSegments.length; i++) {
            newPath.append(pathSegments[i]).append(".");
        }
        newPath.append(newSegment);
        return newPath.toString();
    }

    public String getContextPath() {
        return this.contextPath;
    }

    /**
     * 환경 설정
     */
    public static String redisHost() {
        YmlLoader loader = YmlLoader.ymlLoader();
        return loader.configMap.getOrDefault("redis.host", "localhost");
    }

    public static String redisPort() {
        YmlLoader loader = YmlLoader.ymlLoader();
        return loader.configMap.getOrDefault("redis.port", "6379");
    }

    public static int workingQueueMaxSize() {
        YmlLoader loader = YmlLoader.ymlLoader();
        return Integer.parseInt(loader.configMap.getOrDefault("working-queue.max-size", "1000"));
    }

    public static int workingQueueTokenTtl() {
        YmlLoader loader = YmlLoader.ymlLoader();
        return Integer.parseInt(loader.configMap.getOrDefault("working-queue.token-ttl", "30"));
    }

}