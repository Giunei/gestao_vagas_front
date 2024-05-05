package br.com.giunei.gestao_vagas_front.modules.company.service;

import br.com.giunei.gestao_vagas_front.modules.candidate.dto.ApplyJobDTO;
import br.com.giunei.gestao_vagas_front.modules.candidate.dto.JobDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ListAllAppliedsJobsService {

    @Value("${host.api.gestao.vagas}")
    private String hostAPIGestaoVagas;

    public List<ApplyJobDTO> execute(String token, UUID jobId) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        String url = hostAPIGestaoVagas.concat("/company/job/applieds");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("jobId", jobId);

        ParameterizedTypeReference<List<ApplyJobDTO>> responseType =
                new ParameterizedTypeReference<>() {
                };

        try {
            ResponseEntity<List<ApplyJobDTO>> result = rt.exchange(builder.toUriString(), HttpMethod.GET, request, responseType);
            return result.getBody();
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
