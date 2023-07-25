package com.candidate.mastcheshmi.onboarding.exceptioin;

import static com.candidate.mastcheshmi.onboarding.constants.TestConstants.SOME_PASSWORD;
import static com.candidate.mastcheshmi.onboarding.constants.TestConstants.SOME_USER_NAME;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.candidate.mastcheshmi.onboarding.controller.dto.LogonRequestDto;
import com.candidate.mastcheshmi.onboarding.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UnexpectedErrorIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    private static final String SERVICE_URI = "/client-api/v1/logon";

    @Test
    void checkMortgageFeasibility_validRequest_isFeasible() throws Exception {
        // Given
        LogonRequestDto request = new LogonRequestDto(SOME_USER_NAME, SOME_PASSWORD);
        String reqBody = objectMapper.writeValueAsString(request);

        Mockito.when(customerService.authenticate(any(), any())).thenThrow(new IllegalStateException());

        //when
        mockMvc.perform(post(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
            // Then
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.type").value("/problems/server"))
            .andExpect(jsonPath("$.title").value("Server Error"))
            .andExpect(jsonPath("$.trackingCode").value(notNullValue()));
    }
}
