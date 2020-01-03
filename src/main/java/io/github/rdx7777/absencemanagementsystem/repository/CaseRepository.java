package io.github.rdx7777.absencemanagementsystem.repository;

import io.github.rdx7777.absencemanagementsystem.model.Case;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
}
