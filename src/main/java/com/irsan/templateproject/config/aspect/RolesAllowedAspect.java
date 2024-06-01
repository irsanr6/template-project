package com.irsan.templateproject.config.aspect;

import com.irsan.templateproject.exception.UnauthorizedException;
import com.irsan.templateproject.utility.annotation.RolesAllowed;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 01/06/2024
 */
@Aspect
@Component
public class RolesAllowedAspect {

    @Around("@annotation(rolesAllowed)")
    public Object checkRolesAllowed(ProceedingJoinPoint pjp, RolesAllowed rolesAllowed) throws Throwable {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities();

        List<String> collectRoles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        if (Arrays.stream(rolesAllowed.roles()).noneMatch(role -> collectRoles.contains(role.name()))) {
            throw new UnauthorizedException("You don't have permission to access this resource");
        }

        return pjp.proceed();
    }

}
