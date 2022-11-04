package net.ausiasmarch.andamio.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ausiasmarch.andamio.entity.DeveloperEntity;
import net.ausiasmarch.andamio.entity.ResolutionEntity;
import net.ausiasmarch.andamio.exception.CannotPerformOperationException;
import net.ausiasmarch.andamio.helper.RandomHelper;
import net.ausiasmarch.andamio.repository.ResolutionRepository;


@Service
public class ResolutionService {

    
    @Autowired
    public ResolutionService(ResolutionRepository oResolutionRepository, AuthService oAuthService) {
        this.oResolutionRepository = oResolutionRepository;
        this.oAuthService = oAuthService;
    }

    private final ResolutionRepository oResolutionRepository;
    private final AuthService oAuthService;

    @Autowired
    DeveloperService oDeveloperService;

    @Autowired
    IssueService oIssueService;

    
    public ResolutionEntity get(Long id) {
        oAuthService.OnlyAdmins();
        return oResolutionRepository.getById(id);
    }

    public Long count() {
        oAuthService.OnlyAdmins();
        return oResolutionRepository.count();
    }
    
    public Page<ResolutionEntity> getPage(String strFilter, Long id_issue, Long id_developer, Pageable oPageable) {
        oAuthService.OnlyAdmins();
        if (strFilter == null && id_issue == null && id_developer == null) {
            return oResolutionRepository.findAll(oPageable);
        } else if (id_developer == null && id_issue == null) {
            return oResolutionRepository.findByObservationsIgnoreCaseContaining(strFilter, oPageable);
        } else if (strFilter == null && id_developer == null) {
            return oResolutionRepository.findByIssueId(id_issue, oPageable);
        } else if (strFilter == null && id_issue == null) {
            return oResolutionRepository.findByDeveloperId(id_developer, oPageable);
        } else if (id_developer == null) {
            return oResolutionRepository.findByObservationsIgnoreCaseContainingAndIssueId(strFilter, id_issue, oPageable);
        } else if (id_issue == null) {
            return oResolutionRepository.findByObservationsIgnoreCaseContainingAndDeveloperId(strFilter, id_developer, oPageable);
        } else if (strFilter == null) {
            return oResolutionRepository.findByIssueIdAndDeveloperId(id_issue, id_developer, oPageable);
        } else {
            return oResolutionRepository.findByObservationsIgnoreCaseContainingAndIssueIdAndDeveloperId(strFilter, id_issue, id_developer, oPageable);
        }
    }

    public ResolutionEntity generate() {
        if (oResolutionRepository.count() > 0) {
            String [] observacionesLista = {"Proyecto dedicado a la sanidad", "Falta actualizar la API", "Mejorar el diagrama de Entidad - Relacion"
            , "Hacer imagen con los recursos necesarios para el despliegue", "GIT: incorporar rama Desarrollo a Produccion"};
            String [] pullRequestUrls = {"http://localhost:8082/developer", "http://localhost:8082/issue", "http://localhost:8082/project",
            "http://localhost:8082/resolution"};
            String pullRequestUrl = pullRequestUrls[(int) (Math.floor(Math.random() * ((pullRequestUrls.length - 1) - 0 + 1) + 0))];
            String observacion = observacionesLista[(int) (Math.floor(Math.random() * ((observacionesLista.length - 1) - 0 + 1) + 0))];
            ResolutionEntity oResolutionEntity = new ResolutionEntity();
            oResolutionEntity.setDeveloper(oDeveloperService.getOneRandom());
            oResolutionEntity.setIntegration_datetime(RandomHelper.getRadomDateTime());
            oResolutionEntity.setIntegration_turn(RandomHelper.getRandomInt(1, 10));
            oResolutionEntity.setIssue(oIssueService.getOneRandom());
            oResolutionEntity.setObservations(observacion);
            oResolutionEntity.setPullrequest_url(pullRequestUrl);
            oResolutionEntity.setValue(RandomHelper.getRandomInt(1, 10));
            return oResolutionEntity;
        } else {
            return null;
        }
    }

    public Long generateSome(int amount) {
        oAuthService.isAdmin();
        if (oIssueService.count() > 0) {
            for (int i = 0; i < amount; i++) {
                ResolutionEntity oResolutionEntity = generate();
                oResolutionRepository.save(oResolutionEntity);
            }
            return oResolutionRepository.count();
        } else {
            throw new CannotPerformOperationException("no hay usuarios en la base de datos");
        }
    }

    public ResolutionEntity getOneRandom() {
        ResolutionEntity oTipoProductoEntity = null;
        int iPosicion = RandomHelper.getRandomInt(0, (int) oResolutionRepository.count() - 1);
        Pageable oPageable = PageRequest.of(iPosicion, 1);
        Page<ResolutionEntity> tipoProductoPage = oResolutionRepository.findAll(oPageable);
        List<ResolutionEntity> tipoProductoList = tipoProductoPage.getContent();
        oTipoProductoEntity = oResolutionRepository.getById(tipoProductoList.get(0).getId());
        return oTipoProductoEntity;
    }

    public void validate(Long id){
        if (!oResolutionRepository.existsById(id)) {
            throw new CannotPerformOperationException("id " + id + " not exist");
        }
    }
    
    public Long update(Long id, ResolutionEntity oResolutionEntity) {
        oAuthService.OnlyAdmins();
        //oResolutionEntity.setId(id);
        //validate(id);
        //validate(oResolutionEntity);
        return oResolutionRepository.save(oResolutionEntity).getId();
    }
}
