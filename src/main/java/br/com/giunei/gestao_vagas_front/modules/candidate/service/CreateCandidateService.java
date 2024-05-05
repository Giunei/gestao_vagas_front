package br.com.giunei.gestao_vagas_front.modules.candidate.service;

import br.com.giunei.gestao_vagas_front.modules.candidate.dto.ApplyJobDTO;
import br.com.giunei.gestao_vagas_front.modules.candidate.dto.CreateCandidateDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class CreateCandidateService {

    @Value("${host.api.gestao.vagas}")
    private String hostAPIGestaoVagas;

    public void execute(CreateCandidateDTO candidateDTO) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateCandidateDTO> requestEntity = new HttpEntity<>(candidateDTO, headers);

        String url = hostAPIGestaoVagas.concat("/candidate/");

        try {
            rt.exchange(url, HttpMethod.POST, requestEntity, Void.class);
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
