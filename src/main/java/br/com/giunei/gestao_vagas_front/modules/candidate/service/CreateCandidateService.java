package br.com.giunei.gestao_vagas_front.modules.candidate.service;

import br.com.giunei.gestao_vagas_front.modules.candidate.dto.CreateCandidateDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CreateCandidateService {

    @Value("${host.api.gestao.vagas}")
    private String hostAPIGestaoVagas;

    public void execute(CreateCandidateDTO candidateDTO) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateCandidateDTO> request = new HttpEntity<>(candidateDTO, headers);

        String url = hostAPIGestaoVagas.concat("/candidate/");

        rt.postForObject(url, request, String.class);
    }
}
