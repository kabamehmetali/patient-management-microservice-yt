package dev.mehmetali.patientservice.controller;

import dev.mehmetali.patientservice.dto.PatientRequestDto;
import dev.mehmetali.patientservice.dto.PatientResponseDto;
import dev.mehmetali.patientservice.dto.validator.CreatePatientValidationGroup;
import dev.mehmetali.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients") // http://localhost:4000/patients
@Tag(name="patient", description = "API")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "Get Patients", description = "this is a describtion")
    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getPatients() {
        List<PatientResponseDto> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
    }

    @Operation(summary = "Create a new patient")
    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDto patientDto) {
        PatientResponseDto patientResponseDto = patientService.createPatient(patientDto);
        return ResponseEntity.ok().body(patientResponseDto);
    }

    @Operation(summary = "Update a patient")
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable UUID id,
            @Validated({Default.class}) @RequestBody PatientRequestDto patientDto)
    {
        PatientResponseDto patientResponseDto = patientService.updatePatient(id, patientDto);
        return ResponseEntity.ok().body(patientResponseDto);
    }

    @Operation(summary = "Delete a pation")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

}
