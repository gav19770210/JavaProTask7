package ru.gav19770210.javapro.task06.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentResponse {
    private String statusCode;
    private String statusText;
}
