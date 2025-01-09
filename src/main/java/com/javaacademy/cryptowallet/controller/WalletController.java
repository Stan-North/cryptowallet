package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.CreateWalletRequestDto;
import com.javaacademy.cryptowallet.dto.WalletActionDto;
import com.javaacademy.cryptowallet.dto.WalletDto;
import com.javaacademy.cryptowallet.exception.currency.CurrencyDoesNotSupportException;
import com.javaacademy.cryptowallet.exception.currency.ResponseException;
import com.javaacademy.cryptowallet.exception.user.UserDoNotExistException;
import com.javaacademy.cryptowallet.exception.wallet.WalletDoesNotExistException;
import com.javaacademy.cryptowallet.exception.wallet.WalletHasNotEnoughMoney;
import com.javaacademy.cryptowallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cryptowallet")
@Tag(name = "Wallet controller", description = "Контроллер для работы со счетами криптовалют")
public class WalletController {
    private static final String USER_NOT_FOUND = "Пользователь с таким именем не найден";
    private static final String CURRENCY_DOES_NOT_SUPPORT = "Указанная валюта не поддерживается."
            + "\nПоддерживаемы валюты [BTC, SOL, ETH]";
    private static final String REFILL_SUCCESS = "Счет успешно пополнен";
    private static final String WALLET_DOES_NOT_EXIST = "Счет с указанным Id не существует";
    private static final String WALLET_HAS_NOT_ENOUGH_MONEY = "На указанном счете недостаточно денег для снятия";
    private static final String CRYPTO_RESPONSE_FAILURE = "Не удалось получить стоимость. "
            + "Ответ с криптобиржи не получен или не содержит тела ответа";
    private final WalletService walletService;

    @PostMapping
    @Operation(summary = "создает новый криптосчет", description = "принимает логин пользователя и криптовалюту счета")
    @ApiResponse(responseCode = "201", description = "Криптосчет успешно создан",
            content = @Content(schema = @Schema(implementation = UUID.class)))
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @ApiResponse(responseCode = "400", description = "Валюта не поддерживается")
    public ResponseEntity<?> create(@RequestBody CreateWalletRequestDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(walletService.save(dto));
        } catch (UserDoNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
        } catch (CurrencyDoesNotSupportException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CURRENCY_DOES_NOT_SUPPORT);
        }
    }

    @GetMapping
    @Operation(summary = "Получает список всех счетов пользователя", description = "принимает логин пользователя"
            + "в параметре и выводит список всех счетов пользователя")
    @ApiResponse(responseCode = "200", description = "Возвращает список криптосчетов",
            content = @Content(mediaType = "application.json",
                    array = @ArraySchema(schema = @Schema(implementation = WalletDto.class)))
    )
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<?> getAll(@RequestParam String username) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(walletService.findAll(username));
        } catch (UserDoNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
        }
    }

    @PostMapping("/refill")
    @Operation(summary = "Пополняет счет в рублях", description = "принимает id кошелька и сумму пополнения в рублях")
    @ApiResponse(responseCode = "200", description = "Счет успешно пополнен")
    @ApiResponse(responseCode = "404", description = "Счет для пополнения не найден")
    @ApiResponse(responseCode = "500", description = "Ошибка при получении стоимости криптовалюты")
    public ResponseEntity<String> refill(@RequestBody WalletActionDto dto) {
        try {
            walletService.deposit(dto.getAccountId(), dto.getRublesAmount());
            return ResponseEntity.ok().body(REFILL_SUCCESS);
        } catch (WalletDoesNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(WALLET_DOES_NOT_EXIST);
        } catch (ResponseException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(CRYPTO_RESPONSE_FAILURE);
        }
    }

    @PostMapping("/withdrawal")
    @Operation(summary = "Снятие рублей со счета", description = "принимает id кошелька и сумму снятия в рублях")
    @ApiResponse(responseCode = "200", description = "Успешное списание со счета")
    @ApiResponse(responseCode = "404", description = "Счет не найден")
    @ApiResponse(responseCode = "500", description = "Ошибка при получении стоимости криптовалюты")
    @ApiResponse(responseCode = "400", description = "На счете не достаточно денег для списания")
    public ResponseEntity<?> withdraw(@RequestBody WalletActionDto dto) {
        try {
            return ResponseEntity.ok().body(walletService.withdraw(dto.getAccountId(), dto.getRublesAmount()));
        } catch (WalletDoesNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(WALLET_DOES_NOT_EXIST);
        } catch (WalletHasNotEnoughMoney e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WALLET_HAS_NOT_ENOUGH_MONEY);
        } catch (ResponseException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(CRYPTO_RESPONSE_FAILURE);
        }
    }

    @GetMapping("/balance/{id}")
    @Operation(summary = "Вывод баланса счета", description = "принимает id кошелька в параметре запроса")
    @ApiResponse(responseCode = "200", description = "Вывод баланса счета в рублях",
            content = @Content(schema = @Schema(implementation = BigDecimal.class)))
    @ApiResponse(responseCode = "404", description = "Счет не найден")
    @ApiResponse(responseCode = "500", description = "Ошибка при получении стоимости криптовалюты")
    public ResponseEntity<?> getRubleWalletBalance(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok().body(walletService.showWalletBalance(id));
        } catch (WalletDoesNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(WALLET_DOES_NOT_EXIST);
        } catch (ResponseException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(CRYPTO_RESPONSE_FAILURE);
        }
    }

    @GetMapping("/balance")
    @Operation(summary = "Вывод баланса всех счетов", description = "принимает логин пользователя в параметре запроса")
    @ApiResponse(responseCode = "200", description = "Вывод баланса всех счетов в рублях",
            content = @Content(schema = @Schema(implementation = BigDecimal.class)))
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @ApiResponse(responseCode = "500", description = "Ошибка при получении стоимости криптовалюты")
    public ResponseEntity<?> getAllWalletsRubleBalance(@RequestParam String username) {
        try {
            return ResponseEntity.ok().body(walletService.showAllWalletBalance(username));
        } catch (UserDoNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
        } catch (ResponseException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(CRYPTO_RESPONSE_FAILURE);
        }
    }
}
