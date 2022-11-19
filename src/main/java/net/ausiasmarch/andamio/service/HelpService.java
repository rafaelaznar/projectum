package net.ausiasmarch.andamio.service;

import net.ausiasmarch.andamio.exception.ResourceNotFoundException;
import net.ausiasmarch.andamio.helper.RandomHelper;
import net.ausiasmarch.andamio.helper.ValidationHelper;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ausiasmarch.andamio.entity.HelpEntity;
import net.ausiasmarch.andamio.repository.HelpRepository;

@Service
public class HelpService {

    @Autowired
    public HelpService(HelpRepository oHelpRepository, AuthService oAuthService, DeveloperService oDeveloperService, ResolutionService oResolutionService) {
        this.oHelpRepository = oHelpRepository;
        this.oAuthService = oAuthService;
        this.oDeveloperService = oDeveloperService;
        this.oResolutionService = oResolutionService;
    }
    private final HelpRepository oHelpRepository;
    private final AuthService oAuthService;
    private final DeveloperService oDeveloperService;
    private final ResolutionService oResolutionService;
              
    private void validate(Long id) {
        if (!oHelpRepository.existsById(id)) {
            throw new ResourceNotFoundException("id " + id + " not exist");
        }
    }

    private void validate(HelpEntity oHelpEntity) {
        ValidationHelper.validateRange(oHelpEntity.getPercentage(), 0, 100, "field percentage must be in the 0-100 range");
        oResolutionService.validate(oHelpEntity.getResolution().getId());
        oDeveloperService.validate(oHelpEntity.getDeveloper().getId());
    }

    public HelpEntity get(Long id) {
        //oAuthService.OnlyAdmins();
        return oHelpRepository.getById(id);
    }  
    
    public Page<HelpEntity> getPage(Long id_developer, Long id_resolution, int page, int size) {
        //oAuthService.OnlyAdmins();
        Pageable oPageable = PageRequest.of(page, size);
        if (id_developer == null && id_resolution == null) {
            return oHelpRepository.findAll(oPageable);
        } else if (id_developer == null) {
            return oHelpRepository.findByResolutionId(id_resolution, oPageable);
        } else if (id_resolution == null) {
            return oHelpRepository.findByDeveloperId(id_developer, oPageable);
        } else {
            return oHelpRepository.findByDeveloperIdAndResolutionId(id_developer, id_resolution, oPageable);
        }
    }
    
    @Transactional
    public Long create(HelpEntity oNewHelpEntity) {
        //oAuthService.OnlyAdmins();
        validate(oNewHelpEntity);
        oNewHelpEntity.setResolution(oResolutionService.get(oNewHelpEntity.getResolution().getId()));
        oNewHelpEntity.setDeveloper(oDeveloperService.get(oNewHelpEntity.getDeveloper().getId()));
        oNewHelpEntity.setId(null);
        return oHelpRepository.save(oNewHelpEntity).getId();
    }

    public Long delete(Long id) {
        validate(id);
        //oAuthService.OnlyAdmins();
        oHelpRepository.deleteById(id);
        return id;
    }

    public HelpEntity generate() {
        //oAuthService.OnlyAdmins();
        return oHelpRepository.save(generateRandomUser());
    }

    public Long generateSome(Integer amount) {
        //oAuthService.OnlyAdmins();
        List<HelpEntity> userList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            HelpEntity oUsuarioEntity = generateRandomUser();
            oHelpRepository.save(oUsuarioEntity);
            userList.add(oUsuarioEntity);
        }
        return oHelpRepository.count();
    }

    private HelpEntity generateRandomUser() {
        HelpEntity oHelpEntity = new HelpEntity();
        oHelpEntity.setResolution(oResolutionService.getOneRandom());
        oHelpEntity.setDeveloper(oDeveloperService.getOneRandom());
        oHelpEntity.setPercentage(RandomHelper.getRadomDouble(0, 100));
        return oHelpEntity;
    }
    
    public Long update(HelpEntity oHelpEntity) {
        validate(oHelpEntity.getId());
        //oAuthService.OnlyAdmins();
        return oHelpRepository.save(oHelpEntity).getId();
    }
}
