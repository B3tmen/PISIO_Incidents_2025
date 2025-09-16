package org.unibl.etf.config_server.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static javax.xml.crypto.dsig.SignatureMethod.HMAC_SHA256;

@Component
public class GithubWebhookFilter extends OncePerRequestFilter {
    private static final String BUSREFRESH_ENDPOINT = "/config/actuator/busrefresh";

    @Value("${git.webhook.secret}")
    private String secret;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //System.out.println(request.getRequestURI());

        if(!"POST".equalsIgnoreCase(request.getMethod()) ||
           !BUSREFRESH_ENDPOINT.equalsIgnoreCase(request.getRequestURI()) ) {
            filterChain.doFilter(request, response);
            return;
        }

        String secretHeader = request.getHeader("X-Hub-Signature-256");
        System.out.println(secretHeader);
        if(secretHeader == null || secretHeader.startsWith("sha256=")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Missing X-Hub-Signature-256");
            return;
        }

        if(secret.equals(secretHeader)) {
            // Signature valid, continue filter chain
            filterChain.doFilter(request, response);
            return;
        }
        else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid signature");
            return;
        }
    }

    private String hmacSha256(String data, String key) throws Exception {
        Mac mac = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
        mac.init(secretKeySpec);
        byte[] hmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hmac);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
