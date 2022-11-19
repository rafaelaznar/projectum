package net.ausiasmarch.andamio.repository;

import net.ausiasmarch.andamio.entity.HelpEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpRepository extends JpaRepository<HelpEntity, Long> {

    Page<HelpEntity> findByDeveloperIdAndResolutionId(Long id_developer, Long id_resolution, Pageable oPageable);

    Page<HelpEntity> findByResolutionId(Long id_resolution, Pageable oPageable);

    Page<HelpEntity> findByDeveloperId(Long id_developer, Pageable oPageable);

}