package net.ausiasmarch.andamio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ausiasmarch.andamio.entity.TaskEntity;
import net.ausiasmarch.andamio.exception.CannotPerformOperationException;
import net.ausiasmarch.andamio.exception.ResourceNotFoundException;
import net.ausiasmarch.andamio.exception.UnauthorizedException;
import net.ausiasmarch.andamio.helper.RandomHelper;
import net.ausiasmarch.andamio.helper.ValidationHelper;
import net.ausiasmarch.andamio.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class TaskService {

    private final String[] DESCRIPTION = { "SQL db test", "Inno db is cool", "administrador SQL test",
            "MongoDB", "Hola Mundo!", "Adios Mundo!", "Say Hello!", "My cat bigotillos", "The mexican", "Another one" };

    @Autowired
    AuthService oAuthService;

    @Autowired
    TaskRepository oTaskRepository;

    @Autowired
    ProjectService oProjectService;

    public TaskEntity get(Long id) {
        //oAuthService.OnlyAdmins();
        return oTaskRepository.getById(id);
    }

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

    public Long delete(Long id) {
        validate(id);
        //oAuthService.OnlyAdmins();
        oTaskRepository.deleteById(id);
        return id;
    }

    public Long count() {
        //oAuthService.OnlyAdmins();
        return oTaskRepository.count();
    }

    public Long update(TaskEntity oTaskEntity) {
        validate(oTaskEntity.getId());
        //oAuthService.OnlyAdmins();
        oTaskRepository.save(oTaskEntity);
        return oTaskEntity.getId();
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
            throw new CannotPerformOperationException("ho hay usuarios en la base de datos");
        }
    }

    public TaskEntity generate() {
        //oAuthService.OnlyAdmins();
        TaskEntity oTaskEntity = generateRandomTask();
        oTaskRepository.save(oTaskEntity);
        return oTaskEntity;
    }

    public Long generateSome(Integer amount) {
        //oAuthService.OnlyAdmins();
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
        return DESCRIPTION[RandomHelper.getRandomInt(0, DESCRIPTION.length - 1)].toLowerCase();
    }

    public Page<TaskEntity> getPage(Pageable oPageable, String strFilter, Long lProject) {
        Page<TaskEntity> oPage = null;
        if (oAuthService.isAdmin()) {
            if (lProject != null) {
                if (strFilter == null || strFilter.isEmpty() || strFilter.trim().isEmpty()) {
                    return oTaskRepository.findByProjectId(lProject, oPageable);
                } else {
                    return oTaskRepository.findByProjectIdAndComplexityContainingIgnoreCase(lProject, strFilter, oPageable);
                }
            } else {
                if (strFilter == null || strFilter.isEmpty() || strFilter.trim().isEmpty()) {
                    return oTaskRepository.findAll(oPageable);
                } else {
                    return oTaskRepository.findByDescriptionContainingIgnoreCase(strFilter, oPageable);
                }
            }
        } else {
            throw new UnauthorizedException("this request is only allowed to admin role");
        }
    }

    public Long create(TaskEntity oNewTaskEntity){
        //oAuthService.OnlyAdmins();
        

        return oTaskRepository.save(oNewTaskEntity).getId();
    }


}