package net.ausiasmarch.andamio.service;

import net.ausiasmarch.andamio.entity.TeamEntity;
import net.ausiasmarch.andamio.exception.CannotPerformOperationException;
import net.ausiasmarch.andamio.exception.ResourceNotFoundException;
import net.ausiasmarch.andamio.exception.UnauthorizedException;
import net.ausiasmarch.andamio.helper.RandomHelper;
import net.ausiasmarch.andamio.repository.DeveloperRepository;
import net.ausiasmarch.andamio.repository.TeamRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository oTeamRepository;
    private final AuthService oAuthService;

    @Autowired
    DeveloperRepository oDeveloperRepository;

    @Autowired
    DeveloperService oDeveloperService;


    

    @Autowired
    public TeamService(TeamRepository oTeamRepository, AuthService oAuthService) {
        this.oTeamRepository = oTeamRepository;
        this.oAuthService = oAuthService;
    }

    public TeamEntity get(Long id) {
        //oAuthService.OnlyAdmins();
        return oTeamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team with id: " + id + " not found"));
    }

    public void validate(Long id) {
        if (!oTeamRepository.existsById(id)) {
            throw new ResourceNotFoundException("id " + id + " not exist");
        }
    }

    public Long delete(Long id) {
        validate(id);
        //oAuthService.OnlyAdmins();
        oTeamRepository.deleteById(id);
        return id;
    }

    public Page<TeamEntity> getPage(Pageable oPageable, String strFilter, Long lDeveloper) {
        Page<TeamEntity> oPage = null;
        if (oAuthService.isAdmin()) {
            if (lDeveloper != null) {
                if (strFilter == null || strFilter.isEmpty() || strFilter.trim().isEmpty()) {
                    return oTeamRepository.findByDeveloperId(lDeveloper, oPageable);
                } else {
                    return oTeamRepository.findByDeveloperIdAndNameContainingIgnoreCase(lDeveloper, strFilter, oPageable);
                }
            } else {
                if (strFilter == null || strFilter.isEmpty() || strFilter.trim().isEmpty()) {
                    return oTeamRepository.findAll(oPageable);
                } else {
                    return oTeamRepository.findByNameContainingIgnoreCase(strFilter, oPageable);
                }
            }
        } else {
            throw new UnauthorizedException("this request is only allowed to admin role");
        }
    }

    public Long create(TeamEntity oNewTeamEntity) {
        //oAuthService.OnlyAdmins();
        oNewTeamEntity.setId(0L);
        return oTeamRepository.save(oNewTeamEntity).getId();
    }

    public Long count() {
        //oAuthService.OnlyAdmins();
        return oTeamRepository.count();
    }

    public TeamEntity generate() {
        if (oDeveloperRepository.count() > 0) {
            String [] name_teams = {"DAW 22/23", "ASIR 22/23", "SMR 22/23", "REDDIT TEAM", "GIT POWER"};
            String name_team = name_teams[(int) (Math.floor(Math.random() * ((name_teams.length - 1) - 0 + 1) + 0))];
            TeamEntity oTeamEntity = new TeamEntity();
            oTeamEntity.setName(name_team);
            oTeamEntity.setDeveloper(oDeveloperService.getOneRandom());
            return oTeamEntity;
        } else {
            return null;
        }
    }
    
    public Long generateSome(int amount) {
        if (oDeveloperService.count() > 0) {
            for (int i = 0; i < amount; i++) {
                TeamEntity oTeamEntity = generate();
                oTeamRepository.save(oTeamEntity);
            }
            return oTeamRepository.count();
        } else {
            throw new CannotPerformOperationException("no hay usuarios en la base de datos");
        }
    }

    public TeamEntity getOneRandom() {
        if (count() > 0) {
            TeamEntity oTeamEntity = null;
            int iPosicion = RandomHelper.getRandomInt(0, (int) oTeamRepository.count() - 1);
            Pageable oPageable = PageRequest.of(iPosicion, 1);
            Page<TeamEntity> TeamPage = oTeamRepository.findAll(oPageable);
            List<TeamEntity> TeamList = TeamPage.getContent();
            oTeamEntity = oTeamRepository.getById(TeamList.get(0).getId());
            return oTeamEntity;
        } else {
            throw new CannotPerformOperationException("No hay projectos en la base de datos");
        }
    }

    public Long update(TeamEntity oTeamEntity) {
        validate(oTeamEntity.getId());
        //oAuthService.OnlyAdmins();
        return oTeamRepository.save(oTeamEntity).getId();
    }

    


}
