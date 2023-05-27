package es.blaster.projectum.repository;

import es.blaster.projectum.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Page<ProjectEntity> findAll(Pageable oPageable);

    Page<ProjectEntity> findByCodeIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String strFilterCode, String strFilterDesc, Pageable oPageable);

}
