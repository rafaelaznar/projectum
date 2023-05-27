package es.blaster.projectum.api;

import es.blaster.projectum.entity.IssueEntity;
import es.blaster.projectum.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/issue")
public class IssueController {

    private final IssueService oIssueService;

    @Autowired
    public IssueController(IssueService oIssueService) {
        this.oIssueService = oIssueService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueEntity> get(@PathVariable Long id) {
        return new ResponseEntity<>(oIssueService.get(id), HttpStatus.OK);
    }

    @GetMapping("")
	public ResponseEntity<Page<IssueEntity>> getPage(
        	@RequestParam(value = "developer", required = false) Long id_developer,
            @RequestParam(value = "task", required = false) Long id_task,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
            ) {
    	return new ResponseEntity<Page<IssueEntity>>(oIssueService.getPage(id_developer,id_task,page, size), HttpStatus.OK);
	}

    @PutMapping("")
    public ResponseEntity<Long> update(@RequestBody IssueEntity oIssueEntity) {
        return new ResponseEntity<Long>(oIssueService.update(oIssueEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<Long>(oIssueService.delete(id), HttpStatus.OK);
    }
    


    @PostMapping("/")
    public ResponseEntity<Long> create(@RequestBody IssueEntity oNewIssueEntity) {
        return new ResponseEntity<Long>(oIssueService.create(oNewIssueEntity), HttpStatus.OK);
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> count(){
        return new ResponseEntity<Long>(oIssueService.count(), HttpStatus.OK);        
    }

    
    @PostMapping("/generate/{amount}")
    public ResponseEntity<Long> generateSome(@PathVariable(value = "amount") Integer amount) {
        return new ResponseEntity<>(oIssueService.generateSome(amount), HttpStatus.OK);
    }
    
}
