package com.demok8s.controller;

import com.demok8s.dto.CreateLabRequest;
import com.demok8s.dto.CreateLabResponse;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class LabController {

        private final CoreV1Api coreV1Api;

        @Autowired
        public LabController(CoreV1Api coreV1Api) {
                this.coreV1Api = coreV1Api;
        }

        @PostMapping("/create-lab")
        public ResponseEntity<CreateLabResponse> createLab(@RequestBody CreateLabRequest request) {
                if (request.getScriptContent() == null || request.getScriptContent().isEmpty()) {
                        CreateLabResponse response = new CreateLabResponse("error",
                                        "script_content không được để trống", null, null);
                        return ResponseEntity.badRequest().body(response);
                }

                final String namespace = "kodeklone";
                final String uniqueID = UUID.randomUUID().toString().substring(0, 8);
                final String cmName = "setup-script-" + uniqueID;
                final String podName = "practice-pod-" + uniqueID;

                try {
                        V1ConfigMap configMap = new V1ConfigMap()
                                        .apiVersion("v1")
                                        .kind("ConfigMap")
                                        .metadata(new V1ObjectMeta().namespace(namespace).name(cmName))
                                        .data(Map.of("setup.sh", request.getScriptContent()));

                        coreV1Api.createNamespacedConfigMap(namespace, configMap).execute();
                        System.out.println("Đã tạo ConfigMap: " + cmName);

                        V1Pod pod = createPodDefinition(namespace, podName, cmName);

                        coreV1Api.createNamespacedPod(namespace, pod).execute();
                        System.out.println("Đã tạo Pod: " + podName);

                        CreateLabResponse response = new CreateLabResponse(
                                        "success",
                                        "Môi trường thực hành đã được tạo thành công!",
                                        podName,
                                        cmName);
                        return ResponseEntity.status(HttpStatus.CREATED).body(response);

                } catch (ApiException e) {
                        System.err.println("Lỗi khi tạo môi trường lab: " + e.getResponseBody());
                        e.printStackTrace();
                        CreateLabResponse response = new CreateLabResponse("error",
                                        "Lỗi phía server: " + e.getMessage(), null, null);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }
        }

        private V1Pod createPodDefinition(String namespace, String podName, String cmName) {
                return new V1Pod()
                                .apiVersion("v1")
                                .kind("Pod")
                                .metadata(new V1ObjectMeta()
                                                .namespace(namespace)
                                                .name(podName))
                                .spec(new V1PodSpec()
                                                .overhead(null)
                                                .containers(List.of(
                                                                new V1Container()
                                                                                .name("practice-container")
                                                                                .image("alpine:latest")
                                                                                .command(List.of("/bin/sh", "-c"))
                                                                                .args(List.of("/scripts/setup.sh && echo '--- Script da chay xong, giu container song... ---' && tail -f /dev/null"))
                                                                                .volumeMounts(List.of(
                                                                                                new V1VolumeMount()
                                                                                                                .name("script-volume")
                                                                                                                .mountPath("/scripts")))))
                                                .volumes(List.of(
                                                                new V1Volume()
                                                                                .name("script-volume")
                                                                                .configMap(new V1ConfigMapVolumeSource()
                                                                                                .name(cmName)
                                                                                                .defaultMode(0777))))
                                                .restartPolicy("Never"));
        }
}