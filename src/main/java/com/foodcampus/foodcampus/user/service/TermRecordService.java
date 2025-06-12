package com.foodcampus.foodcampus.user.service;
import com.foodcampus.foodcampus.user.dto.TermsDto;
import com.foodcampus.foodcampus.user.entity.TermRecord;
import com.foodcampus.foodcampus.user.repository.TermRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;

@Service
public class TermRecordService {

    private final TermRecordRepository termRecordRepository;

    @Autowired
    public TermRecordService(TermRecordRepository termRecordRepository) {
        this.termRecordRepository = termRecordRepository;
    }

    public void recordTermAgreement(TermsDto termsDto) {
        try {
            String agreementDay = termsDto.getAgreementDay();

            // 날짜 형식을 변환하는 SimpleDateFormat 객체 생성
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yy.MM.dd");

            // 날짜 포맷 변환
            String formattedAgreementDay = targetFormat.format(originalFormat.parse(agreementDay));

            // TermRecord 객체 생성 및 저장
            TermRecord termRecord = new TermRecord();
            termRecord.setHistoryNum(termsDto.getHistoryNum());
            termRecord.setAgreementDay(formattedAgreementDay);
            termRecord.setConsent(termsDto.isConsent());

            // TermRecordRepository를 사용하여 동의 기록을 저장합니다.
            termRecordRepository.save(termRecord);
            System.out.println("동의 기록이 저장되었습니다: " + termRecord);
        } catch (Exception e) {
            System.err.println("동의 기록 중 오류가 발생했습니다: " + e.getMessage());
            // 예외 처리
        }
    }
}