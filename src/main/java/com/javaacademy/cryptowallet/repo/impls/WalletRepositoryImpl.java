package com.javaacademy.cryptowallet.repo.impls;

import com.javaacademy.cryptowallet.data_base.WalletDb;
import com.javaacademy.cryptowallet.entity.Wallet;
import com.javaacademy.cryptowallet.exception.wallet.WalletAlreadyExistException;
import com.javaacademy.cryptowallet.repo.WalletRepository;
import com.javaacademy.cryptowallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {
    private static final String WALLET_EXIST_EXCEPTION_MESSAGE = "Счет уже существует";
    private final WalletDb walletDb;
    private final UserService userService;

    @Override
    public void save(Wallet wallet) {
        if (walletDb.getData().containsKey(wallet.getUuid())) {
            throw new WalletAlreadyExistException(WALLET_EXIST_EXCEPTION_MESSAGE);
        }
        walletDb.getData().put(wallet.getUuid(), wallet);
    }

    @Override
    public Optional<Wallet> findByID(UUID uuid) {
        return Optional.ofNullable(walletDb.getData().get(uuid));
    }

    @Override
    public Optional<List<Wallet>> findAll(String login) {
        return Optional.of(userService.findByLogin(login).getWallets());
    }
}
