package dev.mehmetali.patientservice.controller;

import dev.mehmetali.patientservice.dto.PatientRequestDto;
import dev.mehmetali.patientservice.dto.PatientResponseDto;
import dev.mehmetali.patientservice.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients") // http://localhost:4000/patients
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getPatients() {
        List<PatientResponseDto> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody PatientRequestDto patientDto) {
        PatientResponseDto patientResponseDto = patientService.createPatient(patientDto);
        return ResponseEntity.ok().body(patientResponseDto);
    }
}
