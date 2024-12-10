package ru.gav19770210.javapro.task06.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PaymentResponse {
    private Long productId;
    private BigDecimal balance;
    private String statusCode;
    private String statusText;
}
