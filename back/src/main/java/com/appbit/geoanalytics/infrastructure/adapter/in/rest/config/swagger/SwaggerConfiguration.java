package com.appbit.geoanalytics.infrastructure.adapter.in.rest.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.servers.ServerVariables;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Profile("dev")
@Configuration(proxyBeanMethods = false)
public class SwaggerConfiguration {

    private static final String API_TITLE = "App BiT - GeoAnalytics API";

    private static final String API_DESCRIPTION = """
            API para análisis territorial orientado a la toma de decisiones públicas basada en evidencia.
            
            Permite consultar y visualizar datos agregados de concentración poblacional, movilidad,
            cobertura de red e indicadores sociales asociados a formación, empleabilidad, mentorías,
            experiencias territoriales y salud mental.
            
            La API expone contratos para procesar consultas en lenguaje natural, entregar información
            geográfica agregada para visualización en mapa y relacionar regiones, indicadores y fuentes
            de origen.
            
            Los datos deben interpretarse como información territorial agregada. La API no expone
            trayectos individuales identificables, datos personales ni inferencias sobre personas.
            """;

    private static final String API_PREFIX = "/api";
    private static final String SERVER_URL = API_PREFIX + "/{version}";
    private static final String VERSION_VARIABLE_NAME = "version";
    private static final String DEFAULT_API_VERSION = "v1";
    private static final String API_VERSION_PLACEHOLDER_PREFIX = API_PREFIX + "/{version}";

    private static final List<String> SUPPORTED_API_VERSIONS = List.of("v1");

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(apiInfo()).addServersItem(currentServer());
    }

    @Bean
    public OpenApiCustomizer apiVersionPathCustomizer() {
        return openApi -> {
            Paths originalPaths = openApi.getPaths();

            if (originalPaths == null || originalPaths.isEmpty()) {
                return;
            }

            Paths rewrittenPaths = new Paths();

            originalPaths.forEach((path, pathItem) -> {
                String rewrittenPath = removeApiVersionPrefix(path);

                if (rewrittenPaths.containsKey(rewrittenPath)) {
                    throw new IllegalStateException("Duplicate OpenAPI path after removing API version prefix: " + rewrittenPath);
                }

                rewrittenPaths.addPathItem(rewrittenPath, pathItem);
            });

            openApi.setPaths(rewrittenPaths);
        };
    }

    private Info apiInfo() {
        return new Info().title(API_TITLE).description(API_DESCRIPTION);
    }

    private Server currentServer() {
        return new Server().url(SERVER_URL).description("Ruta base de los recursos públicos de GeoAnalytics").variables(apiVersionVariables());
    }

    private ServerVariables apiVersionVariables() {
        ServerVariables variables = new ServerVariables();
        variables.put(VERSION_VARIABLE_NAME, apiVersionVariable());
        return variables;
    }

    private ServerVariable apiVersionVariable() {
        ServerVariable variable = new ServerVariable()
                ._default(DEFAULT_API_VERSION)
                .description("Versión pública de la API");
        SUPPORTED_API_VERSIONS.forEach(variable::addEnumItem);
        return variable;
    }

    private String removeApiVersionPrefix(String path) {
        if (path == null || path.isBlank()) {
            return path;
        }

        String normalizedPath = normalizePath(path);

        String pathWithoutPlaceholder = removePrefixIfPresent(normalizedPath, API_VERSION_PLACEHOLDER_PREFIX);

        if (!pathWithoutPlaceholder.equals(normalizedPath)) {
            return pathWithoutPlaceholder;
        }

        for (String version : SUPPORTED_API_VERSIONS) {
            String versionPrefix = API_PREFIX + "/" + version;

            String pathWithoutConcreteVersion = removePrefixIfPresent(normalizedPath, versionPrefix);

            if (!pathWithoutConcreteVersion.equals(normalizedPath)) {
                return pathWithoutConcreteVersion;
            }
        }

        return normalizedPath;
    }

    private String normalizePath(String path) {
        return path.startsWith("/") ? path : "/" + path;
    }

    private String removePrefixIfPresent(String path, String prefix) {
        if (path.equals(prefix)) {
            return "/";
        }
        if (path.startsWith(prefix + "/")) {
            return path.substring(prefix.length());
        }
        return path;
    }
}