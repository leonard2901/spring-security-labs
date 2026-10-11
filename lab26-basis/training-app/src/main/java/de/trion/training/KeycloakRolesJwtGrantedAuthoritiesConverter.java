package de.trion.training;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Extracts Keycloak roles from the JWT and converts them to Spring Security GrantedAuthorities with
 * the prefix "ROLE_".
 * Roles are expected to be in the "resource_access" claim of the JWT.
 * The full path to the roles is "resource_access.training-app-frontend.roles".
 */
public class KeycloakRolesJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public Collection<GrantedAuthority> convert(final Jwt jwt) {
        // Merge authorities from the default converter and the Keycloak roles
        return Stream.concat(
                        defaultGrantedAuthoritiesConverter.convert(jwt).stream(),
                        extractResourceRoles(jwt).stream()
                )
                .collect(Collectors.toSet());
    }

    /**
     * Extracts the roles from the "resource_access" claim of the JWT.
     * The format looks something like this:
     * <pre>
     * {
     *     "resource_access": {
     *       "training-app-frontend": {
     *         "roles": [
     *           "ADMIN"
     *         ]
     *       }
     *     }
     * }
     */
    private Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt) {

        return Optional.ofNullable(
                        jwt.getClaimAsMap("resource_access")
                )
                .map(resourceAccess -> resourceAccess.get("training-app-frontend"))
                .map(Map.class::cast)
                .map(client -> client.get("roles"))
                .map(Collection.class::cast)
                .map(roles -> ((Collection<String>) roles).stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toSet())
                ).orElse(Collections.emptySet());

    }
}
