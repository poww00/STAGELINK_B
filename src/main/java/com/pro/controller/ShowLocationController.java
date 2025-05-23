package com.pro.controller;

import com.pro.dto.ShowLocationDTO;
import com.pro.service.ShowLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/showlocation")
@RequiredArgsConstructor
public class ShowLocationController {

    private final ShowLocationService showLocationService;


    @GetMapping("/{id}")
    public ShowLocationDTO getLocation(@PathVariable Long id) {
        System.out.println("📍 공연장 주소 요청 들어옴: " + id);
        return showLocationService.getLocation(id);
    }
}
