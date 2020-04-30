package io.github.rdx7777.absencemanagementsystem.repository;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceCaseRepository extends JpaRepository<AbsenceCase, Long> {

    @Query(value="SELECT * FROM absence_case ac ORDER BY ac.id offset ?1 limit ?2", nativeQuery = true)
    Collection<AbsenceCase> findAllByOffsetAndLimit(@Param("offset") Long offset, @Param("limit") Long limit);
}
