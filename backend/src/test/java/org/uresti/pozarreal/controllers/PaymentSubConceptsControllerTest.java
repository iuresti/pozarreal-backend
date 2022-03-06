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
import org.uresti.pozarreal.dto.PaymentSubConcept;
import org.uresti.pozarreal.service.PaymentSubConceptsService;
import org.uresti.pozarreal.testmode.config.TestAuthenticationProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@WebMvcTest(PaymentSubConceptsController.class)
@ActiveProfiles("tests")
public class PaymentSubConceptsControllerTest {
    private final String BASE_URL = "/api/paymentSubConcepts";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentSubConceptsService paymentSubConceptsService;

    @MockBean
    private TestAuthenticationProvider testAuthenticationProvider;

    @Test
    @WithMockUser()
    public void givenAPaymentConceptId_whenFindAllByConcept_thenListOfPaymentsIsReturned() throws Exception {
        // Given:
        String paymentConceptId = UUID.randomUUID().toString();

        List<PaymentSubConcept> paymentsByConcept = new LinkedList<>();

        Mockito.when(paymentSubConceptsService.findAllByConcept(paymentConceptId)).thenReturn(paymentsByConcept);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/concept/" + paymentConceptId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(paymentSubConceptsService).findAllByConcept(paymentConceptId);
        Mockito.verifyNoMoreInteractions(paymentSubConceptsService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenAPaymentSubConcept_whenSave_thenPaymentSubConceptIsSaved() throws Exception {
        // Given:
        PaymentSubConcept paymentSubConcept = PaymentSubConcept.builder()
                .conceptId("conceptId")
                .id("id")
                .label("label")
                .build();

        Mockito.when(paymentSubConceptsService.save(paymentSubConcept)).thenReturn(paymentSubConcept);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(paymentSubConcept)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        Mockito.verify(paymentSubConceptsService).save(paymentSubConcept);
        Mockito.verifyNoMoreInteractions(paymentSubConceptsService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenANewPaymentSubConcept_whenUpdate_thenPaymentSubConceptIsUpdated() throws Exception {
        // Given:
        PaymentSubConcept paymentSubConcept = PaymentSubConcept.builder()
                .conceptId("conceptId")
                .id("id")
                .label("label")
                .build();

        Mockito.when(paymentSubConceptsService.update(paymentSubConcept)).thenReturn(paymentSubConcept);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(paymentSubConcept)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(paymentSubConceptsService).update(paymentSubConcept);
        Mockito.verifyNoMoreInteractions(paymentSubConceptsService);
    }
}