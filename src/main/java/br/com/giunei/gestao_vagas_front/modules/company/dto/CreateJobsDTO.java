package br.com.giunei.gestao_vagas_front.modules.company.dto;

import lombok.Data;

@Data
public class CreateJobsDTO {

    private String description;
    private String level;
    private String benefits;
}
