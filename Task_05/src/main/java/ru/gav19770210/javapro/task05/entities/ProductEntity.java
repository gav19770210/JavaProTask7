package ru.gav19770210.javapro.task05.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "products", schema = "public", catalog = "javapro_task5")
public class ProductEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Поле <userId> должно быть заполнено")
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "Поле <accountNumber> должно быть заполнено")
    @Column(name = "account_number")
    private String accountNumber;

    @NotNull(message = "Поле <balance> должно быть заполнено")
    @Min(value = 0, message = "Значение поля <balance> должно быть больше нуля")
    @Column(name = "balance")
    private BigDecimal balance;

    @NotNull(message = "Поле <type> должно быть заполнено")
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProductType type;
}
