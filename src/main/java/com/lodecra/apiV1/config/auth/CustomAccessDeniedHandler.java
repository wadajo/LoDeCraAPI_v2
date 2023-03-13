package com.lodecra.apiV1.config.auth;

import com.lodecra.apiV1.util.Utilidades;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j(topic = "LoDeCraSecurity")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final Utilidades util;

    public CustomAccessDeniedHandler(Utilidades util) {
        this.util = util;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        log.error(accessDeniedException.getMessage()+". El usuario "+util.usuarioAutenticado()+" se ha autenticado pero no tiene permisos para realizar esta operación.");
        response.setStatus(403);
    }
}
