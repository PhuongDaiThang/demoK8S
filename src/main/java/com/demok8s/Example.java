package com.demok8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import java.io.IOException;

class Example {
    public static void main(String[] args) throws IOException, ApiException {
//        String masterUrl = "https://192.168.42.112:6443";
//        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IkN2SE9PQ3RMTXVnZVZpVkpNejJLdEtoejNad1VJemRZOVRYYnJIbnBoVkkifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJqZW5raW5zIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6InNhLXRva2VuLXNlY3JldCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJqZW5raW5zLWFkbWluIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiN2VkM2FhZDItN2Y3Ni00YTkyLTk1YzgtNWFhNmE2NmU4YTk1Iiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OmplbmtpbnM6amVua2lucy1hZG1pbiJ9.TH1StLriKydmwHxt2F0_XUfCxpP4HB_yaBS_-LK7bZx1I_i1z6lMG_GIsXGLjXyrd1YP8YximR4dQB2cH35-VZc8FxjeALQpXhYF8NLCbV63DpCYub0BySPBYYnU0tvblJl8VRit4Oc1cYhAvXF_BGZrbq8KeVzJd6rlM6T96FPB-USnMEwnoGY7fo1RK83_hi0NqnuY7aKEQJad2WIVNfxEpen9ffXNrtKrCptlmZVU3Cvm3LYf1vOFO5h-0XsBBjZ7T8efH8qBt5pbUO-cm0ut2Q-tZArdTSmtQy4Pzgx_DwTWwc5ToEe9lU6ypsuoTaa5ANWAsRYlX2UTOjursw";
//        ApiClient client = new ClientBuilder()
//                .setBasePath(masterUrl)
//                .setVerifyingSsl(false)
//                .build();
//
//        client.addDefaultHeader("Authorization", "Bearer " + token);
//        Configuration.setDefaultApiClient(client);
//
//        CoreV1Api api = new CoreV1Api();
//        V1PodList pods = api.listPodForAllNamespaces().execute();
//
//        for(V1Pod pod : pods.getItems()) {
//            System.out.println(pod.getMetadata().getName());
//        }
    }
}
