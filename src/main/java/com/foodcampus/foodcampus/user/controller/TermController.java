package com.foodcampus.foodcampus.user.controller;

import com.foodcampus.foodcampus.user.dto.TermsDto;
import com.foodcampus.foodcampus.user.service.TermRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TermController {

    private final TermRecordService termRecordService;

    @Autowired
    public TermController(TermRecordService termRecordService) {
        this.termRecordService = termRecordService;
    }

    @PostMapping("/terms_record")
    public ResponseEntity<String> recordTermAgreement(@RequestBody TermsDto termsDto) {
        try {
            termRecordService.recordTermAgreement(termsDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("동의가 기록되었습니다.");
        } catch (Exception e) {
            System.err.println("동의 기록 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("동의 기록 중 오류가 발생했습니다.");
        }
    }
}
