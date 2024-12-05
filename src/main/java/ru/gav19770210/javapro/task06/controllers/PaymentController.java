package ru.gav19770210.javapro.task06.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gav19770210.javapro.task05.controllers.ErrorMessage;
import ru.gav19770210.javapro.task05.entities.ProductEntity;
import ru.gav19770210.javapro.task06.dto.PaymentRequest;
import ru.gav19770210.javapro.task06.dto.PaymentResponse;
import ru.gav19770210.javapro.task06.services.PaymentService;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "Payment Controller",
                version = "1.0",
                description = "My API Services"
        ),
        servers = {
                @Server(
                        description = "test server",
                        url = "http://localhost:8080"
                )
        }
)
@RestController
@RequestMapping(value = "payment", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Получение продукта по ИД клиента",
            responses = {
                    @ApiResponse(description = "Список найденных продуктов",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductEntity.class)
                            )
                    )
            }
    )
    @GetMapping(value = "/getUserProducts")
    public ResponseEntity<List<ProductEntity>> getUserProducts(@RequestParam("user_id") Long user_id) {
        var products = paymentService.getUserProducts(user_id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(products);
    }

    @Operation(summary = "Получение продукта по его ИД",
            responses = {
                    @ApiResponse(description = "Данные найденного продукт",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductEntity.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "NOT_FOUND",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @GetMapping(value = "/getProduct")
    public ResponseEntity<ProductEntity> getProductById(@RequestParam("id") Long id) {
        var productGet = paymentService.getProductById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productGet);
    }

    @Operation(summary = "Платежная операция списания",
            responses = {
                    @ApiResponse(description = "Статус выполнения операции",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "BAD_REQUEST",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "NOT_FOUND",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @PostMapping(value = "/debit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponse> debit(@RequestBody @Validated PaymentRequest paymentRequest) {
        var paymentResponse = paymentService.debit(paymentRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentResponse);
    }

    @Operation(summary = "Платежная операция начисления",
            responses = {
                    @ApiResponse(description = "Статус выполнения операции",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "BAD_REQUEST",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "NOT_FOUND",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @PostMapping(value = "/credit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponse> credit(@RequestBody @Validated PaymentRequest paymentRequest) {
        var paymentResponse = paymentService.credit(paymentRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentResponse);
    }
}
