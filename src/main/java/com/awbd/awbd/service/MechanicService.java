package com.awbd.awbd.service;

import com.awbd.awbd.dto.MechanicDto;
import com.awbd.awbd.mapper.MechanicMapper;
import com.awbd.awbd.repository.MechanicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MechanicService {

    private final MechanicRepository mechanicRepository;
    private final MechanicMapper mechanicMapper;

    public List<MechanicDto> findAll() {
        return mechanicRepository.findAll().stream()
                .map(mechanicMapper::toMechanicDto)
                .toList();
    }
}
