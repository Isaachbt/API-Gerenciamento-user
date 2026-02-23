package com.geren.users.service.imp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.geren.users.exception.NotFound;
import com.geren.users.exception.PasswordIncorreta;
import com.geren.users.exception.UserNotCadastrado;
import com.geren.users.model.User;
import com.geren.users.dto.LoginDTO;
import com.geren.users.repository.UserRepository;
import com.geren.users.service.AuthenticationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class AuthenticationServiceImp implements AuthenticationService {
    private static final String ISSUER = "API Gerenciamento user";
    private static final String SECRET = "my-secret";

    @Autowired
    private UserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      User user = repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user == null) {
            throw new UserNotCadastrado("Credenciais inválidas para o usuário: " + username);
        }

        return (UserDetails) user;
    }

    @Override
    public String login(LoginDTO dto) throws NotFound, PasswordIncorreta {
        return obterToken(dto);
    }

    @Override
    public String obterToken(LoginDTO dto) {
        var user = new User();
        BeanUtils.copyProperties(dto,user);
        return gerarTokenJwt(user);
    }

    @Override
    public String validToken(String token) {
        try{

            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException e){
            return "";
        }
    }

    @Override
    public String gerarTokenJwt(User user) {
        try{

            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(gerarDateExpiracao())
                    .sign(algorithm);
        }catch (JWTCreationException e){
            throw new RuntimeException("Erro ao tentar gerar o token");
        }
    }

    private Instant gerarDateExpiracao(){
        return LocalDateTime.now()
                .plusHours(8)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public String getLoginFromExpiredToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");
            return JWT.require(algorithm)
                    .withIssuer("API Gerenciamento user")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (TokenExpiredException e) {
            return JWT.decode(token).getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token inválido", e);
        }
    }

}
