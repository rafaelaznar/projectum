package es.blaster.projectum.repository;

import es.blaster.projectum.entity.IssueEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<IssueEntity, Long> {

    Page<IssueEntity> findByDeveloperIdAndTaskId(Long id_developer, Long id_task, Pageable oPageable);

    Page<IssueEntity> findByTaskId(Long id_task, Pageable oPageable);

    Page<IssueEntity> findByDeveloperId(Long id_developer, Pageable oPageable);
}