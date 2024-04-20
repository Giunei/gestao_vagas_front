package br.com.giunei.gestao_vagas_front.modules.company.controller;

import br.com.giunei.gestao_vagas_front.modules.candidate.dto.JobDTO;
import br.com.giunei.gestao_vagas_front.modules.candidate.dto.Token;
import br.com.giunei.gestao_vagas_front.modules.company.dto.CreateCompanyDTO;
import br.com.giunei.gestao_vagas_front.modules.company.dto.CreateJobsDTO;
import br.com.giunei.gestao_vagas_front.modules.company.service.CreateCompanyService;
import br.com.giunei.gestao_vagas_front.modules.company.service.CreateJobService;
import br.com.giunei.gestao_vagas_front.modules.company.service.ListAllJobsCompanyService;
import br.com.giunei.gestao_vagas_front.modules.company.service.LoginCompanyService;
import br.com.giunei.gestao_vagas_front.utils.ErrorMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CreateCompanyService createCompanyService;

    @Autowired
    private LoginCompanyService loginCompanyService;

    @Autowired
    private CreateJobService createJobService;

    private final String COMPANY = "company";

    @Autowired
    private ListAllJobsCompanyService listAllJobsCompanyService;

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

    @GetMapping("/login")
    public String login() {
        return "company/login";
    }

    @PostMapping("/signIn")
    public String signIn(RedirectAttributes redirectAttributes, HttpSession session, String username, String password) {
        try {
            Token token = this.loginCompanyService.execute(username, password);
            var grants = token.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase())).toList();

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, null, grants);
            auth.setDetails(token.getAccess_token());

            SecurityContextHolder.getContext().setAuthentication(auth);
            SecurityContext context = SecurityContextHolder.getContext();
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);
            session.setAttribute("token", token);

            return "redirect:/company/jobs";

        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error_message", "Usuário/Senha incorretos");
            return "redirect:/company/jobs";
        }
    }

    @GetMapping("/jobs")
    @PreAuthorize("hasRole('COMPANY')")
    public String jobs(Model model) {
        model.addAttribute("jobs", new CreateJobsDTO());
        return "company/jobs";
    }

    @PostMapping("/jobs")
    @PreAuthorize("hasRole('COMPANY')")
    public String createJobs(CreateJobsDTO jobs) {
        String result = this.createJobService.execute(jobs, getToken());
        System.out.println(result);
        return "redirect:/company/jobs/list";
    }

    @GetMapping("/jobs/list")
    @PreAuthorize("hasRole('COMPANY')")
    public String list(Model model) {
        List<JobDTO> result = this.listAllJobsCompanyService.execute(getToken());
        model.addAttribute("jobs", result);
        return "company/list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContext context = SecurityContextHolder.getContext();
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);
        session.setAttribute("token", null);
        return "redirect:/company/login";
    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getDetails().toString();
    }
}
