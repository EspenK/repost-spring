package me.kverna.spring.repost.openapi;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SecurityScheme(
        name = "OAuth2PasswordBearer",
        type = SecuritySchemeType.OAUTH2,
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "jwt",
        flows = @OAuthFlows(
                password = @OAuthFlow(
                        tokenUrl = "/oauth/token",
                        scopes = {
                                @OAuthScope(name = "user", description = "User access")
                        }
                )
        )
)
public class OpenApiConfig {

}
