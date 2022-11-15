package net.ausiasmarch.andamio.service;

import net.ausiasmarch.andamio.entity.IssueEntity;
import net.ausiasmarch.andamio.exception.CannotPerformOperationException;
import net.ausiasmarch.andamio.exception.ResourceNotFoundException;
import net.ausiasmarch.andamio.helper.RandomHelper;
import net.ausiasmarch.andamio.helper.ValidationHelper;
import net.ausiasmarch.andamio.repository.IssueRepository;

import java.math.BigInteger;
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

    private final IssueRepository oIssueRepository;
    private final AuthService oAuthService;
            
    private final String[] OBSERVATION = {"Ejemplo Observación 1", "Ejemplo Observación 2", "Ejemplo Observación 3", "Ejemplo Observación 4", "Ejemplo Observación 5", "Ejemplo Observación 6", "Ejemplo Observación 7",
    "Ejemplo Observación 8", "Ejemplo Observación 9", "Ejemplo Observación 10"};

    @Autowired
    public IssueService(IssueRepository oIssueRepository, AuthService oAuthService) {
        this.oIssueRepository = oIssueRepository;
        this.oAuthService = oAuthService;
    }

    public void validate(Long id) {
        if (!oIssueRepository.existsById(id)) {
            throw new ResourceNotFoundException("id " + id + " not exist");
        }
    }

    public IssueEntity get(Long id) {
        //oAuthService.OnlyAdmins();
        return oIssueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue with id: " + id + " not found"));
    }

    public Long update(IssueEntity oIssueEntity) {
        validate(oIssueEntity.getId());
        //oAuthService.OnlyAdmins();
        return oIssueRepository.save(oIssueEntity).getId();
    }

    public Page<IssueEntity> getPage(Long id_developer, Long id_task, int page, int size) {
        //oAuthService.OnlyAdmins();
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
        

    public Long delete(Long id) {
        //oAuthService.OnlyAdmins();
        validate(id);
        oIssueRepository.deleteById(id);
        return id;
    }

    public void validate(IssueEntity oIssueEntity){
        ValidationHelper.validateDate(oIssueEntity.getOpen_datetime(), LocalDateTime.of(1990, 01, 01, 00, 00, 00), LocalDateTime.of(2025, 01, 01, 00, 00, 00), "campo fecha de issue");
        ValidationHelper.validateStringLength(oIssueEntity.getObservations(), 10, 255, "campo observations de issue (debe tener una longitud entre 10 y 255)");
        ValidationHelper.validateRange(oIssueEntity.getValue(), 0, 5, "campo value de issue (debe ser un entero entre 0 y 5)");
        oDeveloperService.validate(oIssueEntity.getDeveloper().getId());
        oTaskService.validate(oIssueEntity.getTask().getId());
    }


    public Long create(IssueEntity oNewIssueEntity) {
        //oAuthService.OnlyAdmins();
        validate(oNewIssueEntity);
        oNewIssueEntity.setId(0L);
        return oIssueRepository.save(oNewIssueEntity).getId();
    }
    
    public Long count(){
        //oAuthService.OnlyAdmins();
        return oIssueRepository.count();

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

    private IssueEntity generateIssue() {
        //oAuthService.OnlyAdmins();
        return oIssueRepository.save(generateRandomIssue());
    }
    
    private String generateObservation() {
        return OBSERVATION[RandomHelper.getRandomInt(0, OBSERVATION.length-1)];
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


    public Long generateSome(Integer amount) {
        //oAuthService.OnlyAdmins();
        List<IssueEntity> userList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            IssueEntity oIssueEntity = generateIssue();
            oIssueRepository.save(oIssueEntity);
            userList.add(oIssueEntity);
        }
        return oIssueRepository.count();
    }

}
