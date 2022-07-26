package com.devcourse.checkmoi.domain.user.repository;

import java.util.Optional;

import com.devcourse.checkmoi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthId(String oauthId);

}
