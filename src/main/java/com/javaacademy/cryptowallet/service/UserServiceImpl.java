package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.dto.ResetPassRequestDto;
import com.javaacademy.cryptowallet.dto.SaveUserDto;
import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.exception.user.PasswordDoesNotMatchException;
import com.javaacademy.cryptowallet.exception.user.UserDoNotExistException;
import com.javaacademy.cryptowallet.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String PASSWORD_DOES_NOT_MATCH = "Failed to change password, old passwords do not match";
    private final UserRepository repository;

    @Override
    public void save(SaveUserDto saveUserDto) {
        repository.save(saveUserDto);
    }

    @Override
    public User getByLogin(String login) {
        return repository.findByLogin(login).orElseThrow(UserDoNotExistException::new);
    }

    @Override
    public void resetPassword(ResetPassRequestDto dto) {
        User user = getByLogin(dto.getLogin());
        if (!user.getPassword().equals(dto.getOldPassword())) {
            throw new PasswordDoesNotMatchException(PASSWORD_DOES_NOT_MATCH);
        }
        user.setPassword(dto.getNewPassword());
    }
}
