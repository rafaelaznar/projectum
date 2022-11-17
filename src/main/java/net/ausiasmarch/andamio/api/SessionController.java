package net.ausiasmarch.andamio.api;

import net.ausiasmarch.andamio.bean.DeveloperBean;
import net.ausiasmarch.andamio.entity.DeveloperEntity;
import net.ausiasmarch.andamio.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    AuthService oAuthService;

    @GetMapping("")
    public ResponseEntity<DeveloperEntity> check() {
        return new ResponseEntity<DeveloperEntity>(oAuthService.check(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> login(@org.springframework.web.bind.annotation.RequestBody DeveloperBean oDeveloperBean) {
        return new ResponseEntity<String>("\"" + oAuthService.login(oDeveloperBean) + "\"", HttpStatus.OK);
    }

}
