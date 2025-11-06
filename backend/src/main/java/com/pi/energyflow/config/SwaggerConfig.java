package com.pi.energyflow.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Configuração do Swagger/OpenAPI para o projeto EnergyFlow.
 * 
 * Essa classe define as informações principais da API, 
 * bem como as respostas padrão e o esquema de autenticação JWT.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Define as informações gerais da API para exibição no Swagger UI.
     */
    @Bean
    OpenAPI energyFlowOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("EnergyFlow API")
                .description("""
                    API desenvolvida para o projeto **EnergyFlow** —
                    um sistema de gerenciamento e monitoramento energético que visa
                    otimizar o consumo, acompanhar métricas de eficiência e promover
                    sustentabilidade dentro de ambientes corporativos e residenciais.
                    """)
                .version("v1.0.0")
                .license(new License()
                    .name("Uso acadêmico - Projeto Interdisciplinar")
                    .url("https://github.com/queren-alves/energyflow")))
            .externalDocs(new ExternalDocumentation()
                .description("Repositório oficial do projeto no GitHub")
                .url("https://github.com/queren-alves/energyflow"))
            .components(new Components()
                .addSecuritySchemes("jwt_auth", createSecurityScheme()))
            .addSecurityItem(new SecurityRequirement().addList("jwt_auth"));
    }

    /**
     * Define as respostas globais que serão exibidas em todos os endpoints.
     */
    @Bean
    OpenApiCustomizer globalResponseOpenApiCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
            pathItem.readOperations().forEach(operation -> {
                ApiResponses responses = operation.getResponses();
                responses.addApiResponse("200", createApiResponse("Requisição bem-sucedida."));
                responses.addApiResponse("201", createApiResponse("Recurso criado com sucesso."));
                responses.addApiResponse("204", createApiResponse("Recurso excluído com sucesso."));
                responses.addApiResponse("400", createApiResponse("Requisição inválida."));
                responses.addApiResponse("401", createApiResponse("Não autorizado — token inválido ou ausente."));
                responses.addApiResponse("403", createApiResponse("Acesso proibido."));
                responses.addApiResponse("404", createApiResponse("Recurso não encontrado."));
                responses.addApiResponse("500", createApiResponse("Erro interno no servidor."));
            })
        );
    }

    private ApiResponse createApiResponse(String message) {
        return new ApiResponse().description(message);
    }

    /**
     * Configura o esquema de segurança para autenticação via JWT.
     */
    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
            .name("jwt_auth")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("""
                Utilize o token JWT gerado após o login. 
                O prefixo 'Bearer' será adicionado automaticamente.
                Exemplo: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
                """);
    }
}