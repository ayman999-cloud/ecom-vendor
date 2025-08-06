package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/profile")
    public ResponseEntity<User> getUserByJwt(@RequestHeader("Authorization") final String jwt) throws Exception {
        final User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(user);
    }
}
