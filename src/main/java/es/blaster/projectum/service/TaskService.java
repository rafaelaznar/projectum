package es.blaster.projectum.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import es.blaster.projectum.entity.TaskEntity;
import es.blaster.projectum.exception.CannotPerformOperationException;
import es.blaster.projectum.exception.ResourceNotFoundException;
import es.blaster.projectum.helper.RandomHelper;
import es.blaster.projectum.helper.ValidationHelper;
import es.blaster.projectum.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class TaskService {

    private final String[] DESCRIPTION = {"SQL db test", "Inno db is cool", "administrador SQL test",
        "MongoDB", "Hola Mundo!", "Adios Mundo!", "Say Hello!", "My cat bigotillos", "The mexican", "Another one"};

    @Autowired
    AuthService oAuthService;

    @Autowired
    TaskRepository oTaskRepository;

    @Autowired
    ProjectService oProjectService;

    private void validate(TaskEntity oTaskEntity) {
        ValidationHelper.validateStringLength(oTaskEntity.getDescription(), 10, 255, "the field description of Task must be (de 2 a 50 characteres)");
        ValidationHelper.validateRange(oTaskEntity.getPriority(), 0, 10, "the field percentage must be(de 0 a 10) range");
        ValidationHelper.validateRange(oTaskEntity.getComplexity(), 0, 10, "the field percentage must be(de 0 a 10) range");
        oProjectService.validate(oTaskEntity.getProject().getId());

    }

    public void validate(Long id) {
        if (!oTaskRepository.existsById(id)) {
            throw new ResourceNotFoundException("id " + id + " not exist");
        }
    }

    public TaskEntity get(Long id) {
        oAuthService.OnlyAdminsOrViewers();
        return oTaskRepository.getById(id);
    }

    public Page<TaskEntity> getPage(Pageable oPageable, String strFilter, Long lProject) {
        oAuthService.OnlyAdminsOrViewers();
        Page<TaskEntity> oPage = null;
        if (lProject != null) {
            if (strFilter == null || strFilter.isEmpty() || strFilter.trim().isEmpty()) {
                return oTaskRepository.findByProjectId(lProject, oPageable);
            } else {
                return oTaskRepository.findByProjectIdAndDescriptionContainingIgnoreCase(lProject, strFilter, oPageable);
            }
        } else {
            if (strFilter == null || strFilter.isEmpty() || strFilter.trim().isEmpty()) {
                return oTaskRepository.findAll(oPageable);
            } else {
                return oTaskRepository.findByDescriptionContainingIgnoreCase(strFilter, oPageable);
            }
        }
    }

    public Long count() {
        oAuthService.OnlyAdminsOrViewers();
        return oTaskRepository.count();
    }

    public Long create(TaskEntity oTaskEntity) {
        oAuthService.OnlyAdmins();
        validate(oTaskEntity);
        return oTaskRepository.save(oTaskEntity).getId();
    }

    public Long update(TaskEntity oTaskEntity) {
        validate(oTaskEntity.getId());
        oAuthService.OnlyAdmins();
        validate(oTaskEntity);
        oTaskRepository.save(oTaskEntity);
        return oTaskEntity.getId();
    }

    public Long delete(Long id) {
        validate(id);
        oAuthService.OnlyAdmins();
        oTaskRepository.deleteById(id);
        return id;
    }

    public TaskEntity getOneRandom() {
        if (count() > 0) {
            TaskEntity oTaskEntity = null;
            int iPosicion = RandomHelper.getRandomInt(0, (int) oTaskRepository.count() - 1);
            Pageable oPageable = PageRequest.of(iPosicion, 1);
            Page<TaskEntity> taskPage = oTaskRepository.findAll(oPageable);
            List<TaskEntity> TaskList = taskPage.getContent();
            oTaskEntity = oTaskRepository.getById(TaskList.get(0).getId());
            return oTaskEntity;
        } else {
            throw new CannotPerformOperationException("no hay tareas en la base de datos");
        }
    }

    public TaskEntity generate() {
        oAuthService.OnlyAdmins();
        TaskEntity oTaskEntity = generateRandomTask();
        oTaskRepository.save(oTaskEntity);
        return oTaskEntity;
    }

    public Long generateSome(Integer amount) {
        oAuthService.OnlyAdmins();
        List<TaskEntity> userList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            TaskEntity oTaskEntity = generateRandomTask();
            oTaskRepository.save(oTaskEntity);
            userList.add(oTaskEntity);
        }
        return oTaskRepository.count();
    }

    private TaskEntity generateRandomTask() {
        TaskEntity oTaskEntity = new TaskEntity();
        oTaskEntity.setDescription(generateDescription());
        oTaskEntity.setComplexity(RandomHelper.getRandomInt(1, 10));
        oTaskEntity.setPriority(RandomHelper.getRandomInt(1, 10));
        oTaskEntity.setProject(oProjectService.getOneRandom());
        return oTaskEntity;
    }

    private String generateDescription() {
        return DESCRIPTION[RandomHelper.getRandomInt(0, DESCRIPTION.length - 1)].toLowerCase() + 
               " " + DESCRIPTION[RandomHelper.getRandomInt(0, DESCRIPTION.length - 1)].toLowerCase() +
               " " + DESCRIPTION[RandomHelper.getRandomInt(0, DESCRIPTION.length - 1)].toLowerCase();
    }

}
