package es.blaster.projectum.service;

import es.blaster.projectum.entity.IssueEntity;
import es.blaster.projectum.exception.CannotPerformOperationException;
import es.blaster.projectum.exception.ResourceNotFoundException;
import es.blaster.projectum.helper.RandomHelper;
import es.blaster.projectum.helper.ValidationHelper;
import es.blaster.projectum.repository.IssueRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IssueService {

    @Autowired
    DeveloperService oDeveloperService;

    @Autowired
    TaskService oTaskService;

    @Autowired
    IssueRepository oIssueRepository;

    @Autowired
    AuthService oAuthService;

    private final String[] OBSERVATION = {"Ejemplo Observación 1", "Ejemplo Observación 2", "Ejemplo Observación 3", "Ejemplo Observación 4", "Ejemplo Observación 5", "Ejemplo Observación 6", "Ejemplo Observación 7",
        "Ejemplo Observación 8", "Ejemplo Observación 9", "Ejemplo Observación 10"};

    public void validate(Long id) {
        if (!oIssueRepository.existsById(id)) {
            throw new ResourceNotFoundException("id " + id + " not exist");
        }
    }

    public void validate(IssueEntity oIssueEntity) {
        ValidationHelper.validateDate(oIssueEntity.getOpen_datetime(), LocalDateTime.of(1990, 01, 01, 00, 00, 00), LocalDateTime.of(2025, 01, 01, 00, 00, 00), "campo fecha de issue");
        ValidationHelper.validateStringLength(oIssueEntity.getObservations(), 10, 255, "campo observations de issue (debe tener una longitud entre 10 y 255)");
        ValidationHelper.validateRange(oIssueEntity.getValue(), 0, 5, "campo value de issue (debe ser un entero entre 0 y 5)");
        oDeveloperService.validate(oIssueEntity.getDeveloper().getId());
        oTaskService.validate(oIssueEntity.getTask().getId());
    }

    public IssueEntity get(Long id) {
        oAuthService.OnlyAdminsOrViewers();
        return oIssueRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Issue with id: " + id + " not found"));
    }

    public Page<IssueEntity> getPage(Long id_developer, Long id_task, int page, int size) {
        oAuthService.OnlyAdminsOrViewers();
        Pageable oPageable = PageRequest.of(page, size);
        if (id_developer == null && id_task == null) {
            return oIssueRepository.findAll(oPageable);
        } else if (id_developer == null) {
            return oIssueRepository.findByTaskId(id_task, oPageable);
        } else if (id_task == null) {
            return oIssueRepository.findByDeveloperId(id_developer, oPageable);
        } else {
            return oIssueRepository.findByDeveloperIdAndTaskId(id_developer, id_task, oPageable);
        }
    }

    public Long count() {
        oAuthService.OnlyAdminsOrViewers();
        return oIssueRepository.count();

    }

    public Long create(IssueEntity oNewIssueEntity) {
        oAuthService.OnlyAdmins();
        validate(oNewIssueEntity);
        oNewIssueEntity.setId(0L);
        return oIssueRepository.save(oNewIssueEntity).getId();
    }

    public Long update(IssueEntity oIssueEntity) {
        validate(oIssueEntity.getId());
        oAuthService.OnlyAdmins();
        return oIssueRepository.save(oIssueEntity).getId();
    }

    public Long delete(Long id) {
        oAuthService.OnlyAdmins();
        validate(id);
        oIssueRepository.deleteById(id);
        return id;
    }

    public IssueEntity getOneRandom() {
        if (count() > 0) {
            IssueEntity oIssueEntity = null;
            int iPosicion = RandomHelper.getRandomInt(0, (int) oIssueRepository.count() - 1);
            Pageable oPageable = PageRequest.of(iPosicion, 1);
            Page<IssueEntity> issuePage = oIssueRepository.findAll(oPageable);
            List<IssueEntity> issueList = issuePage.getContent();
            oIssueEntity = oIssueRepository.getById(issueList.get(0).getId());
            return oIssueEntity;
        } else {
            throw new CannotPerformOperationException("ho hay usuarios en la base de datos");
        }
    }

    public Long generateSome(Integer amount) {
        oAuthService.OnlyAdmins();
        List<IssueEntity> userList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            IssueEntity oIssueEntity = generateIssue();
            oIssueRepository.save(oIssueEntity);
            userList.add(oIssueEntity);
        }
        return oIssueRepository.count();
    }

    private IssueEntity generateIssue() {
        return oIssueRepository.save(generateRandomIssue());
    }

    private String generateObservation() {
        return OBSERVATION[RandomHelper.getRandomInt(0, OBSERVATION.length - 1)];
    }

    private IssueEntity generateRandomIssue() {
        IssueEntity oIssueEntity = new IssueEntity();
        oIssueEntity.setDeveloper(oDeveloperService.getOneRandom());
        oIssueEntity.setObservations(generateObservation());
        oIssueEntity.setOpen_datetime(RandomHelper.getRadomDateTime());
        oIssueEntity.setTask(oTaskService.getOneRandom());
        oIssueEntity.setValue(RandomHelper.getRandomInt(0, 10));
        return oIssueEntity;
    }

}
