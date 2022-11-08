package net.ausiasmarch.andamio.api;

import net.ausiasmarch.andamio.entity.ProjectEntity;
import net.ausiasmarch.andamio.service.ProjectService;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

@RestController
@RequestMapping("/project")
public class ProjectController {

    
    private final ProjectService oProjectService;
    
    @Autowired
    public ProjectController(ProjectService oProjectService) {
        this.oProjectService = oProjectService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> get(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<ProjectEntity>(oProjectService.get(id), HttpStatus.OK);
    }

    @GetMapping("")
	public ResponseEntity<Page<ProjectEntity>> getPage(
        	@ParameterObject @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable oPageable,
            @RequestParam(name = "filter", required = false) String strFilter,
        	@RequestParam(name = "team", required = false) Long lTeam) {
    	return new ResponseEntity<Page<ProjectEntity>>(oProjectService.getPage(oPageable, strFilter, lTeam), HttpStatus.OK);
	}
    
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<Long>(oProjectService.count(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody ProjectEntity oProjectEntity) {
        return new ResponseEntity<>(oProjectService.create(oProjectEntity), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Long> update(@RequestBody ProjectEntity oProjectEntity) {
        return new ResponseEntity<Long>(oProjectService.update(oProjectEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Long delete(@PathVariable(value = "id") Long id){
        return oProjectService.delete(id);
    }

    @PostMapping("/generate")
    public ResponseEntity<ProjectEntity> generate() {
        return new ResponseEntity<ProjectEntity>(oProjectService.generate(), HttpStatus.OK);
    }
    
    @PostMapping("/generate/{amount}")
    public ResponseEntity<Long> generateSome(@PathVariable(value = "amount") Integer amount) {
        return new ResponseEntity<>(oProjectService.generateSome(amount), HttpStatus.OK);
    }
    
}
