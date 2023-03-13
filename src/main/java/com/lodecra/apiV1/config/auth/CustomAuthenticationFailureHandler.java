package com.lodecra.apiV1.config.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j(topic = "LoDeCraSecurity")
public class CustomAuthenticationFailureHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        var encabezadoAuth=request.getHeader("authorization");
        if (null!=encabezadoAuth) {
            var stringBase64 = encabezadoAuth.split("Basic ");
            var usuarioYPass = new String(Base64.getDecoder().decode(stringBase64[1]), StandardCharsets.UTF_8);
            var usuario = usuarioYPass.split(":")[0];
            log.error("El usuario " + usuario + " no está autenticado correctamente.");
        } else {
            response.setStatus(401);
            log.error("Debe autenticarse para acceder.");
        }
    }
}
