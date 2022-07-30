package com.devcourse.checkmoi.template;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.global.security.oauth.OAuthService;
import com.devcourse.checkmoi.global.security.oauth.UserProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
public abstract class IntegrationTest {

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected RestDocumentationResultHandler restDocs;

    @Autowired
    private OAuthService oAuthService;


    private static UserProfile createUserProfile(String name) {
        return UserProfile.builder()
            .oauthId(name)
            .provider("kakao")
            .name(name)
            .email(name + "@gmail.com")
            .profileImgUrl("url")
            .build();
    }

    @BeforeEach
    protected void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .apply(documentationConfiguration(provider))
            .apply(springSecurity())
            .alwaysDo(print())
            .build();
    }

    @BeforeEach
    protected void setUpRestDocs(WebApplicationContext context) {
        this.restDocs = document(
            "{class-name}/{method-name}",
            preprocessRequest(prettyPrint()),
            preprocessResponse(removeHeaders(
                "Transfer-Encoding",
                "Date",
                "Keep-Alive",
                "Connection"
            ), prettyPrint())
        );
    }


    protected String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    protected TokenWithUserInfo getTokenWithUserInfo() {
        return oAuthService.register(createUserProfile(UUID.randomUUID().toString().substring(19)));
    }

}