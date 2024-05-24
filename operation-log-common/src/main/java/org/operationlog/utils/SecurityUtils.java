package org.operationlog.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;
import java.util.function.Supplier;

public class SecurityUtils {

    public static Long getLoginedInUserId() {

        //get logined operateUser
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (auth == null)
            return null;
        if (auth.getPrincipal() instanceof User) {
            String idString = ((User) auth.getPrincipal()).getUsername();
            try {
                return Long.valueOf(idString);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        String idString = null;
        if (auth.getPrincipal() instanceof CharSequence) {
            idString = auth.getPrincipal().toString();
        }

        if (idString == null) {
            return null;
        }

        if (!idString.equalsIgnoreCase("anonymousUser")) {
            try {
                return Long.valueOf(idString);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;

    }

    public static Supplier<Optional<Long>> userGetter() {
        return () -> Optional.ofNullable(SecurityUtils.getLoginedInUserId());
    }
}
