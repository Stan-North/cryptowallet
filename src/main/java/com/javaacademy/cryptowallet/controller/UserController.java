package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.ResetPassRequestDto;
import com.javaacademy.cryptowallet.dto.SaveUserDto;
import com.javaacademy.cryptowallet.exception.user.PasswordDoesNotMatchException;
import com.javaacademy.cryptowallet.exception.user.UserAlreadyExistException;
import com.javaacademy.cryptowallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private static final String USER_CREATED = "Пользователь успешно создан.";
    private static final String USER_ALREADY_EXIST = "Пользователь с таким логином уже существует.";
    private static final String PASSWORD_CHANGE_SUCCESS = "Пароль успешно изменен.";
    private static final String PASSWORD_DOES_NOT_MATCH = "Старый пароль не совпадает";
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SaveUserDto dto) {
        try {
            userService.save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(USER_CREATED);
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(USER_ALREADY_EXIST);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPass(@RequestBody ResetPassRequestDto dto) {
        try {
            userService.resetPassword(dto);
            return ResponseEntity.status(HttpStatus.OK).body(PASSWORD_CHANGE_SUCCESS);
        } catch (PasswordDoesNotMatchException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(PASSWORD_DOES_NOT_MATCH);
        }
    }
}
