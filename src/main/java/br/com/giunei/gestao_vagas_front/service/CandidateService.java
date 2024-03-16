package br.com.giunei.gestao_vagas_front.service;

import br.com.giunei.gestao_vagas_front.dto.Token;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CandidateService {

    public Token login(String username, String password) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(data, headers);

        var result = rt.postForObject("http://localhost:8080/candidate/auth", request, Token.class);
        System.out.println("Retorno da requisição");
        System.out.println(result.getAccess_token());

        return result;
    }
}