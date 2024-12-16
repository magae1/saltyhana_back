package com.saltyhana.saltyhanaserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltyhana.saltyhanaserver.dto.AccountRequestDTO;
import com.saltyhana.saltyhanaserver.dto.AccountResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // JSON 직렬화/역직렬화에 사용

    @Test
    @DisplayName("/account 테스트")
    public void testGetAccount() throws Exception {
        // AccountRequestDTO 객체 생성
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setUserId(1L);  // 예시 사용자 ID
        accountRequestDTO.setStartDate("2024-12-01");  // 시작일
        accountRequestDTO.setEndDate("2024-12-31");    // 종료일

        // AccountRequestDTO를 JSON으로 변환
        String accountRequestJson = objectMapper.writeValueAsString(accountRequestDTO);

        // mockMvc를 사용하여 POST 요청을 보내고, 응답 상태 및 출력 확인
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountRequestJson)) // 요청 본문에 JSON 데이터 추가
                .andDo(print());  // 응답을 콘솔에 출력
    }
}
