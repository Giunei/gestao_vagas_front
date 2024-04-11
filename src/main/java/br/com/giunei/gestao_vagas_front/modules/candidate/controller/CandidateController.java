package br.com.giunei.gestao_vagas_front.modules.candidate.controller;

import br.com.giunei.gestao_vagas_front.modules.candidate.dto.CreateCandidateDTO;
import br.com.giunei.gestao_vagas_front.modules.candidate.dto.JobDTO;
import br.com.giunei.gestao_vagas_front.modules.candidate.dto.ProfileUserDTO;
import br.com.giunei.gestao_vagas_front.modules.candidate.dto.Token;
import br.com.giunei.gestao_vagas_front.modules.candidate.service.ApplyJobService;
import br.com.giunei.gestao_vagas_front.modules.candidate.service.CandidateService;
import br.com.giunei.gestao_vagas_front.modules.candidate.service.CreateCandidateService;
import br.com.giunei.gestao_vagas_front.modules.candidate.service.FindJobsService;
import br.com.giunei.gestao_vagas_front.modules.candidate.service.ProfileCandidateService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private ProfileCandidateService profileCandidateService;

    @Autowired
    private FindJobsService findJobsService;

    @Autowired
    private ApplyJobService applyJobService;

    @Autowired
    private CreateCandidateService createCandidateService;

    private final String REDIRECT_LOGIN = "redirect:/candidate/login";

    @GetMapping("/login")
    public String login() {
        return "candidate/login";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("candidate", new CreateCandidateDTO());
        return "candidate/create";
    }

    @PostMapping("/create")
    public String save(Model model, CreateCandidateDTO candidate) {
        try {
            this.createCandidateService.execute(candidate);

        } catch (HttpClientErrorException ex) {
            model.addAttribute("error_message", ErrorMessage.formatErrorMessage(ex.getResponseBodyAsString()));
        }

        model.addAttribute("candidate", candidate);
        return "candidate/create";
    }

    @PostMapping("/signIn")
    public String signIn(RedirectAttributes redirectAttributes, HttpSession session, String username, String password) {
        try {
            Token token = this.candidateService.login(username, password);
            var grants = token.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase())).toList();

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, null, grants);
            auth.setDetails(token.getAccess_token());

            SecurityContextHolder.getContext().setAuthentication(auth);
            SecurityContext context = SecurityContextHolder.getContext();
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);
            session.setAttribute("token", token);
            System.out.println("token: " + grants);

            return "redirect:/candidate/profile";

        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error_message", "Usuário/Senha incorretos");
            return REDIRECT_LOGIN;
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public String profile(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            ProfileUserDTO user = this.profileCandidateService.execute(authentication.getDetails().toString());

            model.addAttribute("user", user);

            return "candidate/profile";
        } catch (HttpClientErrorException e) {
            return REDIRECT_LOGIN;
        }

    }

    @GetMapping("/jobs")
    @PreAuthorize("hasRole('CANDIDATE')")
    public String jobs(Model model, String filter) {

        System.out.println("Filtro: " + filter);

        try {
            if (filter != null) {
                List<JobDTO> jobs = this.findJobsService.execute(getToken(), filter);
                model.addAttribute("jobs", jobs);
            }

        } catch (HttpClientErrorException e) {
            return REDIRECT_LOGIN;
        }
        return "candidate/jobs";
    }

    @PostMapping("/jobs/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    public String applyJob(@RequestParam("jobId") UUID jobId) {
        this.applyJobService.execute(getToken(), jobId);
        return "redirect:/candidate/jobs";
    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getDetails().toString();
    }
}
