package net.ausiasmarch.andamio.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.ausiasmarch.andamio.entity.HelpEntity;
import net.ausiasmarch.andamio.service.HelpService;

@RestController
@RequestMapping("/help")
public class HelpController {

    @Autowired
    HelpService oHelpService;

    @GetMapping("/{id}")
    public ResponseEntity<HelpEntity> get(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<HelpEntity>(oHelpService.get(id), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Long> create(@RequestBody HelpEntity oNewHelpEntity) {
        return new ResponseEntity<Long>(oHelpService.create(oNewHelpEntity), HttpStatus.OK);
    }
        
    @PutMapping("")
    public ResponseEntity<Long> update(@RequestBody HelpEntity oHelpEntity) {
        return new ResponseEntity<Long>(oHelpService.update(oHelpEntity), HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public Long delete(@PathVariable(value = "id") Long id) {
        return oHelpService.delete(id);
    }

    @PostMapping("/generate")
    public ResponseEntity<HelpEntity> generate() {
        return new ResponseEntity<>(oHelpService.generate(), HttpStatus.OK);
    }

    @PostMapping("/generate/{amount}")
    public ResponseEntity<Long> generateSome(@PathVariable(value = "amount") Integer amount) {
        return new ResponseEntity<>(oHelpService.generateSome(amount), HttpStatus.OK);
    }
}
