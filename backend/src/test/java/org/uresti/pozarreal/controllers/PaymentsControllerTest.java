package org.uresti.pozarreal.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.uresti.pozarreal.config.controller.LocalDateAdapter;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.dto.Payment;
import org.uresti.pozarreal.dto.PaymentFilter;
import org.uresti.pozarreal.dto.PaymentView;
import org.uresti.pozarreal.service.PaymentsService;
import org.uresti.pozarreal.testmode.config.TestAuthenticationProvider;

import java.time.LocalDate;
import java.util.*;

@WebMvcTest(PaymentsController.class)
@ActiveProfiles("tests")
public class PaymentsControllerTest {

    private final String BASE_URL = "/api/payments";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentsService paymentsService;

    @MockBean
    private SessionHelper sessionHelper;

    @MockBean
    private TestAuthenticationProvider testAuthenticationProvider;

    private Gson gson;

    @BeforeEach
    public void setup() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAPaymentFilter_whenGetPayments_thenIsReturnedAListOfPaymentView() throws Exception {
        // Given:
        PaymentFilter paymentFilter = PaymentFilter.builder().build();

        List<PaymentView> payments = new LinkedList<>();

        Mockito.when(paymentsService.getPayments(paymentFilter)).thenReturn(payments);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(paymentsService).getPayments(paymentFilter);
        Mockito.verifyNoMoreInteractions(paymentsService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAPaymentId_whenGetPayment_thenPaymentIsReturned() throws Exception {
        // Given:
        String paymentId = UUID.randomUUID().toString();

        Payment payment = Payment.builder()
                .id("id")
                .amount(100.0)
                .houseId("houseId")
                .paymentConceptId("Concept")
                .build();

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", "id"))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(paymentsService.getPayment(paymentId, loggedUser.getUserId())).thenReturn(payment);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);
        Mockito.when(sessionHelper.getUserIdForLoggedUser(principal)).thenReturn(loggedUser.getUserId());

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/" + paymentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(payment)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(paymentsService).getPayment(paymentId, loggedUser.getUserId());
        Mockito.verifyNoMoreInteractions(paymentsService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAPayment_whenSave_thenPaymentIsSaved() throws Exception {
        // Given:
        Payment payment = Payment.builder()
                .id("id")
                .amount(100.0)
                .houseId("houseId")
                .paymentConceptId("Concept")
                .build();

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", "id"))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(paymentsService.save(payment, principal)).thenReturn(payment);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(payment)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        Mockito.verify(paymentsService).save(payment, principal);
        Mockito.verifyNoMoreInteractions(paymentsService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAPaymentId_whenDelete_thenFindAndDeletePaymentById() throws Exception {
        // Given:
        String paymentId = UUID.randomUUID().toString();

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", "id"))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete(BASE_URL + "/" + paymentId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(paymentsService).delete(paymentId, principal);
        Mockito.verifyNoMoreInteractions(paymentsService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAPaymentId_whenValidatePayment_then_UpdatePaymentValidationIsUpdated() throws Exception {
        // Given:
        String paymentId = UUID.randomUUID().toString();

        Payment payment = Payment.builder()
                .id("id")
                .amount(100.0)
                .houseId("houseId")
                .paymentConceptId("Concept")
                .validated(true)
                .build();

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", "id"))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(paymentsService.validatePayment(paymentId)).thenReturn(payment);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.patch(BASE_URL + "/" + paymentId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(paymentsService).validatePayment(paymentId);
        Mockito.verifyNoMoreInteractions(paymentsService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "REPRESENTATIVE")
    public void givenAPaymentId_whenConflictPayment_thenPaymentConflictIsUpdated() throws Exception {
        // Given:
        String paymentId = UUID.randomUUID().toString();

        Payment payment = Payment.builder()
                .id("id")
                .amount(100.0)
                .houseId("houseId")
                .conflict(true)
                .paymentConceptId("Concept")
                .build();

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", "id"))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(paymentsService.conflictPayment(paymentId)).thenReturn(payment);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL + "/" + paymentId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(paymentsService).conflictPayment(paymentId);
        Mockito.verifyNoMoreInteractions(paymentsService);
    }
}
