package br.com.giunei.gestao_vagas_front.modules.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDTO {

    private UUID id;
    private String description;
    private String benefits;
    private String level;
    private UUID companyId;
    private LocalDateTime createdDate;
}
