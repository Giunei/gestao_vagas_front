package br.com.giunei.gestao_vagas_front.modules.candidate.dto;

import lombok.Data;

import java.util.List;

@Data
public class Token {

    private String access_token;
    private Long expires_in;
    private List<String> roles;

}
