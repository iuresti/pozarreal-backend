package org.uresti.pozarreal.controllers;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.uresti.pozarreal.dto.PaymentConcept;
import org.uresti.pozarreal.service.PaymentConceptsService;
import org.uresti.pozarreal.testmode.config.TestAuthenticationProvider;

import java.util.LinkedList;
import java.util.List;

@WebMvcTest(PaymentConceptsController.class)
@ActiveProfiles("tests")
public class PaymentConceptsControllerTest {
    private final String BASE_URL = "/api/paymentConcepts";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentConceptsService paymentConceptsService;

    @MockBean
    private TestAuthenticationProvider testAuthenticationProvider;

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void given_whenFindAll_thenAllPaymentsConceptsAreReturned() throws Exception {
        // Given:
        List<PaymentConcept> paymentConcepts = new LinkedList<>();

        Mockito.when(paymentConceptsService.findAll()).thenReturn(paymentConcepts);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(paymentConceptsService).findAll();
        Mockito.verifyNoMoreInteractions(paymentConceptsService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAPaymentConcept_whenSave_thenPaymentConceptIsSaved() throws Exception {
        // Given:
        PaymentConcept paymentConcept = PaymentConcept.builder()
                .id("id")
                .label("label")
                .build();

        Mockito.when(paymentConceptsService.save(paymentConcept)).thenReturn(paymentConcept);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(paymentConcept)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        Mockito.verify(paymentConceptsService).save(paymentConcept);
        Mockito.verifyNoMoreInteractions(paymentConceptsService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAPaymentConcept_whenUpdate_thenPaymentConceptIsUpdated() throws Exception {
        // Given:
        PaymentConcept paymentConcept = PaymentConcept.builder()
                .id("id")
                .label("label")
                .build();

        Mockito.when(paymentConceptsService.update(paymentConcept)).thenReturn(paymentConcept);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(paymentConcept)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(paymentConceptsService).update(paymentConcept);
        Mockito.verifyNoMoreInteractions(paymentConceptsService);
    }
}
