package net.ausiasmarch.andamio.service;

import javax.servlet.http.HttpServletRequest;
import net.ausiasmarch.andamio.bean.DeveloperBean;
import net.ausiasmarch.andamio.entity.DeveloperEntity;
import net.ausiasmarch.andamio.exception.UnauthorizedException;
import net.ausiasmarch.andamio.helper.JwtHelper;
import net.ausiasmarch.andamio.helper.UsertypeHelper;
import net.ausiasmarch.andamio.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {

    @Autowired
    private HttpServletRequest oRequest;

    @Autowired
    DeveloperRepository oDeveloperRepository;

    public String login(@RequestBody DeveloperBean oDeveloperBean) {
        if (oDeveloperBean.getPassword() != null) {
            DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsernameAndPassword(oDeveloperBean.getUsername(), oDeveloperBean.getPassword());
            if (oDeveloperEntity != null) {
                return JwtHelper.generateJWT(oDeveloperBean.getUsername());
            } else {
                throw new UnauthorizedException("login or password incorrect");
            }
        } else {
            throw new UnauthorizedException("wrong password");
        }
    }

    public DeveloperEntity check() {
        String strDeveloperName = (String) oRequest.getAttribute("developer");
        if (strDeveloperName != null) {
            DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
            return oDeveloperEntity;
        } else {
            throw new UnauthorizedException("No active session");
        }
    }

    public boolean isAdmin() {
        DeveloperEntity oDeveloperSessionEntity = oDeveloperRepository.findByUsername((String) oRequest.getAttribute("developer"));
        if (oDeveloperSessionEntity != null) {
            if (oDeveloperSessionEntity.getUsertype().getId().equals(UsertypeHelper.ADMIN)) {
                return true;
            }
        }
        return false;
    }

    public void OnlyAdmins() {
        DeveloperEntity oDeveloperSessionEntity = oDeveloperRepository.findByUsername((String) oRequest.getAttribute("developer"));
        if (oDeveloperSessionEntity == null) {
            throw new UnauthorizedException("this request is only allowed to admin role");
        } else {
            if (!oDeveloperSessionEntity.getUsertype().getId().equals(UsertypeHelper.ADMIN)) {
                throw new UnauthorizedException("this request is only allowed to admin role");
            }
        }
    }

    public boolean isLoggedIn() {
        String strDeveloperName = (String) oRequest.getAttribute("developer");
        if (strDeveloperName == null) {
            return false;
        } else {
            return true;
        }
    }

    public Long getUserID() {
        String strDeveloperName = (String) oRequest.getAttribute("developer");
        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
        if (oDeveloperEntity != null) {
            return oDeveloperEntity.getId();
        } else {
            throw new UnauthorizedException("this request is only allowed to auth developers");
        }
    }

    public boolean isReviewer() {
        String strDeveloperName = (String) oRequest.getAttribute("developer");
        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
        if (oDeveloperEntity != null) {
            if (oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.REVIEWER)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDeveloper() {
        String strDeveloperName = (String) oRequest.getAttribute("developer");
        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
        if (oDeveloperEntity != null) {
            if (oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.DEVELOPER)) {
                return true;
            }
        }
        return false;
    }

    public void OnlyDevelopers() {
        String strDeveloperName = (String) oRequest.getAttribute("developer");
        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
        if (oDeveloperEntity == null) {
            throw new UnauthorizedException("this request is only allowed to developer role");
        } else {
            if (!oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.DEVELOPER)) {
                throw new UnauthorizedException("this request is only allowed to developer role");
            }
        }
    }

    public void OnlyAdminsOrReviewers() {
        String strDeveloperName = (String) oRequest.getAttribute("developer");
        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
        if (oDeveloperEntity == null) {
            throw new UnauthorizedException("this request is only allowed to admin or reviewer role");
        } else {
            if (oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.ADMIN)) {
            } else {
                if (oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.DEVELOPER)) {
                } else {
                    throw new UnauthorizedException("this request is only allowed to admin or reviewer role");
                }
            }
        }
    }

    public void OnlyOwnDevelopersData(Long id) {
        String strDeveloperName = (String) oRequest.getAttribute("developer");
        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
        if (oDeveloperEntity != null) {
            if (oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.DEVELOPER)) {
                if (!oDeveloperEntity.getId().equals(id)) {
                    throw new UnauthorizedException("this request is only allowed for your own developer data");
                }
            } else {
                throw new UnauthorizedException("this request is only allowed to developer role");
            }
        } else {
            throw new UnauthorizedException("this request is only allowed to developer role");
        }
    }

    public void OnlyOwnReviewerData(Long id) {
        String strDeveloperName = (String) oRequest.getAttribute("developer");
        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
        if (oDeveloperEntity != null) {
            if (oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.REVIEWER)) {
                if (!oDeveloperEntity.getId().equals(id)) {
                    throw new UnauthorizedException("this request is only allowed for your own reviewer data");
                }
            } else {
                throw new UnauthorizedException("this request is only allowed to reviewer role");
            }
        } else {
            throw new UnauthorizedException("this request is only allowed to reviewer role");
        }
    }

}
