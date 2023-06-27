package nl.workshophub.workshophubeindopdrachtbackend.util;

import nl.workshophub.workshophubeindopdrachtbackend.models.Authority;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CheckAuthorization {

    public static boolean isAuthorized(User user, Collection<GrantedAuthority> grantedAuthorityCollection, String loggedInUserEmail) {

        //admin is allowed to do everything
        for (GrantedAuthority a : grantedAuthorityCollection) {
            if (a.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }

        return user.getEmail().equalsIgnoreCase(loggedInUserEmail);
    }
}
