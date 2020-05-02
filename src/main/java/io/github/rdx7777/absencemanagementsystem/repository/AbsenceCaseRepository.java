package io.github.rdx7777.absencemanagementsystem.repository;

import io.github.rdx7777.absencemanagementsystem.model.AbsenceCase;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceCaseRepository extends JpaRepository<AbsenceCase, Long> {

    @Query(value="SELECT * FROM absence_case ac ORDER BY ac.id offset ?1 limit ?2", nativeQuery = true)
    Collection<AbsenceCase> findAllByOffsetAndLimit(Long offset, Long limit);

    @Query(value="SELECT * FROM absence_case ac WHERE ac.is_case_resolved = false ORDER BY ac.id offset ?1 limit ?2", nativeQuery = true)
    Collection<AbsenceCase> findAllActiveByOffsetAndLimit(Long offset, Long limit);

    @Query(value="SELECT * FROM absence_case ac WHERE ac.is_case_resolved = false AND ac.head_teacher_id = ?1 ORDER BY ac.id offset ?2 limit ?3", nativeQuery = true)
    Collection<AbsenceCase> findAllActiveManagedByHeadTeacherByOffsetAndLimit(Long headTeacherId, Long offset, Long limit);

    @Query(value="SELECT * FROM absence_case ac WHERE ac.user_id = ?1 ORDER BY ac.id offset ?2 limit ?3", nativeQuery = true)
    Collection<AbsenceCase> findAllByUserIdByOffsetAndLimit(Long userId, Long offset, Long limit);
}
