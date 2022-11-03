package net.ausiasmarch.andamio.api;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import net.ausiasmarch.andamio.entity.TaskEntity;
import net.ausiasmarch.andamio.service.TaskService;

@RestController
@RequestMapping("/task")
public class TaskController {
    

    @Autowired
    TaskService oTaskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskEntity> get(@PathVariable(value = "id") Long id) {

        return new ResponseEntity<TaskEntity>(oTaskService.get(id), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public Long delete(@PathVariable(value = "id") Long id){
        return oTaskService.delete(id);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<Long>(oTaskService.count(), HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<Long> update(@RequestBody TaskEntity oTaskEntity) {
        return new ResponseEntity<>(oTaskService.update(oTaskEntity), HttpStatus.OK);
    }

    @PostMapping("/generate")
    public ResponseEntity<TaskEntity> generate() {
        return new ResponseEntity<TaskEntity>(oTaskService.generate(), HttpStatus.OK);
    }
    
    @PostMapping("/generate/{amount}")
    public ResponseEntity<Long> generateSome(@PathVariable(value = "amount") Integer amount) {
        return new ResponseEntity<>(oTaskService.generateSome(amount), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Page<TaskEntity>> getPage(
            @ParameterObject @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable oPageable,
            @RequestParam(name = "filter", required = false) String strFilter,
            @RequestParam(name = "project", required = false) Long lProject) {
        return new ResponseEntity<Page<TaskEntity>>(oTaskService.getPage(oPageable, strFilter, lProject), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Long> create(@RequestBody TaskEntity oNewTaskEntity){
        return new ResponseEntity<Long>(oTaskService.create(oNewTaskEntity), HttpStatus.OK);
    }


}
