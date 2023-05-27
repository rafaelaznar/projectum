package es.blaster.projectum.service;

import javax.servlet.http.HttpServletRequest;
import es.blaster.projectum.bean.UserBean;
import es.blaster.projectum.exception.UnauthorizedException;
import es.blaster.projectum.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private HttpServletRequest oRequest;

    @Autowired
    DeveloperRepository oDeveloperRepository;

    public UserBean check() {
        if (oRequest.getAttribute("user") != null) {
            if (oRequest.getAttribute("type") != null) {
                return new UserBean((String) oRequest.getAttribute("user"), (String) oRequest.getAttribute("type"));
            } else {
                throw new UnauthorizedException("No active session");
            }
        } else {
            throw new UnauthorizedException("No active session");
        }
    }

    public boolean isAdmin() {
        return oRequest.getAttribute("type").equals("admin");
    }

    public void OnlyAdmins() {
        if (!oRequest.getAttribute("type").equals("admin")) {
            throw new UnauthorizedException("this request is only allowed to admin role");
        }
    }

    public void OnlyAdminsOrViewers() {
        if (!oRequest.getAttribute("type").equals("admin") && !oRequest.getAttribute("type").equals("viewer")) {
            throw new UnauthorizedException("this request is only allowed to admin or viewer role");
        }
    }

    public boolean isLoggedIn() {
        if (oRequest.getAttribute("user") != null) {
            return false;
        } else {
            return true;
        }
    }

//    public Long getUserID() {
//        String strDeveloperName = (String) oRequest.getAttribute("user");
//        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
//        if (oDeveloperEntity != null) {
//            return oDeveloperEntity.getId();
//        } else {
//            throw new UnauthorizedException("this request is only allowed to auth developers");
//        }
//    }
//    public boolean isReviewer() {
//        String strDeveloperName = (String) oRequest.getAttribute("user");
//        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
//        if (oDeveloperEntity != null) {
//            if (oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.REVIEWER)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean isDeveloper() {
//        String strDeveloperName = (String) oRequest.getAttribute("user");
//        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
//        if (oDeveloperEntity != null) {
//            if (oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.DEVELOPER)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void OnlyDevelopers() {
//        String strDeveloperName = (String) oRequest.getAttribute("user");
//        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
//        if (oDeveloperEntity == null) {
//            throw new UnauthorizedException("this request is only allowed to developer role");
//        } else {
//            if (!oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.DEVELOPER)) {
//                throw new UnauthorizedException("this request is only allowed to developer role");
//            }
//        }
//    }
//
//    public void OnlyAdminsOrReviewers() {
//        String strDeveloperName = (String) oRequest.getAttribute("user");
//        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
//        if (oDeveloperEntity == null) {
//            throw new UnauthorizedException("this request is only allowed to admin or reviewer role");
//        } else {
//            if (oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.ADMIN)) {
//            } else {
//                if (oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.DEVELOPER)) {
//                } else {
//                    throw new UnauthorizedException("this request is only allowed to admin or reviewer role");
//                }
//            }
//        }
//    }
//
//    public void OnlyOwnDevelopersData(Long id) {
//        String strDeveloperName = (String) oRequest.getAttribute("user");
//        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
//        if (oDeveloperEntity != null) {
//            if (oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.DEVELOPER)) {
//                if (!oDeveloperEntity.getId().equals(id)) {
//                    throw new UnauthorizedException("this request is only allowed for your own developer data");
//                }
//            } else {
//                throw new UnauthorizedException("this request is only allowed to developer role");
//            }
//        } else {
//            throw new UnauthorizedException("this request is only allowed to developer role");
//        }
//    }
//
//    public void OnlyOwnReviewerData(Long id) {
//        String strDeveloperName = (String) oRequest.getAttribute("user");
//        DeveloperEntity oDeveloperEntity = oDeveloperRepository.findByUsername(strDeveloperName);
//        if (oDeveloperEntity != null) {
//            if (oDeveloperEntity.getUsertype().getId().equals(UsertypeHelper.REVIEWER)) {
//                if (!oDeveloperEntity.getId().equals(id)) {
//                    throw new UnauthorizedException("this request is only allowed for your own reviewer data");
//                }
//            } else {
//                throw new UnauthorizedException("this request is only allowed to reviewer role");
//            }
//        } else {
//            throw new UnauthorizedException("this request is only allowed to reviewer role");
//        }
//    }
}
