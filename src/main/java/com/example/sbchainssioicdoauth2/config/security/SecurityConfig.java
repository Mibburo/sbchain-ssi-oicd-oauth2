package com.example.sbchainssioicdoauth2.config.security;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Slf4j
@KeycloakConfiguration
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    // @Autowired
    // public void configureGlobal(AuthenticationManagerBuilder auth) {
    //   SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
    //   grantedAuthorityMapper.setPrefix("ROLE_");
    //   KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
    //   keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
    //   auth.authenticationProvider(keycloakAuthenticationProvider);
    // }
    // @Autowired
    // public void configureGlobal(
    //   AuthenticationManagerBuilder auth) throws Exception {
    //     KeycloakAuthenticationProvider keycloakAuthenticationProvider
    //      = keycloakAuthenticationProvider();
    //     keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(
    //       new SimpleAuthorityMapper());
    //     auth.authenticationProvider(keycloakAuthenticationProvider);
    // }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(keycloakAuthenticationProvider());
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    @Override
    @ConditionalOnMissingBean(HttpSessionManager.class)
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }

    // @Bean
    // public HttpSessionEventPublisher httpSessionEventPublisher() {
    //     return new HttpSessionEventPublisher();
    // }
    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     super.configure(http);
    //     http
    //         .authorizeRequests()
    //         .antMatchers("/multi/personalInfo/*").hasAuthority("taxis_user")
    //         .antMatchers("/multi/disqualifyingCrit/*").hasRole("personal_declaration")
    //         .antMatchers("/multi/residenceInfo/*").hasRole("user")
    //         //.antMatchers("/disqualifyingCrit/*").hasRole("user")
    //         .anyRequest().permitAll();
    //         // .and()
    //         // .logout()
    //         // .invalidateHttpSession(false)
    //         // .and()
    //         // .sessionManagement()
    //         // .sessionFixation().migrateSession()
    //         // .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    // }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                // .logout().logoutSuccessUrl("/home")
                // .and()
                .authorizeRequests()
                .antMatchers("/multi/**").hasAuthority("user")
                .antMatchers("/electricityBill/*").hasAuthority("user")
                .antMatchers("/fead/*").hasAuthority("user")
                .antMatchers("/employment/*").hasAuthority("user")
                .antMatchers("/contactDetails/*").hasAuthority("user")
                .antMatchers("/parenthood/*").hasAuthority("user")
                .antMatchers("/financialInfo/*").hasAuthority("user")
                .antMatchers("/assetInfo/*").hasAuthority("user")
                .antMatchers("/householdInfo/*").hasAuthority("user")
                .antMatchers("/notifications/*").hasAuthority("user")
                .antMatchers("/amounts/*").hasAuthority("user")
                .antMatchers("/**").permitAll();
    }

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakConfigResolver() {

            private KeycloakDeployment keycloakDeployment;

            @Override
            public KeycloakDeployment resolve(HttpFacade.Request facade) {
                if (keycloakDeployment != null) {
                    return keycloakDeployment;
                }

                String path = "/keycloak.json";
                InputStream configInputStream = getClass().getResourceAsStream(path);

                if (configInputStream == null) {
                    throw new RuntimeException("Could not load Keycloak deployment info: " + path);
                } else {
                    keycloakDeployment = KeycloakDeploymentBuilder.build(configInputStream);
                }

                return keycloakDeployment;
            }
        };
    }

    // //request debuger, remove
    // @Override
    // public void configure(WebSecurity web) throws Exception {
    //     web.debug(true);
    // }
    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            authorities.forEach(
                    authority -> {
                        if (authority instanceof OidcUserAuthority) {
                            OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;

                            OidcIdToken idToken = oidcUserAuthority.getIdToken();
                            OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

                            mappedAuthorities.add(new SimpleGrantedAuthority("taxis_user"));

                            // List<SimpleGrantedAuthority> groupAuthorities =
                            //       userInfo.getClaimAsStringList("claims").stream()
                            //           .map(g -> new SimpleGrantedAuthority("ROLE_" + g.toUpperCase()))
                            //           .collect(Collectors.toList());
                            // mappedAuthorities.addAll(groupAuthorities);
                        }
                    });

            return mappedAuthorities;
        };
    }

}
