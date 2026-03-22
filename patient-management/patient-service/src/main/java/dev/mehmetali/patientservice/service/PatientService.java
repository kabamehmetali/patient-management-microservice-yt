package dev.mehmetali.patientservice.service;

import dev.mehmetali.patientservice.dto.PatientRequestDto;
import dev.mehmetali.patientservice.dto.PatientResponseDto;
import dev.mehmetali.patientservice.dto.mapper.PatientMapper;
import dev.mehmetali.patientservice.model.Patient;
import dev.mehmetali.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDto> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients
                .stream()
                .map(PatientMapper::toDto).toList();
    }

    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        Patient newPatient = patientRepository.save(
                PatientMapper
                .toModel(patientRequestDto));
        return PatientMapper.toDto(newPatient);
    }

}
