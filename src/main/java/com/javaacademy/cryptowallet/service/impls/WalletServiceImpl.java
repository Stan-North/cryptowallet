package com.javaacademy.cryptowallet.service.impls;

import com.javaacademy.cryptowallet.dto.CreateWalletRequestDto;
import com.javaacademy.cryptowallet.dto.WalletDto;
import com.javaacademy.cryptowallet.entity.enums.Currency;
import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.entity.Wallet;
import com.javaacademy.cryptowallet.exception.wallet.WalletDoesNotExistException;
import com.javaacademy.cryptowallet.exception.wallet.WalletHasNotEnoughMoney;
import com.javaacademy.cryptowallet.mapper.WalletMapper;
import com.javaacademy.cryptowallet.repo.WalletRepository;
import com.javaacademy.cryptowallet.service.CurrencyService;
import com.javaacademy.cryptowallet.service.RubleConverterService;
import com.javaacademy.cryptowallet.service.UserService;
import com.javaacademy.cryptowallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private static final String NOT_ENOUGH_MONEY_MESSAGE = "There are not enough money in the account. "
            + "Operation rejected";
    private static final String SUCCESS_WITHDRAW_MESSAGE = "Операция прошла успешно. Продано %s %s";
    private final WalletRepository repository;
    private final UserService userService;
    private final CurrencyService currencyService;
    private final RubleConverterService rubleConverterService;
    private final WalletMapper walletMapper;

    @Override
    public WalletDto findByUuid(UUID uuid) {
        Wallet wallet = repository.findByID(uuid).orElseThrow(WalletDoesNotExistException::new);
        return walletMapper.toDto(wallet);
    }

    @Override
    public List<WalletDto> findAll(String login) {
        List<Wallet> wallets = repository.findAll(login).orElseThrow(WalletDoesNotExistException::new);
        return wallets.stream().map(walletMapper::toDto).toList();
    }

    @Override
    public UUID save(CreateWalletRequestDto dto) {
        User user = userService.findByLogin(dto.getUserName());
        Wallet wallet = walletMapper.requestToWallet(dto);
        user.getWallets().add(wallet);
        repository.save(wallet);
        return wallet.getUuid();
    }

    @Override
    public void deposit(UUID uuid, BigDecimal rubAmount) {
        Wallet wallet = repository.findByID(uuid).orElseThrow(WalletDoesNotExistException::new);
        BigDecimal currencyAmountInWallet = currencyAmountInRuble(uuid, rubAmount);
        wallet.setAmount(wallet.getAmount().add(currencyAmountInWallet));
    }

    @Override
    public String withdraw(UUID uuid, BigDecimal rubAmount) {
        Wallet wallet = getWalletFromRep(uuid);
        BigDecimal currencyAmountInWallet = currencyAmountInRuble(uuid, rubAmount);
        BigDecimal moneyOnWallet = wallet.getAmount();
        if (moneyOnWallet.compareTo(currencyAmountInWallet) < 0) {
            throw new WalletHasNotEnoughMoney(NOT_ENOUGH_MONEY_MESSAGE);
        }
        wallet.setAmount(wallet.getAmount().subtract(currencyAmountInWallet));
        return String.format(SUCCESS_WITHDRAW_MESSAGE, currencyAmountInWallet, wallet.getCurrency());
    }

    @Override
    public BigDecimal showWalletBalance(UUID uuid) {
        Wallet wallet = getWalletFromRep(uuid);
        BigDecimal currencyAmountInWallet = wallet.getAmount();
        BigDecimal currencyUsdPrice = findCurrencyUsdPriceByWalletId(uuid);
        BigDecimal totalUsdPrice = currencyUsdPrice.multiply(currencyAmountInWallet);
        return rubleConverterService.toRub(totalUsdPrice);
    }

    @Override
    public BigDecimal showAllWalletBalance(String login) {
        BigDecimal result = BigDecimal.ZERO;
        for (Wallet wallet : getAllWalletsFromRep(login)) {
            result = result.add(showWalletBalance(wallet.getUuid()));
        }
        return result;
    }

    private BigDecimal findCurrencyUsdPriceByWalletId(UUID uuid) {
        Wallet wallet = getWalletFromRep(uuid);
        Currency currency = wallet.getCurrency();
        return currencyService.getUsdPrice(currency);
    }

    private Wallet getWalletFromRep(UUID uuid) {
        return repository.findByID(uuid).orElseThrow(WalletDoesNotExistException::new);
    }

    private List<Wallet> getAllWalletsFromRep(String login) {
        return repository.findAll(login).orElseThrow(WalletDoesNotExistException::new);
    }

    private BigDecimal currencyAmountInRuble(UUID uuid, BigDecimal rubAmount) {
        BigDecimal currencyUsdPrice = findCurrencyUsdPriceByWalletId(uuid);
        BigDecimal usdAmount = rubleConverterService.toUsd(rubAmount);
        return usdAmount.divide(currencyUsdPrice, RoundingMode.DOWN);
    }
}
