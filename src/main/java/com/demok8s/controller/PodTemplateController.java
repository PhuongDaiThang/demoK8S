package com.demok8s.controller;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1PodList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PodTemplateController {

    private final CoreV1Api coreV1Api;

    @Autowired
    public PodTemplateController(CoreV1Api coreV1Api) {
        this.coreV1Api = coreV1Api;
    }

    @GetMapping("/pods")
    public ResponseEntity<List<String>> listPods() {
        try {
            V1PodList pods = coreV1Api.listPodForAllNamespaces().execute();

            List<String> podNames = pods.getItems().stream()
                    .map(pod -> pod.getMetadata().getName())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(podNames);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#listPodForAllNamespaces");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
            return ResponseEntity.status(500).body(List.of("Error fetching pods: " + e.getMessage()));
        }
    }

}
