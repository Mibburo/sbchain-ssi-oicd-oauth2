package com.example.sbchainssioicdoauth2.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.PopulateInfoService;
import com.example.sbchainssioicdoauth2.utils.FormType;

import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/multi/parenthood")
public class ParenthoodController {
    
    @Autowired
    CacheService cacheService;
    
    @Autowired
    PopulateInfoService infoService;

    @GetMapping("/view")
    protected ModelAndView parenthoodView(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request){
        model.addAttribute("uuid", uuid);
        return new ModelAndView("parenthood");
    }

    @GetMapping("/results")
    protected ModelAndView parenthoodResults(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request){
        
            infoService.populateFetchInfo(model, request, uuid);

            return new ModelAndView("parenthood");
        
    }

    @GetMapping("/save")
    protected ModelAndView parenthoodSave(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request){
        
        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        SsiApplication ssiApp = cacheService.get(uuid);
        infoService.populateSsiApp(ssiApp, context, FormType.PARENTHOOD_INFO.value, uuid);
        cacheService.putInfo(ssiApp, uuid);
        attr.addAttribute("uuid", uuid);

        try {
            request.logout();
        } catch (ServletException e) {
            log.error(e.getMessage());
        }
        return new ModelAndView("redirect:/multi/financialInfo/view");
    
    }
}