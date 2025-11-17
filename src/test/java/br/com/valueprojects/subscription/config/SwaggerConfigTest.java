package br.com.valueprojects.subscription.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SwaggerConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void shouldCreateOpenAPIBean() {
        OpenAPI openAPI = applicationContext.getBean(OpenAPI.class);
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Subscription Service API", openAPI.getInfo().getTitle());
        assertEquals("0.0.1-SNAPSHOT", openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getInfo().getDescription());
        assertNotNull(openAPI.getInfo().getContact());
        assertNotNull(openAPI.getInfo().getLicense());
    }

    @Test
    void shouldHaveCorrectContactInfo() {
        OpenAPI openAPI = applicationContext.getBean(OpenAPI.class);
        assertEquals("Subscription Service Team", openAPI.getInfo().getContact().getName());
        assertEquals("contact@subscription-service.com", openAPI.getInfo().getContact().getEmail());
    }

    @Test
    void shouldHaveCorrectLicenseInfo() {
        OpenAPI openAPI = applicationContext.getBean(OpenAPI.class);
        assertEquals("Apache 2.0", openAPI.getInfo().getLicense().getName());
        assertEquals("https://www.apache.org/licenses/LICENSE-2.0.html", openAPI.getInfo().getLicense().getUrl());
    }
}

