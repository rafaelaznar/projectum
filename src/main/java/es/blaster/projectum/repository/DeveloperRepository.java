package es.blaster.projectum.repository;

import es.blaster.projectum.entity.DeveloperEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<DeveloperEntity, Long> {

    boolean existsByUsername(String username);

    DeveloperEntity findByUsername(String username);

    Page<DeveloperEntity> findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLastnameIgnoreCaseContaining(String strFilterName, String strFilterSurname, String strFilterLast_name, Pageable oPageable);

}
