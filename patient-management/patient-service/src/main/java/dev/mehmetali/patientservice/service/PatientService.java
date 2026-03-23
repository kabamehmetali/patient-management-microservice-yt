package dev.mehmetali.patientservice.service;

import dev.mehmetali.patientservice.dto.PatientRequestDto;
import dev.mehmetali.patientservice.dto.PatientResponseDto;
import dev.mehmetali.patientservice.grpc.BillingServiceGrpcClient;
import dev.mehmetali.patientservice.mapper.PatientMapper;
import dev.mehmetali.patientservice.exception.EmailAlreadyExistException;
import dev.mehmetali.patientservice.exception.PatientNotFoundException;
import dev.mehmetali.patientservice.model.Patient;
import dev.mehmetali.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public PatientService(PatientRepository patientRepository,  BillingServiceGrpcClient billingServiceGrpcClient) {
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDto> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients
                .stream()
                .map(PatientMapper::toDto).toList();
    }

    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        if(patientRepository.existsByEmail(patientRequestDto.getEmail())) {
            throw new EmailAlreadyExistException("A patient with this email already exist.: " + patientRequestDto.getEmail());
        }
        Patient newPatient = patientRepository.save(
                PatientMapper
                .toModel(patientRequestDto));

        billingServiceGrpcClient.createBillingAccount(
                newPatient.getId().toString(),
                newPatient.getName(),
                newPatient.getEmail());
        return PatientMapper.toDto(newPatient);
    }

    public PatientResponseDto updatePatient(UUID id, PatientRequestDto patientRequestDto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(()->new PatientNotFoundException("Patient Not Found with ID: " + id) );
        if(patientRepository.existsByEmailAndIdNot(patientRequestDto.getEmail(), id)) {
            throw new EmailAlreadyExistException("A patient with this email already exist.: " + patientRequestDto.getEmail());
        }

        patient.setName(patientRequestDto.getName());
        patient.setAddress(patientRequestDto.getAddress());
        patient.setEmail(patientRequestDto.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()));

        patientRepository.save(patient);
        return PatientMapper.toDto(patient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }



}