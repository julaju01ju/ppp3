package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.security.jwt.JwtUtil;
import com.javamentor.qa.platform.security.dto.AuthenticationRequest;
import com.javamentor.qa.platform.security.dto.JwtTokenDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class AuthenticationResourceController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDao userDAO;

    public AuthenticationResourceController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserDao userDAO) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDAO = userDAO;
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
            jwtTokenDTO.setToken(jwtUtil.generateAccessToken(userDAO.getUserByEmail(userDetails.getUsername()).get()));
            return new ResponseEntity<>(jwtTokenDTO, HttpStatus.OK);
        } catch (BadCredentialsException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Имя или пароль неправильны", exception);
        }
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
