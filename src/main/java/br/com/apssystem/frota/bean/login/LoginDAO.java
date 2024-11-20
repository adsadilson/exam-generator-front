package br.com.apssystem.frota.bean.login;

import br.com.apssystem.frota.annotation.ExceptionHandler;
import br.com.apssystem.frota.bean.login.custom.CustomRestTemplate;
import br.com.apssystem.frota.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Map;

import static br.com.apssystem.frota.util.ApiUtil.LOGIN_URL;


public class LoginDAO implements Serializable {
    private final CustomRestTemplate restTemplate;
    private final JsonUtil jsonUtil;

    @Inject
    public LoginDAO(CustomRestTemplate restTemplate, JsonUtil jsonUtil) {
        this.restTemplate = restTemplate;
        this.jsonUtil = jsonUtil;
    }

    @ExceptionHandler
    public Token loginReturningToken(String username, String password) {
        try {
            // Objeto de login
            Map<String, String> loginPayload = Map.of(
                    "username", username,
                    "password", password
            );

            // Converte o payload para JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String loginJson = objectMapper.writeValueAsString(loginPayload);

            // Cabeçalho JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Entidade HTTP
            HttpEntity<String> requestEntity = new HttpEntity<>(loginJson, headers);

            // Realiza a chamada REST
            ResponseEntity<Token> response = restTemplate.exchange(
                    LOGIN_URL,
                    HttpMethod.POST,
                    requestEntity,
                    Token.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            // Trata erros HTTP (401, 403, etc.)
            throw new RuntimeException("Erro ao realizar login: " + e.getStatusCode(), e);
        } catch (Exception e) {
            // Trata outros erros
            throw new RuntimeException("Erro inesperado ao realizar login", e);
        }
    }


}
