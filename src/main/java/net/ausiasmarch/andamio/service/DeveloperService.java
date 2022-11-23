package net.ausiasmarch.andamio.service;

import java.util.ArrayList;
import java.util.List;

import net.ausiasmarch.andamio.repository.TeamRepository;
import net.ausiasmarch.andamio.repository.UsertypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import net.ausiasmarch.andamio.entity.DeveloperEntity;
import net.ausiasmarch.andamio.exception.CannotPerformOperationException;
import net.ausiasmarch.andamio.exception.ResourceNotFoundException;
import net.ausiasmarch.andamio.exception.ResourceNotModifiedException;
import net.ausiasmarch.andamio.exception.ValidationException;
import net.ausiasmarch.andamio.helper.RandomHelper;
import net.ausiasmarch.andamio.helper.ValidationHelper;
import net.ausiasmarch.andamio.repository.DeveloperRepository;

@Service
public class DeveloperService {

    @Autowired
    UsertypeService oUsertypeService;

    @Autowired
    TeamService oTeamService;

    private final DeveloperRepository oDeveloperRepository;
    private final TeamRepository oTeamRepository;
    private final UsertypeRepository oUsertypeRepository;
    private final AuthService oAuthService;

    private final List<String> names = List.of("Ainhoa", "Kevin", "Estefania", "Cristina",
            "Jose Maria", "Lucas Ezequiel", "Carlos", "Elliot", "Alexis", "Ruben", "Luis Fernando", "Karim", "Luis",
            "Jose David", "Nerea", "Ximo", "Iris", "Alvaro", "Mario", "Raimon");

    private final List<String> surnames = List.of("Benito", "Blanco", "Boriko", "Carrascosa", "Guerrero", "Gyori",
            "Lazaro", "Luque", "Perez", "Perez", "Perez", "Rezgaoui", "Rodríguez", "Rosales", "Soler", "Soler", "Suay", "Talaya", "Tomas", "Vilar");

    private final List<String> last_names = List.of("Sanchis", "Bañuls", "Laenos", "Torres", "Sanchez", "Gyori",
            "Luz", "Pascual", "Blayimir", "Castello", "Hurtado", "Mourad", "Fernández", "Ríos", "Benavent", "Benavent", "Patricio", "Romance", "Zanon", "Morera");

    private final String ANDAMIO_DEFAULT_PASSWORD = "73ec8dee81ea4c9e7515d63c9e5bbb707c7bc4789363c5afa401d3aa780630f6";

    @Autowired
    public DeveloperService(DeveloperRepository oDeveloperRepository, TeamRepository oTeamRepository, UsertypeRepository oUsertypeRepository, AuthService oAuthService) {
        this.oDeveloperRepository = oDeveloperRepository;
        this.oTeamRepository = oTeamRepository;
        this.oUsertypeRepository = oUsertypeRepository;
        this.oAuthService = oAuthService;
    }

    public void validate(Long id) {
        if (!oDeveloperRepository.existsById(id)) {
            throw new ResourceNotFoundException("id " + id + " not exist");
        }
    }

    public void validate(DeveloperEntity oDeveloperEntity) {
        ValidationHelper.validateStringLength(oDeveloperEntity.getName(), 2, 50, "campo name de Developer(el campo debe tener longitud de 2 a 50 caracteres)");
        ValidationHelper.validateStringLength(oDeveloperEntity.getSurname(), 2, 50, "campo surname de Developer(el campo debe tener longitud de 2 a 50 caracteres)");
        ValidationHelper.validateStringLength(oDeveloperEntity.getLastname(), 2, 50, "campo lastname de Developer(el campo debe tener longitud de 2 a 50 caracteres)");
        ValidationHelper.validateEmail(oDeveloperEntity.getEmail(), "campo email de Developer");
        ValidationHelper.validateLogin(oDeveloperEntity.getUsername(), "campo username de Developer");
        if (oDeveloperRepository.existsByUsername(oDeveloperEntity.getUsername())) {
            throw new ValidationException("el campo username está repetido");
        }
        oUsertypeService.validate(oDeveloperEntity.getUsertype().getId());
        oTeamService.validate(oDeveloperEntity.getTeam().getId());
    }

    public DeveloperEntity get(Long id) {
        //oAuthService.OnlyAdmins();
        return oDeveloperRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer with id: " + id + " not found"));
    }

    public Page<DeveloperEntity> getPage(Pageable oPageable, String strFilter, Long id_team, Long id_usertype) {
        //oAuthService.OnlyAdmins();
        ValidationHelper.validateRPP(oPageable.getPageSize());
        if (strFilter == null || strFilter.length()==0) {
            if (id_team == null) {
                if (id_usertype == null) {
                    return oDeveloperRepository.findAll(oPageable);
                } else {
                    return oDeveloperRepository.findByUsertypeId(id_usertype, oPageable);
                }
            } else {
                if (id_usertype == null) {
                    return oDeveloperRepository.findByTeamId(id_team, oPageable);
                } else {
                    return oDeveloperRepository.findByTeamIdAndUsertypeId(id_team, id_usertype, oPageable);
                }
            }
        } else {
            if (id_team == null) {
                if (id_usertype == null) {
                    return oDeveloperRepository.findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLastnameIgnoreCaseContaining(strFilter, strFilter, strFilter, oPageable);
                } else {
                    return oDeveloperRepository.findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLastnameIgnoreCaseContainingAndUsertypeId(strFilter, strFilter, strFilter, id_usertype, oPageable);
                }
            } else {
                if (id_usertype == null) {
                    return oDeveloperRepository.findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLastnameIgnoreCaseContainingAndTeamId(strFilter, strFilter, strFilter, id_team, oPageable);
                } else {
                    return oDeveloperRepository.findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLastnameIgnoreCaseContainingAndTeamIdAndUsertypeId(strFilter, strFilter, strFilter, id_team, id_usertype, oPageable);
                }
            }
        }

    }

    public Long count() {
        //oAuthService.OnlyAdmins();
        return oDeveloperRepository.count();
    }

    public Long update(DeveloperEntity oDeveloperEntity) {
        validate(oDeveloperEntity.getId());
        //oAuthService.OnlyAdmins();
        DeveloperEntity oOldDeveloperEntity=oDeveloperRepository.getById(oDeveloperEntity.getId());
        oDeveloperEntity.setPassword(oOldDeveloperEntity.getPassword());
        return oDeveloperRepository.save(oDeveloperEntity).getId();
    }

    public Long create(DeveloperEntity oNewDeveloperEntity) {
        //oAuthService.OnlyAdmins();
        validate(oNewDeveloperEntity);
        oNewDeveloperEntity.setId(0L);
        oNewDeveloperEntity.setPassword(ANDAMIO_DEFAULT_PASSWORD);
        return oDeveloperRepository.save(oNewDeveloperEntity).getId();
    }

    public Long delete(Long id) {
        //oAuthService.OnlyAdmins();
        validate(id);
        oDeveloperRepository.deleteById(id);
        if (oDeveloperRepository.existsById(id)) {
            throw new ResourceNotModifiedException("can't remove register " + id);
        } else {
            return id;
        }
    }

    //necesario para coger el id para el generate del team 
    public DeveloperEntity getOneRandom() {
        if (count() > 0) {
            DeveloperEntity oDeveloperEntity = null;
            int iPosicion = RandomHelper.getRandomInt(0, (int) oDeveloperRepository.count() - 1);
            Pageable oPageable = PageRequest.of(iPosicion, 1);
            Page<DeveloperEntity> developerPage = oDeveloperRepository.findAll(oPageable);
            List<DeveloperEntity> developerList = developerPage.getContent();
            oDeveloperEntity = oDeveloperRepository.getById(developerList.get(0).getId());
            return oDeveloperEntity;
        } else {
            throw new CannotPerformOperationException("ho hay usuarios en la base de datos");
        }
    }

    private DeveloperEntity generateDeveloper() {
        DeveloperEntity oDeveloperEntity = new DeveloperEntity();

        oDeveloperEntity.setName(names.get(RandomHelper.getRandomInt(0, names.size() - 1)));
        oDeveloperEntity.setSurname(surnames.get(RandomHelper.getRandomInt(0, names.size() - 1)));
        oDeveloperEntity.setLastname(last_names.get(RandomHelper.getRandomInt(0, names.size() - 1)));

        oDeveloperEntity.setUsername((oDeveloperEntity.getName().toLowerCase()
                + oDeveloperEntity.getSurname().toLowerCase()).replaceAll("\\s", ""));
        oDeveloperEntity.setEmail(oDeveloperEntity.getUsername() + "@andamio.net");

        oDeveloperEntity.setPassword(ANDAMIO_DEFAULT_PASSWORD);

        int totalUsertypes = (int) oUsertypeRepository.count();
        int randomUserTypeId = RandomHelper.getRandomInt(1, totalUsertypes);
        oUsertypeRepository.findById((long) randomUserTypeId)
                .ifPresent(oDeveloperEntity::setUsertype);

        int totalTeams = (int) oTeamRepository.count();
        int randomTeamId = RandomHelper.getRandomInt(1, totalTeams);
        oTeamRepository.findById((long) randomTeamId)
                .ifPresent(oDeveloperEntity::setTeam);

        return oDeveloperEntity;
    }

    public DeveloperEntity generateOne() {
        //oAuthService.OnlyAdmins();
        return oDeveloperRepository.save(generateDeveloper());
    }

    public Long generateSome(Long amount) {
        //oAuthService.OnlyAdmins();
        List<DeveloperEntity> developerToSave = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            developerToSave.add(generateDeveloper());
        }
        oDeveloperRepository.saveAll(developerToSave);
        return oDeveloperRepository.count();
    }
}
