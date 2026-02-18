package com.chanakya.service;

import com.chanakya.dto.CareerDTO;
import com.chanakya.entity.Career;
import com.chanakya.exception.ResourceNotFoundException;
import com.chanakya.repository.CareerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CareerService {

    private final CareerRepository careerRepository;
    private final ModelMapper modelMapper;

    public List<CareerDTO> getAllCareers() {
        List<Career> careers = careerRepository.findByIsActiveTrueOrderByPopularityScoreDesc();
        return careers.stream()
                .map(career -> modelMapper.map(career, CareerDTO.class))
                .collect(Collectors.toList());
    }

    public CareerDTO getCareerById(Long id) {
        Career career = careerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Career not found with id: " + id));
        return modelMapper.map(career, CareerDTO.class);
    }

    public CareerDTO getCareerByName(String name) {
        Career career = careerRepository.findByNameAndIsActiveTrue(name)
                .orElseThrow(() -> new ResourceNotFoundException("Career not found with name: " + name));
        return modelMapper.map(career, CareerDTO.class);
    }
}
