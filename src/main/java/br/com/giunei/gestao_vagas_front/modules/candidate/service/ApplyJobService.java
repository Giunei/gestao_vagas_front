package br.com.giunei.gestao_vagas_front.modules.candidate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ApplyJobService {

    @Value("${host.api.gestao.vagas}")
    private String hostAPIGestaoVagas;

    public String execute(String token, UUID idJob, Integer num) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jobId", idJob.toString());
        requestBody.put("rating", num);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        String url = hostAPIGestaoVagas.concat("/candidate/jobs/apply")
                + "?jobId=" + idJob
                + "&rating=" + num.toString();

        return rt.postForObject(url, request, String.class);
    }
}
