package com.example.sbchainssioicdoauth2.controller;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.DBService;
import com.example.sbchainssioicdoauth2.service.PopulateInfoService;
import com.example.sbchainssioicdoauth2.service.ResourceService;
import com.example.sbchainssioicdoauth2.utils.FormType;
import com.example.sbchainssioicdoauth2.utils.LogoutUtils;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/multi/review")
public class ReviewApplicationsController {

    @Autowired
    CacheService cacheService;

    @Autowired
    ResourceService resourceService;

    @Autowired
    PopulateInfoService infoService;

    @Autowired
    DBService dbServ;

    private final static Logger log = LoggerFactory.getLogger(ReviewApplicationsController.class);

    @GetMapping("/view")
    protected ModelAndView personalInfo(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        model.addAttribute("uuid", uuid);
        model.addAttribute("firstPage", true);
        LocalDate today = LocalDate.now();

        final Principal principal = request.getUserPrincipal();
        if (principal instanceof KeycloakAuthenticationToken) {
            KeycloakAuthenticationToken kp = (KeycloakAuthenticationToken) principal;
            kp.getAccount().getKeycloakSecurityContext().getIdToken().getOtherClaims();
            Map<String, Object> otherClaims = kp.getAccount().getKeycloakSecurityContext().getIdToken().getOtherClaims();
            List<SsiApplication> applications = dbServ.findAllByAFM((String) otherClaims.get("taxisAfm"));
            model.addAttribute("applications", applications);
            model.addAttribute("newEnabled", true);

            applications.stream().forEach(app -> {
                long monthsBetween = ChronoUnit.MONTHS.between(
                        app.getTime().withDayOfMonth(1),
                        today.withDayOfMonth(1));

                if (app.getStatus().equals("active") && monthsBetween < 6) {
                    model.addAttribute("newEnabled", false);
                }
                //applications after 6 moths become expired
                if (monthsBetween >= 6) {
                    app.setStatus("expired");
                    dbServ.expire(app);
                }

            });
        }

        infoService.populateFetchInfo(model, request, uuid);
        SsiApplication ssiApp = cacheService.get(uuid);
        infoService.populateSsiApp(ssiApp, request, FormType.PERSONAL_DECLARATION.value, uuid);
//        ssiApp = infoService.updateModelfromCacheMergeDB(ssiApp, model, request,);
        cacheService.putInfo(ssiApp, uuid);
        return new ModelAndView("review");
    }

    @GetMapping("/continue")
    protected ModelAndView personalInfoSubmit(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
            ModelMap model, HttpServletRequest request, HttpSession session) {

        SsiApplication ssiApp = cacheService.get(uuid);
        LogoutUtils.forceRelogIfNotCondition(request, ssiApp.getHospitalized());
        return new ModelAndView("redirect:/multi/disqualifyingCrit/view?uuid=" + uuid);
    }

    @GetMapping("/nextCompleted")
    protected ModelAndView nextComplete(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid,
            ModelMap model, HttpServletRequest request, HttpSession session) {
        return new ModelAndView("redirect:/multi/disqualifyingCrit/view?uuid=" + uuid);
    }

}
