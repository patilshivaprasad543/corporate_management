package com.corporate.travel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "employee", roles = {"EMPLOYEE"})
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testReceiptOcrSimulation() throws Exception {
        mockMvc.perform(post("/api/expenses/ocr-scan?fileName=dinner_bill.jpg&amount=1450.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.merchantName").value("Mainland China Restaurant & Lounge"))
                .andExpect(jsonPath("$.data.amount").value(1450.00))
                .andExpect(jsonPath("$.data.policyCompliant").value(true));
    }
}
