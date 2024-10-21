package com.ticketping.gateway.infrastructure.security;

import com.ticketping.gateway.application.dto.UserCache;
import com.ticketping.gateway.exception.ApplicationException;
import com.ticketping.gateway.infrastructure.repository.RedisRepository;
import com.ticketping.gateway.infrastructure.utils.ResponseWriter;
import com.ticketping.gateway.presentation.cases.SecurityErrorCase;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter implements ServerSecurityContextRepository {
    private Key secretKey;

    private RedisRepository redisRepository;

    public JwtFilter(@Value("${jwt.secret}") String secret, RedisRepository redisRepository, ResponseWriter responseWriter) {
        byte[] bytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(bytes);
        this.redisRepository = redisRepository;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                Claims claims = getClaims(token);
                String userId = claims.getSubject();

                UserCache userCache = Optional.ofNullable(
                        redisRepository.getValueAsClass(userId, UserCache.class)
                ).orElseThrow(() -> new RuntimeException("캐시 없음"));

                Collection<GrantedAuthority> roleCollection = List.of(userCache::role);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, roleCollection);

                exchange.mutate()
                        .request(r -> r.headers(headers -> {
                            headers.add("X-User-Id", userId);
                            headers.add("X-User-Role", userCache.role());
                        }))
                        .build();

                return Mono.just(new SecurityContextImpl(authentication));

            }

            return Mono.empty();
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new ApplicationException(SecurityErrorCase.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new ApplicationException(SecurityErrorCase.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new ApplicationException(SecurityErrorCase.UNSUPPORTED_AUTHENTICATION);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(SecurityErrorCase.ILLEGAL_CLAIM);
        }
    }

}
