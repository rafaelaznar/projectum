package net.ausiasmarch.andamio.api;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.ausiasmarch.andamio.entity.ResolutionEntity;
import net.ausiasmarch.andamio.service.ResolutionService;

@RestController
@RequestMapping("/resolution")
public class ResolutionController {

    @Autowired
    ResolutionService oResolutionService;

    @GetMapping("/{id}")
    public ResponseEntity<ResolutionEntity> get(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<ResolutionEntity>(oResolutionService.get(id), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<Long>(oResolutionService.count(), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<Page<ResolutionEntity>> getPage(
        @ParameterObject @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable oPageable,
        @RequestParam(value = "observations", required = false) String observations,
        @RequestParam(value = "issue", required = false) Long id_issue,
        @RequestParam(value = "developer", required = false) Long id_developer) {
        return new ResponseEntity<>(oResolutionService.getPage(observations, id_issue, id_developer, oPageable), HttpStatus.OK);
    }

    @PostMapping("/generate")
    public ResponseEntity<ResolutionEntity> generate() {
        return new ResponseEntity<ResolutionEntity>(oResolutionService.generate(), HttpStatus.OK);
    }

    @PostMapping("/generate/{amount}")
    public ResponseEntity<Long> generateSome(@PathVariable(value = "amount") int amount) {
        return new ResponseEntity<Long>(oResolutionService.generateSome(amount), HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<Long> update(@RequestBody ResolutionEntity oResolutionEntity) {
        return new ResponseEntity<Long>(oResolutionService.update(oResolutionEntity.getId(), oResolutionEntity), HttpStatus.OK);
    }
    
    @PostMapping("/")
    public ResponseEntity<Long> create(@RequestBody ResolutionEntity oNewResolutionEntity){
        return new ResponseEntity<Long>(oResolutionService.create(oNewResolutionEntity), HttpStatus.OK);
    }

}
