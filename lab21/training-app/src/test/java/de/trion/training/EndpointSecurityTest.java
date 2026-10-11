package de.trion.training;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class EndpointSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void health() throws Exception {
       mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health"))
               .andExpect(status().isOk());
    }

    @Test
    void actuator() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/actuator/info"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void actuatorAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/actuator/info")
                .with(httpBasic("actuator","actuator")))
                .andExpect(status().isOk());
    }

}
