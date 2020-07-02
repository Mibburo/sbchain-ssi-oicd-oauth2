package com.example.sbchainssioicdoauth2.controller;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.service.CacheService;
import com.example.sbchainssioicdoauth2.service.PopulateInfoService;
import com.example.sbchainssioicdoauth2.utils.FormType;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/multi/assetInfo")
public class AssetInfoController {

    @Autowired
    CacheService cacheService;

    @Autowired
    PopulateInfoService infoService;

    @GetMapping("/view")
    protected ModelAndView financialInfo(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) throws JsonProcessingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        model.addAttribute("uuid", uuid);
        infoService.populateFetchInfo(model, request, uuid);
        SsiApplication ssiApp = cacheService.get(uuid);
        infoService.populateSsiApp(ssiApp, request, FormType.ASSET_INFO.value, uuid);
        infoService.mergeModelFromCache(ssiApp, model);
        cacheService.putInfo(ssiApp, uuid);
        return new ModelAndView("assetInfo");
    }

//    @GetMapping("/results")
//    protected ModelAndView financialInfoResults(@RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {
//
//        infoService.populateFetchInfo(model, request, uuid);
//        return new ModelAndView("assetInfo");
//
//    }
    @GetMapping("/continue")
    protected ModelAndView financialInfoSubmit(RedirectAttributes attr, @RequestParam(value = "uuid", required = true) String uuid, ModelMap model, HttpServletRequest request) {

        // do not log out as they already have imported their taxis claim containing the required credentials
        // i.e. the household info
        return new ModelAndView("redirect:/multi/householdInfo/view?uuid=" + uuid);
    }
}
