import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import spring.SpringBootApp;
import spring.dto.AuthenticationRequestDto;
import spring.dto.RegistrationRequestDto;
import spring.entity.Role;
import spring.security.jwt.JwtTokenProvider;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootApp.class)
@Slf4j
public class RegisterUseCaseIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void testRegistrationValidRequestBody() throws Exception {
        MvcResult result  = mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new RegistrationRequestDto("userNew", "Password1?"))))
                .andExpect(status().isOk())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    public void testLoginValidRequestBody() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new AuthenticationRequestDto("user", "user"))))
                .andExpect(status().isOk())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreationDepositWithdrawStatement() throws Exception {
        JwtTokenProvider jwtTokenProvider = webApplicationContext.getBean(JwtTokenProvider.class);
        String generatedToken = "Bearer_" + jwtTokenProvider.createToken("user",
                Collections.singletonList(new Role("ROLE_USER")));
        MvcResult result = mockMvc.perform(post("/user/accounts/create_account")
                .header("Authorization", generatedToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        log.info("After account creation \n" + result.getResponse().getContentAsString());

        result = mockMvc.perform(put("/user/accounts/1/deposit")
                .content(asJsonString(500))
                .header("Authorization", generatedToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        log.info("After deposit \n" + result.getResponse().getContentAsString());

        result = mockMvc.perform(put("/user/accounts/1/withdraw")
                .content(asJsonString(300))
                .header("Authorization", generatedToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        log.info("After withdraw \n" + result.getResponse().getContentAsString());

        result = mockMvc.perform(get("/user/accounts/1/statement")
                .header("Authorization", generatedToken))
                .andExpect(status().isOk())
                .andReturn();
        log.info("After statement \n" + result.getResponse().getContentAsString());
    }

    public String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
