package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.models.dto.JwtTokenDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.security.jwt.JwtUtil;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class AuthenticationResourceController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
//    private final UserService userService;

    public AuthenticationResourceController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
//        this.userService = userService;
    }

    @PostMapping("/auth/token/")
    @ApiOperation("Возвращает строку токена в виде объекта JwtTokenDto, на вход получает объект AuthenticationRequest, который содержит username и password")
    public ResponseEntity<JwtTokenDto> getToken(@RequestBody AuthenticationRequest request)
    {
        JwtTokenDto jwtTokenDTO = new JwtTokenDto();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            тогда у меня тут 2 вариант решения
//            и, как понимаю, лучше использовать через userDetails, а userService вообще удалить.
//            И метод getUserByEmail тогда можно не писать, раз мы берем пользователя из контекста
            jwtTokenDTO.setToken(jwtUtil.generateAccessToken((User) userDetails));
//            jwtTokenDTO.setToken(jwtUtil.generateAccessToken(userService.getUserByEmail(userDetails.getUsername()).get()));
            return new ResponseEntity<>(jwtTokenDTO, HttpStatus.OK);
        } catch (BadCredentialsException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Имя или пароль неправильны", exception);
        }
    }

    @GetMapping("/user/check_auth")
    public ResponseEntity<Void> checkAuthorization() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_USER")) {
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/testuser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> usertest() {
        return new ResponseEntity<>("API USER TEST", HttpStatus.OK);
    }

    @GetMapping("/testadmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> admintest() {
        return new ResponseEntity<>("API ADMIN TEST", HttpStatus.OK);
    }
}
