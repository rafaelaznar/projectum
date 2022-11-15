package net.ausiasmarch.andamio.repository;

import net.ausiasmarch.andamio.entity.DeveloperEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<DeveloperEntity, Long> {

    boolean existsByUsername(String username);

    DeveloperEntity findByUsernameAndPassword(String username, String password);
    
    DeveloperEntity findByUsername(String username);

    Page<DeveloperEntity> findByTeamIdAndUsertypeId(Long id_team, Long id_usertype, Pageable oPageable);

    Page<DeveloperEntity> findByUsertypeId(Long id_usertype, Pageable oPageable);

    Page<DeveloperEntity> findByTeamId(Long id_team, Pageable oPageable);
                        
    Page<DeveloperEntity> findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLastnameIgnoreCaseContaining(String strFilterName, String strFilterSurname, String strFilterLast_name, Pageable oPageable);

    Page<DeveloperEntity> findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLastnameIgnoreCaseContainingAndUsertypeId(String strFilterName, String strFilterSurname, String strFilterLast_name, Long id_usertype, Pageable oPageable);

    Page<DeveloperEntity> findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLastnameIgnoreCaseContainingAndTeamId(String strFilterName, String strFilterSurname, String strFilterLast_name, Long id_team, Pageable oPageable);

    Page<DeveloperEntity> findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLastnameIgnoreCaseContainingAndTeamIdAndUsertypeId(String strFilterName, String strFilterSurname, String strFilterLast_name, Long id_team, Long id_usertype, Pageable oPageable);

    
    
}
