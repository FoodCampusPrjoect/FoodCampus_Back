package com.foodcampus.foodcampus.user.dto;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TermsDto {
    private Long historyNum;

    private String agreementDay;

    private boolean consent;
}