package com.demok8s.dto;

public class CreateLabResponse {
    private String status;
    private String message;
    private String podName;
    private String configMapName;

    public CreateLabResponse(String status, String message, String podName, String configMapName) {
        this.status = status;
        this.message = message;
        this.podName = podName;
        this.configMapName = configMapName;
    }

    // Chỉ cần constructor và getters là đủ
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public String getPodName() { return podName; }
    public String getConfigMapName() { return configMapName; }
}