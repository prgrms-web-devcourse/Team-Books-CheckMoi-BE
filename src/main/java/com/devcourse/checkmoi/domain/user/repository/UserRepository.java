package com.devcourse.checkmoi.domain.user.repository;

import com.devcourse.checkmoi.domain.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauthId(String oauthId);

}
