package com.mapmaker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mapmaker.dto.PinDTO;
import com.mapmaker.entity.Pin;
import com.mapmaker.repository.PinRepository;

@Service
public class PinService {

    @Autowired
    private PinRepository pinRepository;

    public void registerPin(PinDTO dto) {
        Pin pin = new Pin();
        pin.setName(dto.getName());
        pin.setDescription(dto.getDescription());
        pin.setLatitude(dto.getLatitude());
        pin.setLongitude(dto.getLongitude());
        pin.setCategory(dto.getCategory());
        pin.setAddress(dto.getAddress());

        pinRepository.insertPin(pin);
    }

    public List<Pin> getAllPins() {
        return pinRepository.findAll();
    }
}
