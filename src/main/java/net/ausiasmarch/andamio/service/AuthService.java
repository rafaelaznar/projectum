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
        DeveloperEntity oDeveloperSessionEntity = oDeveloperRepository.findByUsername((String)  oRequest.getAttribute("developer"));
        if (oDeveloperSessionEntity != null) {
            if (oDeveloperSessionEntity.getUsertype().getId().equals(UsertypeHelper.ADMIN.getUsertype())) {
                return true;
            }
        }
        return false;
    }

    public void OnlyAdmins() {
        DeveloperEntity oDeveloperSessionEntity = oDeveloperRepository.findByUsername((String)  oRequest.getAttribute("developer"));
        if (oDeveloperSessionEntity == null) {
            throw new UnauthorizedException("this request is only allowed to admin role");
        } else {
            if (!oDeveloperSessionEntity.getUsertype().getId().equals(UsertypeHelper.ADMIN.getUsertype())) {
                throw new UnauthorizedException("this request is only allowed to admin role");
            }
        }
    }

    /*    
    
    public boolean isLoggedIn() {
        UsuarioEntity oUsuarioSessionEntity = (UsuarioEntity) oHttpSession.getAttribute("usuario");
        if (oUsuarioSessionEntity == null) {
            return false;
        } else {
            return true;
        }
    }

    public Long getUserID() {
        UsuarioEntity oUsuarioSessionEntity = (UsuarioEntity) oHttpSession.getAttribute("usuario");
        if (oUsuarioSessionEntity != null) {
            return oUsuarioSessionEntity.getId();
        } else {
            throw new UnauthorizedException("this request is only allowed to auth users");
        }
    }


    public boolean isUser() {
        UsuarioEntity oUsuarioSessionEntity = (UsuarioEntity) oHttpSession.getAttribute("usuario");
        if (oUsuarioSessionEntity != null) {
            if (oUsuarioSessionEntity.getTipousuario().getId().equals(TipoUsuarioHelper.USER)) {
                return true;
            }
        }
        return false;
    }


    public void OnlyUsers() {
        UsuarioEntity oUsuarioSessionEntity = (UsuarioEntity) oHttpSession.getAttribute("usuario");
        if (oUsuarioSessionEntity == null) {
            throw new UnauthorizedException("this request is only allowed to user role");
        } else {
            if (!oUsuarioSessionEntity.getTipousuario().getId().equals(TipoUsuarioHelper.USER)) {
                throw new UnauthorizedException("this request is only allowed to user role");
            }
        }
    }

    public void OnlyAdminsOrUsers() {
        UsuarioEntity oUsuarioSessionEntity = (UsuarioEntity) oHttpSession.getAttribute("usuario");
        if (oUsuarioSessionEntity == null) {
            throw new UnauthorizedException("this request is only allowed to user or admin role");
        } else {

        }
    }

    public void OnlyAdminsOrOwnUsersData(Long id) {
        UsuarioEntity oUsuarioSessionEntity = (UsuarioEntity) oHttpSession.getAttribute("usuario");
        if (oUsuarioSessionEntity != null) {
            if (oUsuarioSessionEntity.getTipousuario().getId().equals(TipoUsuarioHelper.USER)) {
                if (!oUsuarioSessionEntity.getId().equals(id)) {
                    throw new UnauthorizedException("this request is only allowed for your own data");
                }
            }
        } else {
            throw new UnauthorizedException("this request is only allowed to user or admin role");
        }
    }

     */
}
