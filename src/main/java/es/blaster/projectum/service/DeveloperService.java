package es.blaster.projectum.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import es.blaster.projectum.entity.DeveloperEntity;
import es.blaster.projectum.exception.CannotPerformOperationException;
import es.blaster.projectum.exception.ResourceNotFoundException;
import es.blaster.projectum.exception.ResourceNotModifiedException;
import es.blaster.projectum.exception.ValidationException;
import es.blaster.projectum.helper.RandomHelper;
import es.blaster.projectum.helper.ValidationHelper;
import es.blaster.projectum.repository.DeveloperRepository;

@Service
public class DeveloperService {

    @Autowired
    DeveloperRepository oDeveloperRepository;

    @Autowired
    AuthService oAuthService;

    private final List<String> names = List.of("Ainhoa", "Kevin", "Estefania", "Cristina",
           "Jose Maria", "Lucas Ezequiel", "Carlos", "Elliot", "Alexis", "Ruben", "Luis Fernando", "Karim", "Luis",
           "Jose David", "Nerea", "Ximo", "Iris", "Alvaro", "Mario", "Raimon", "Rafael", "María");

    private final List<String> surnames = List.of("Benito", "Blanco", "Boriko", "Carrascosa", "Guerrero", "Gyori",
           "Lazaro", "Luque", "Perez", "Perez", "Perez", "Rezgaoui", "Rodríguez", "Rosales", "Soler", "Soler", "Suay", "Talaya", "Tomas", "Vilar");

    private final List<String> last_names = List.of("Sanchis", "Bañuls", "Laenos", "Torres", "Sanchez", "Gyori",
           "Luz", "Pascual", "Blayimir", "Castello", "Hurtado", "Mourad", "Fernández", "Ríos", "Benavent", "Benavent", "Patricio", "Romance", "Zanon", "Morera");

    private final String ANDAMIO_DEFAULT_PASSWORD = "73ec8dee81ea4c9e7515d63c9e5bbb707c7bc4789363c5afa401d3aa780630f6";

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
    }

    public DeveloperEntity get(Long id) {
        oAuthService.OnlyAdminsOrViewers();
        return oDeveloperRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Developer with id: " + id + " not found"));
    }

    public Page<DeveloperEntity> getPage(Pageable oPageable, String strFilter, Long id_team, Long id_usertype) {
        oAuthService.OnlyAdminsOrViewers();
        ValidationHelper.validateRPP(oPageable.getPageSize());
        if (strFilter == null || strFilter.length() == 0) {
            return oDeveloperRepository.findAll(oPageable);
        } else {
            return oDeveloperRepository.findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrLastnameIgnoreCaseContaining(strFilter, strFilter, strFilter, oPageable);
        }

    }

    public Long count() {
        oAuthService.OnlyAdminsOrViewers();
        return oDeveloperRepository.count();
    }

    public Long create(DeveloperEntity oDeveloperEntity) {
        oAuthService.OnlyAdmins();
        validate(oDeveloperEntity);
        oDeveloperEntity.setId(0L);
        return oDeveloperRepository.save(oDeveloperEntity).getId();
    }

    public Long update(DeveloperEntity oDeveloperEntity) {
        validate(oDeveloperEntity.getId());
        oAuthService.OnlyAdmins();
        validate(oDeveloperEntity);
        DeveloperEntity oOldDeveloperEntity = oDeveloperRepository.getById(oDeveloperEntity.getId());
        return oDeveloperRepository.save(oDeveloperEntity).getId();
    }

    public Long delete(Long id) {
        oAuthService.OnlyAdmins();
        validate(id);
        oDeveloperRepository.deleteById(id);
        if (oDeveloperRepository.existsById(id)) {
            throw new ResourceNotModifiedException("can't remove register " + id);
        } else {
            return id;
        }
    }

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

    public DeveloperEntity generateOne() {
        oAuthService.OnlyAdmins();
        return oDeveloperRepository.save(generateDeveloper());
    }

    public Long generateSome(Long amount) {
        oAuthService.OnlyAdmins();
        List<DeveloperEntity> developerToSave = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            developerToSave.add(generateDeveloper());
        }
        oDeveloperRepository.saveAll(developerToSave);
        return oDeveloperRepository.count();
    }

    private DeveloperEntity generateDeveloper() {
        DeveloperEntity oDeveloperEntity = new DeveloperEntity();

        oDeveloperEntity.setName(names.get(RandomHelper.getRandomInt(0, names.size() - 1)));
        oDeveloperEntity.setSurname(surnames.get(RandomHelper.getRandomInt(0, surnames.size() - 1)));
        oDeveloperEntity.setLastname(last_names.get(RandomHelper.getRandomInt(0, last_names.size() - 1)));

        oDeveloperEntity.setUsername((oDeveloperEntity.getName().toLowerCase()
               + oDeveloperEntity.getSurname().toLowerCase()).replaceAll("\\s", "") + RandomHelper.getRandomInt(111, 999));
        oDeveloperEntity.setEmail(oDeveloperEntity.getUsername() + "@projectum.net");

        return oDeveloperEntity;
    }

}
