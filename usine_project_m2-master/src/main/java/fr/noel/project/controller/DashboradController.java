package fr.noel.project.controller;


import fr.noel.project.dto.ContentResponseDto;
import fr.noel.project.dto.ResponseDto;
import fr.noel.project.service.DashboardService;
import fr.noel.project.service.impl.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/app/dash")
public class DashboradController  {


    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private AppUserServiceImpl userService;


    @GetMapping
    public ResponseDto getDashboardState(Authentication authentication){
        try {
            final Long idByEmail = this.userService.findIdByEmail(authentication.getName());
            final Map<String, Object> appState = this.dashboardService.getAppState(idByEmail);
            return new ContentResponseDto(true,"OK",appState);
        } catch (Exception e) {
            return new ResponseDto(false,e.getMessage());
        }
    }


}
