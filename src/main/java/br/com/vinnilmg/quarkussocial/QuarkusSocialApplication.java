package br.com.vinnilmg.quarkussocial;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Api Quarkus Social",
                version = "1.0",
                contact = @Contact(
                        name = "Vinnilmg",
                        url = "http://localhost:8080",
                        email = "user@mail.com"
                )
        )
)
public class QuarkusSocialApplication extends Application {
}
