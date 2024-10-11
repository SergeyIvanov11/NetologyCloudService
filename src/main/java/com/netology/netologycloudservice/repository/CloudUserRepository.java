package com.netology.netologycloudservice.repository;

import com.netology.netologycloudservice.dao.CloudUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CloudUserRepository extends JpaRepository<CloudUser, Long> {
    @Query("SELECT u FROM CloudUser u WHERE u.email = :email")
    Optional<CloudUser> findByEmail(@Param("email") String email);
    @Query("SELECT u FROM CloudUser u WHERE u.phoneNumber = :phoneNumber")
    Optional<CloudUser> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    @Query("SELECT u FROM CloudUser u WHERE u.username = :username")
    Optional<CloudUser> findByUsername(@Param("username") String username);
}
