package br.com.giunei.gestao_vagas_front.modules.company.service;

import br.com.giunei.gestao_vagas_front.modules.candidate.dto.CreateCandidateDTO;
import br.com.giunei.gestao_vagas_front.modules.company.dto.CreateCompanyDTO;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CreateCompanyService {

    public String execute(CreateCompanyDTO createCompanyDTO) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateCompanyDTO> request = new HttpEntity<>(createCompanyDTO, headers);

        return rt.postForObject("http://localhost:8080/company/", request, String.class);
    }
}
