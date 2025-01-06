package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.ResetPassRequestDto;
import com.javaacademy.cryptowallet.dto.SaveUserDto;
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
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SaveUserDto dto) {
        userService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPass(@RequestBody ResetPassRequestDto dto) {
        userService.resetPassword(dto);
        return ResponseEntity.ok().build();
        //подумать над возвращением статуса при ошибке
    }
}
