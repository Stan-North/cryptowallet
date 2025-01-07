package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.CreateWalletRequestDto;
import com.javaacademy.cryptowallet.dto.WalletActionDto;
import com.javaacademy.cryptowallet.exception.currency.CurrencyDoesNotSupportException;
import com.javaacademy.cryptowallet.exception.currency.ResponseException;
import com.javaacademy.cryptowallet.exception.user.UserDoNotExistException;
import com.javaacademy.cryptowallet.exception.wallet.WalletDoesNotExistException;
import com.javaacademy.cryptowallet.exception.wallet.WalletHasNotEnoughMoney;
import com.javaacademy.cryptowallet.service.WalletService;
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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cryptowallet")
public class WalletController {
    private static final String USER_NOT_FOUND = "Пользователь с таким именем не найден";
    private static final String CURRENCY_DOES_NOT_SUPPORT = "Указанная валюта не поддерживается."
            + "\nПоддерживаемы валюты [BTC, SOL, ETH]";
    private static final String WALLET_REFILL_SUCCESS_MESSAGE = "Счет успешно создан. ID созданного кошелька:\n";
    private static final String REFILL_SUCCESS = "Счет успешно пополнен";
    private static final String WALLET_DOES_NOT_EXIST = "Счет с указанным Id не существует";
    private static final String WALLET_HAS_NOT_ENOUGH_MONEY = "На указанном счете недостаточно денег для снятия";
    private static final String WALLET_BALANCE = "Баланс счета: ";
    private static final String ALL_WALLET_BALANCE = "Баланс всех счетов: ";
    private static final String CRYPTO_RESPONSE_FAILURE = "Не удалось получить стоимость. "
            + "Ответ с криптобиржи не получен или не содержит тела ответа";
    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody CreateWalletRequestDto dto) {
        try {
            UUID uuid = walletService.save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(WALLET_REFILL_SUCCESS_MESSAGE + uuid.toString());
        } catch (UserDoNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
        } catch (CurrencyDoesNotSupportException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CURRENCY_DOES_NOT_SUPPORT);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam String username) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(walletService.findAll(username));
        } catch (UserDoNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
        }
    }

    @PostMapping("/refill")
    public ResponseEntity<String> refill(@RequestBody WalletActionDto dto) {
        try {
            walletService.deposit(dto.getAccountId(), dto.getRublesAmount());
            return ResponseEntity.ok().body(REFILL_SUCCESS);
        } catch (WalletDoesNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(WALLET_DOES_NOT_EXIST);
        } catch (ResponseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CRYPTO_RESPONSE_FAILURE);
        }
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<?> withdraw(@RequestBody WalletActionDto dto) {
        try {
            return ResponseEntity.ok().body(walletService.withdraw(dto.getAccountId(), dto.getRublesAmount()));
        } catch (WalletDoesNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(WALLET_DOES_NOT_EXIST);
        } catch (WalletHasNotEnoughMoney e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WALLET_HAS_NOT_ENOUGH_MONEY);
        } catch (ResponseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CRYPTO_RESPONSE_FAILURE);
        }
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<String> getRubleWalletBalance(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok().body(WALLET_BALANCE + walletService.showWalletBalance(id));
        } catch (WalletDoesNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(WALLET_DOES_NOT_EXIST);
        } catch (ResponseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CRYPTO_RESPONSE_FAILURE);
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getAllWalletsRubleBalance(@RequestParam String username) {
        try {
            return ResponseEntity.ok().body(ALL_WALLET_BALANCE + walletService.showAllWalletBalance(username));
        } catch (UserDoNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
        } catch (ResponseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CRYPTO_RESPONSE_FAILURE);
        }
    }
}
