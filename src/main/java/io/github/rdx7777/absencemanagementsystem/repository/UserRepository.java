package io.github.rdx7777.absencemanagementsystem.repository;

import io.github.rdx7777.absencemanagementsystem.model.User;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    @Query(value="SELECT * FROM users u ORDER BY u.id offset ?1 limit ?2", nativeQuery = true)
    Collection<User> findAllByOffsetAndLimit(Long offset, Long limit);

}
