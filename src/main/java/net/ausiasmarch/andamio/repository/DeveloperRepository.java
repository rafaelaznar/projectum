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

    Page<DeveloperEntity> findByNameIgnoreCaseContaining(String strFilter, Pageable oPageable);

    Page<DeveloperEntity> findByNameIgnoreCaseContainingAndUsertypeId(String strFilter, Long id_usertype, Pageable oPageable);

    Page<DeveloperEntity> findByNameIgnoreCaseContainingAndTeamId(String strFilter, Long id_team, Pageable oPageable);

    Page<DeveloperEntity> findByNameIgnoreCaseContainingAndTeamIdAndUsertypeId(String strFilter, Long id_team, Long id_usertype, Pageable oPageable);

}
