package com.pro.service;

import com.pro.dto.ShowLocationDTO;

public interface ShowLocationService {

    //공연 장소
    ShowLocationDTO getLocation(Long id);
}
