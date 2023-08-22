package nl.workshophub.workshophubeindopdrachtbackend.util;

import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CheckAuthorization {

    public static boolean isAuthorized(User user, Collection<GrantedAuthority> grantedAuthorityCollection, String loggedInUserEmail) {
        for (GrantedAuthority a : grantedAuthorityCollection) {
            if (a.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return user.getEmail().equalsIgnoreCase(loggedInUserEmail);
    }
}
