package br.com.giunei.gestao_vagas_front.modules.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUserDTO {

    private String email;
    private UUID id;
    private String description;
    private String username;
    private String name;
    private List<String> vagasAplicadas;
}
