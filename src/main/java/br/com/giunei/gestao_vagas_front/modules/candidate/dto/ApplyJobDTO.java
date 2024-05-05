package br.com.giunei.gestao_vagas_front.modules.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyJobDTO {

    private UUID id;
    private CandidateDTO candidate;
    private UUID candidateId;
    private UUID jobId;
}
