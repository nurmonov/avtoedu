package org.example.fileupload.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OpenApiConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AutoEdu File Upload API")
                        .version("1.0")
                        .description("Fayl yuklash va user bilan bog'lash API"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    public OpenApiCustomizer multipartFixCustomizer() {
        return openApi -> {
            var pathItem = openApi.getPaths().get("/api/files/upload");
            if (pathItem == null || pathItem.getPost() == null) return;

            var rb = pathItem.getPost().getRequestBody();
            if (rb == null) {
                rb = new io.swagger.v3.oas.models.parameters.RequestBody().required(true);
            }

            var content = rb.getContent();
            if (content == null) content = new io.swagger.v3.oas.models.media.Content();

            var mediaType = content.computeIfAbsent("multipart/form-data", k -> new io.swagger.v3.oas.models.media.MediaType());

            var schema = new Schema<>()
                    .type("object")
                    .addProperties("files", new ArraySchema()
                            .items(new Schema<>().type("string").format("binary")))
                    .addProperties("userId", new Schema<>().type("integer"))
                    .addProperties("textContent", new Schema<>().type("string"))
                    .required(java.util.List.of("files", "userId"));

            mediaType.setSchema(schema);
            rb.setContent(content);
            pathItem.getPost().setRequestBody(rb);
        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/")
                .setCachePeriod(3600);
    }


}