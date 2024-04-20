package br.com.giunei.gestao_vagas_front.modules.candidate.service;

import br.com.giunei.gestao_vagas_front.modules.candidate.dto.ProfileUserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ProfileCandidateService {

    @Value("${host.api.gestao.vagas}")
    private String hostAPIGestaoVagas;

    public ProfileUserDTO execute(String token) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        String url = hostAPIGestaoVagas.concat("/candidate/");

        try {
            ResponseEntity<ProfileUserDTO> result = rt.exchange(url, HttpMethod.GET, request, ProfileUserDTO.class);
            return result.getBody();
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
