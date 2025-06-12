package com.foodcampus.foodcampus.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "terms_records")
public class TermRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyNum;

    private String agreementDay;

    private boolean consent;

    // 0 또는 1로 전달되는 값을 boolean 값으로 변환하여 설정합니다.
    public void setConsent(boolean consent) {
        this.consent = consent;
    }
}