package br.com.giunei.gestao_vagas_front.modules.candidate.service;

import br.com.giunei.gestao_vagas_front.modules.candidate.dto.JobDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class FindJobsService {

    public List<JobDTO> execute(String token, String filter) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/candidate/jobs")
                .queryParam("filter", filter);

        ParameterizedTypeReference<List<JobDTO>> responseType =
                new ParameterizedTypeReference<>() {
                };

        try {
            ResponseEntity<List<JobDTO>> result = rt.exchange(builder.toUriString(), HttpMethod.GET, request, responseType);
            return result.getBody();
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
