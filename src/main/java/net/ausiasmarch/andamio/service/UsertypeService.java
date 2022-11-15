package net.ausiasmarch.andamio.service;

import net.ausiasmarch.andamio.entity.UsertypeEntity;
import net.ausiasmarch.andamio.exception.ResourceNotFoundException;
import net.ausiasmarch.andamio.repository.UsertypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsertypeService {

    private final UsertypeRepository oUsertypeRepository;
    private final AuthService oAuthService;

    @Autowired
    public UsertypeService(UsertypeRepository oUsertypeRepository, AuthService oAuthService) {
        this.oUsertypeRepository = oUsertypeRepository;
        this.oAuthService = oAuthService;
    }

    public UsertypeEntity get(Long id) {
        //oAuthService.OnlyAdmins();
        return oUsertypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserType with id: " + id + " not found"));
    }

    public Long count() {
        //oAuthService.OnlyAdmins();
        return oUsertypeRepository.count();
    }

    public Page<UsertypeEntity> getPage(int page, int size) {
        //oAuthService.OnlyAdmins();
        Pageable oPageable = PageRequest.of(page, size);
        
            return oUsertypeRepository.findAll(oPageable);
    }

    public Long update(UsertypeEntity oUsertypeEntity) {
        //oAuthService.OnlyAdmins();
        validate(oUsertypeEntity.getId());
        oUsertypeRepository.save(oUsertypeEntity);
        return oUsertypeEntity.getId();
    }
    
    public void validate(Long id) {
        if (!oUsertypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("id " + id + " not exist");
        }
    }
}
