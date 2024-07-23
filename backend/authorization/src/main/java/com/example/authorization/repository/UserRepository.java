package com.example.authorization.repository;

import com.example.authorization.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String username);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUuid(String uuid);
    @Query(nativeQuery = true, value = "SELECT * FROM users where login=?1 and islock=false and isenable=true")
    Optional<User> findUserByLoginAndLock(String username);
}
