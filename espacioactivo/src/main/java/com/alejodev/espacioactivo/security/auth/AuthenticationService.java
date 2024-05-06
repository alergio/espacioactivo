package com.alejodev.espacioactivo.security.auth;

import com.alejodev.espacioactivo.entity.RoleType;
import com.alejodev.espacioactivo.entity.Token;
import com.alejodev.espacioactivo.entity.User;
import com.alejodev.espacioactivo.repository.impl.IRoleRepository;
import com.alejodev.espacioactivo.repository.impl.ITokenRepository;
import com.alejodev.espacioactivo.repository.impl.IUserRepository;
import com.alejodev.espacioactivo.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final ITokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest registerRequest) throws Exception {

        // probablemente el register puedo hacer que no devuelva token, solo registre

        var roleUser = roleRepository.findByName(RoleType.ROLE_CUSTOMER)
                .orElseThrow(() -> new Exception("FAIL ROLE"));

        var user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(Collections.singleton(roleUser))
                .isEnabled(registerRequest.isEnabled())
                .registrationDate(registerRequest.getRegistrationDate())
                .build();

        userRepository.save(user);

        var jwt = jwtService.generateToken(user);

        saveUserToken(jwt, user);

        return AuthenticationResponse
                .builder()
                .jwt(jwt)
                .build();
    }


    public AuthenticationResponse logIn(AuthenticationRequest authenticationRequest){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        var user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        var jwt = jwtService.generateToken(user);

        blockAllTokenByUser(user);
        saveUserToken(jwt, user);

        return AuthenticationResponse.builder()
                .jwt(jwt)
                .build();
    }

    private void blockAllTokenByUser(User user) {
        List<Token> validTokenListByUser = tokenRepository.findAllTokenByUser(user.getId());
        if(!validTokenListByUser.isEmpty()){
            validTokenListByUser.forEach(token->{
                token.setLoggedOut(true);
            });
        }
    }

    private void saveUserToken(String jwt, User user){

        // guardamos el token generado
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);

        tokenRepository.save(token);

    }

    public static String getUserName() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si el usuario está autenticado
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        } else {
            throw new AuthenticationCredentialsNotFoundException("");
        }

    }

    // aca puedo agregar un refreshToken
    // https://youtu.be/EjrlN_OQVDQ min: 59:50

}
