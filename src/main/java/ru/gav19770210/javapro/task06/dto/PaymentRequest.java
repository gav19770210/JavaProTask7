package ru.gav19770210.javapro.task06.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRequest {
    @NotNull(message = "Поле <productId> должно быть заполнено")
    private Long productId;
    @NotNull
    @Min(value = 0, message = "Поле <summa> должно быть заполнено")
    private BigDecimal summa;
}
