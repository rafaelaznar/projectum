package net.ausiasmarch.andamio.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.ausiasmarch.andamio.entity.DeveloperEntity;
import net.ausiasmarch.andamio.service.DeveloperService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/developer")
public class DeveloperController {

    private final DeveloperService oDeveloperService;

    @Autowired
    public DeveloperController(DeveloperService oDeveloperService) {
        this.oDeveloperService = oDeveloperService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeveloperEntity> get(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(oDeveloperService.get(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<DeveloperEntity>> getPage(
            @ParameterObject @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable oPageable,
            @RequestParam(name = "filter", required = false) String strFilter,
            @RequestParam(value = "team", required = false) Long id_team,
            @RequestParam(value = "usertype", required = false) Long id_usertype) {
        return new ResponseEntity<>(oDeveloperService.getPage(oPageable, strFilter, id_team, id_usertype), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<Long>(oDeveloperService.count(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Long> update(@RequestBody DeveloperEntity oDeveloperEntity) {
        return new ResponseEntity<Long>(oDeveloperService.update(oDeveloperEntity), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody DeveloperEntity oNewDeveloperEntity) {
        return new ResponseEntity<Long>(oDeveloperService.create(oNewDeveloperEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<Long>(oDeveloperService.delete(id), HttpStatus.OK);
    }

    @PostMapping("/generate")
    public ResponseEntity<DeveloperEntity> generateOne() {
        return new ResponseEntity<>(oDeveloperService.generateOne(), HttpStatus.OK);
    }

    @PostMapping("/generate/{amount}")
    public ResponseEntity<Long> generateSome(@PathVariable Long amount) {
        return new ResponseEntity<>(oDeveloperService.generateSome(amount), HttpStatus.OK);
    }
}
