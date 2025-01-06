package com.javaacademy.cryptowallet.repo;

import com.javaacademy.cryptowallet.data_base.UsersDb;
import com.javaacademy.cryptowallet.dto.SaveUserDto;
import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.exception.user.UserAlreadyExistException;
import com.javaacademy.cryptowallet.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private static final String USER_EXIST_EXCEPTION_MESSAGE = "Error saving user. This user already exists";
    private final UsersDb usersDb;
    private final UserMapper mapper;

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(usersDb.getData().get(login));
    }

    @Override
    public void save(SaveUserDto saveUserDto) {
        if (usersDb.getData().containsKey(saveUserDto.getLogin())) {
            throw new UserAlreadyExistException(USER_EXIST_EXCEPTION_MESSAGE);
        }
        User user = mapper.toUser(saveUserDto);
        usersDb.getData().put(user.getLogin(), user);
    }
}
