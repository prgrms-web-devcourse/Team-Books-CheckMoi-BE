package com.devcourse.checkmoi.domain.token.repository;


import com.devcourse.checkmoi.domain.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findTokenByUserId(long userId);

    void deleteByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
