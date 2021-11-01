package com.application.administration.web.configuration;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ConcurrentHashMap;

public class HeaderBasedConfigResolver implements KeycloakConfigResolver {

    public static final String BACKEND_SUFFIX = "-backend";

    private final ConcurrentHashMap<String, KeycloakDeployment> cache = new ConcurrentHashMap<>();

    @Bean
    public KeycloakDeployment getReam(String realmName) {
        return cache.get(realmName);
    }
    
    @Override
    public KeycloakDeployment resolve(HttpFacade.Request request) {
        try {
            String realm = request.getHeader("Origin").split("\\.")[0].substring(7);
            if(!cache.containsKey(realm)) {
                var conf = new AdapterConfig();

                conf.setRealm(realm);
                conf.setAuthServerUrl("http://localhost:8091/auth");
                conf.setResource(realm + BACKEND_SUFFIX);
                conf.setUseResourceRoleMappings(true);
                conf.setBearerOnly(true);
                conf.setCors(true);
                var deployment = KeycloakDeploymentBuilder.build(conf);
                cache.put(realm, deployment);
            }
            return cache.get(realm);
        }catch (Exception e) {
            throw new RuntimeException("Unable to find realm key on request headers");
        }

    }


}
