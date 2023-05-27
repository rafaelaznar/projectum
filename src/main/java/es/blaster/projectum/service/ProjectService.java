package es.blaster.projectum.service;

import es.blaster.projectum.entity.ProjectEntity;
import es.blaster.projectum.exception.CannotPerformOperationException;
import es.blaster.projectum.exception.ResourceNotFoundException;
import es.blaster.projectum.helper.RandomHelper;
import es.blaster.projectum.helper.ValidationHelper;
import es.blaster.projectum.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final String[] DESCRIPTION = {"Example12", "Example13", "Example14",
        "Example15", "Example16", "Example17", "Example18", "Example19", "Example20", "Example21"};

    private final String[] CODE = {"DAW2021", "DAW2022", "DAW2023",
        "SMR2021", "SMR2022", "SMR2023", "ASIR2021", "ASIR2022", "ASIR2023", "DAM2022"};

    @Autowired
    AuthService oAuthService;

    @Autowired
    ProjectRepository oProjectRepository;

    public void validate(Long id) {
        if (!oProjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("id " + id + " not exist");
        }
    }

    private void validate(ProjectEntity oProjectEntity) {
        ValidationHelper.validateStringLength(oProjectEntity.getDescription(), 2, 255, "campo project_description de Project(el campo debe tener longitud de 2 a 255 caracteres)");
        ValidationHelper.validateStringLength(oProjectEntity.getCode(), 2, 255, "campo project_code de Project(el campo debe tener longitud de 2 a 255 caracteres)");
        ValidationHelper.validateStringLength(oProjectEntity.getUrl(), 2, 255, "campo url de Project(el campo debe tener longitud de 2 a 255 caracteres)");
    }

    public ProjectEntity get(Long id) {
        oAuthService.OnlyAdminsOrViewers();
        return oProjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project with id: " + id + " not found"));

    }

    public Page<ProjectEntity> getPage(Pageable oPageable, String strFilter) {
        oAuthService.OnlyAdminsOrViewers();
        Page<ProjectEntity> oPage = null;
        if (strFilter == null || strFilter.isEmpty() || strFilter.trim().isEmpty()) {
            oPage = oProjectRepository.findAll(oPageable);
        } else {
            oPage = oProjectRepository.findByCodeIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(strFilter, strFilter, oPageable);
        }

        return oPage;
    }

    public Long count() {
        oAuthService.OnlyAdminsOrViewers();
        return oProjectRepository.count();
    }

    public Long create(ProjectEntity oProjectEntity) {
        oAuthService.OnlyAdmins();
        validate(oProjectEntity.getId());
        validate(oProjectEntity);
        return oProjectRepository.save(oProjectEntity).getId();
    }

    public Long update(ProjectEntity oProjectEntity) {
        validate(oProjectEntity.getId());
        validate(oProjectEntity);
        oAuthService.OnlyAdmins();
        return oProjectRepository.save(oProjectEntity).getId();
    }

    public Long delete(Long id) {
        validate(id);
        oAuthService.OnlyAdmins();
        oProjectRepository.deleteById(id);
        return id;
    }

    public ProjectEntity getOneRandom() {
        if (count() > 0) {
            ProjectEntity oProjectEntity = null;
            int iPosicion = RandomHelper.getRandomInt(0, (int) oProjectRepository.count() - 1);
            Pageable oPageable = PageRequest.of(iPosicion, 1);
            Page<ProjectEntity> ProjectPage = oProjectRepository.findAll(oPageable);
            List<ProjectEntity> ProjectList = ProjectPage.getContent();
            oProjectEntity = oProjectRepository.getById(ProjectList.get(0).getId());
            return oProjectEntity;
        } else {
            throw new CannotPerformOperationException("No hay projectos en la base de datos");
        }
    }

    public ProjectEntity generate() {
        oAuthService.OnlyAdmins();
        ProjectEntity oProjectEntity = generateRandomProject();
        oProjectRepository.save(oProjectEntity);
        return oProjectEntity;
    }

    public Long generateSome(Integer amount) {
        oAuthService.OnlyAdmins();
        List<ProjectEntity> userList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            ProjectEntity oProjectEntity = generateRandomProject();
            oProjectRepository.save(oProjectEntity);
            userList.add(oProjectEntity);
        }
        return oProjectRepository.count();
    }

    private ProjectEntity generateRandomProject() {
        ProjectEntity oProjectEntity = new ProjectEntity();
        oProjectEntity.setCode(generateCode());
        oProjectEntity.setDescription(generateDescription());
        oProjectEntity.setUrl(generateUrl());
        return oProjectEntity;
    }

    private String generateDescription() {
        return DESCRIPTION[RandomHelper.getRandomInt(0, DESCRIPTION.length - 1)].toLowerCase();
    }

    private String generateCode() {
        return CODE[RandomHelper.getRandomInt(0, CODE.length - 1)].toLowerCase() + "-" + RandomHelper.getRandomInt(1111, 9999);
    }

    private String generateUrl() {
        return "http://www." + generateDescription() + "/andamios.net";
    }

}
