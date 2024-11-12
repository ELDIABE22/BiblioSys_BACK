package com.example.bibliosys.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliosys.Models.response.dashboard.CountEntityResponse;
import com.example.bibliosys.Services.impl.DashboardServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class DashboardController {
    @Autowired
    private DashboardServiceImpl dashboardServiceImpl;

    @GetMapping("/dashboard")
    public ResponseEntity<CountEntityResponse> fetchCountEntityController() {
        CountEntityResponse details = dashboardServiceImpl.fetchCountEntityService();
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
}
