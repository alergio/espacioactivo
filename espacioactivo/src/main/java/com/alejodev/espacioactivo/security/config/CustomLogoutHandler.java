package com.alejodev.espacioactivo.security.config;
import com.alejodev.espacioactivo.entity.Token;
import com.alejodev.espacioactivo.repository.impl.ITokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final ITokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String jwtToken = authHeader.substring(7);

        // buscar el token guardado en la bd
        // invalidarlo y guardarlo
        Token storedToken = tokenRepository.findByToken(jwtToken).orElseThrow(() -> new DataIntegrityViolationException("BAD REQUEST"));

        if (storedToken != null){
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
        }
    }

}