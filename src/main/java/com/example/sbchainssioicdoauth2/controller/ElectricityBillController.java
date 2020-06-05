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
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/multi/electricityBill")
public class ElectricityBillController {

    @Autowired
    CacheService cacheService;
    
    @Autowired
    PopulateInfoService infoService;

    @GetMapping("/view")
    protected ModelAndView electricityBillInfo(@AuthenticationPrincipal OidcUser oidcUser, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request){
        model.addAttribute("uuid", uuid);
        return new ModelAndView("electricityBill");
    }
    
    @GetMapping("/results")
    protected ModelAndView electricityBillInfoResults(@AuthenticationPrincipal OAuth2User oidcUser, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request){
        
        infoService.populateFetchInfo(model, request, uuid);
        return new ModelAndView("electricityBill");
    
    }
    
    @GetMapping("/save")
    protected ModelAndView electricityBillInfoSubmit(@AuthenticationPrincipal OidcUser oidcUser, RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request){

        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        SsiApplication ssiApp = cacheService.get(uuid);
        infoService.populateSsiApp(ssiApp, context, FormType.ELECTRICITY_BILL_INFO.value, uuid);
        cacheService.putInfo(ssiApp, uuid);
        attr.addAttribute("uuid", uuid);

        try {
            request.logout();
        } catch (ServletException e) {
            log.error(e.getMessage());
        }

        return new ModelAndView("redirect:/multi/fead/view");
    }

}