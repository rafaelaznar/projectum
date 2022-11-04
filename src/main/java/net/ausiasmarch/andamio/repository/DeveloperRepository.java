package net.ausiasmarch.andamio.repository;

import net.ausiasmarch.andamio.entity.DeveloperEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<DeveloperEntity, Long> {

    boolean existsByUsername(String username);

    DeveloperEntity findByUsernameAndPassword(String username, String password);

    Page<DeveloperEntity> findByTeamIdAndUsertypeId(Long id_team, Long id_usertype, Pageable oPageable);

    Page<DeveloperEntity> findByUsertypeId(Long id_usertype, Pageable oPageable);

    Page<DeveloperEntity> findByTeamId(Long id_team, Pageable oPageable);

    Page<DeveloperEntity> findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLast_nameIgnoreCaseContaining(String strFilterName, String strFilterSurname, String strFilterLast_name, Pageable oPageable);

    Page<DeveloperEntity> findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLast_nameIgnoreCaseContainingAndUsertypeId(String strFilterName, String strFilterSurname, String strFilterLast_name, Long id_usertype, Pageable oPageable);

    Page<DeveloperEntity> findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLast_nameIgnoreCaseContainingAndTeamId(String strFilterName, String strFilterSurname, String strFilterLast_name, Long id_team, Pageable oPageable);

    Page<DeveloperEntity> findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLast_nameIgnoreCaseContainingAndTeamIdAndUsertypeId(String strFilterName, String strFilterSurname, String strFilterLast_name, Long id_team, Long id_usertype, Pageable oPageable);

}
