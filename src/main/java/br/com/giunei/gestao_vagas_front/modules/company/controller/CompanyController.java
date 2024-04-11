package br.com.giunei.gestao_vagas_front.modules.company.controller;

import br.com.giunei.gestao_vagas_front.modules.company.dto.CreateCompanyDTO;
import br.com.giunei.gestao_vagas_front.modules.company.service.CreateCompanyService;
import br.com.giunei.gestao_vagas_front.utils.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CreateCompanyService createCompanyService;

    private final String COMPANY = "company";

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute(COMPANY, new CreateCompanyDTO());
        return "company/create";
    }

    @PostMapping("/create")
    public String save(Model model, CreateCompanyDTO createCompanyDTO) {
        try {
            this.createCompanyService.execute(createCompanyDTO);
            model.addAttribute(COMPANY, new CreateCompanyDTO());
        } catch (HttpClientErrorException ex) {
            model.addAttribute("error_message", ErrorMessage.formatErrorMessage(ex.getResponseBodyAsString()));
        }
        model.addAttribute(COMPANY, createCompanyDTO);
        return "company/create";
    }
}
