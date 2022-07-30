package com.devcourse.checkmoi.domain.token.repository;


import com.devcourse.checkmoi.domain.token.model.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findTokenByUserId(long userId);

    void deleteByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
