package com.example.contract.web;

import com.example.contract.doamin.Contract;
import com.example.contract.enums.ContractState;
import com.example.contract.mock.ContractDetailImpl;
import com.example.contract.service.ContractService;
import com.example.contract.web.dto.ContractDetail;
import com.example.contract.web.dto.ContractSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Map;
import java.util.Optional;

import static com.example.contract.mock.ConvertUtil.convertContract;
import static com.example.contract.mock.MockUtil.asJsonString;
import static com.example.contract.mock.MockUtil.readJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("거래 데이터 컨트롤러 레이어")
@ExtendWith(SpringExtension.class)
@WebMvcTest(ContractControllerTest.class)
class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractService contractService;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ContractController(contractService)).addFilter(new CharacterEncodingFilter("UTF-8", true)).build();
    }

    @Test
    @DisplayName("계약 생성 API 테스트 케이스")
    public void write_ok() throws Exception {

        Map mockMap = readJson("json/contract/web/write_ok.json", Map.class);

        Contract mock = convertContract((Map) mockMap.get("contract"), (Map) mockMap.get("warrant"), ContractState.NORMAL);

        given(contractService.created(any())).willReturn(mock);

        String uri = "/contract";

        ContractSaveRequest dto = readJson("json/contract/web/contract_save_request.json", ContractSaveRequest.class);

        ResultActions action = mockMvc.perform(post(uri).content(asJsonString(dto)).contentType(MediaType.APPLICATION_JSON)).andDo(print());

        then(contractService).should().created(any());

        action.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mock.getId()))
                .andExpect(jsonPath("$.productId").value(mock.getProduct().getId()))
                .andExpect(jsonPath("$.warrantIds.size()").value(mock.getWarrants().size()))
                .andExpect(jsonPath("$.term").value(mock.getTerm()))
                .andExpect(jsonPath("$.startDate").value(mock.getStartDate()))
                .andExpect(jsonPath("$.endDate").value(mock.getEndDate()))
                .andExpect(jsonPath("$.premium").value(mock.getPremium()))
                .andExpect(jsonPath("$.state").value(mock.getState().name()));
    }

    @Test
    @DisplayName("계약 상세 조회 API 테스트 케이스")
    public void getOne_ok() throws Exception {

        Optional<ContractDetail> mockOptional = Optional.of(new ContractDetailImpl(readJson("json/contract/web/getOne_ok.json", Contract.class), ContractState.NORMAL));

        given(contractService.getOne(any())).willReturn(mockOptional);

        long mockId = 1L;

        String uri = "/contracts/" + mockId;

        ResultActions action = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON)).andDo(print());

        then(contractService).should().getOne(any());

        ContractDetail mock = mockOptional.get();

        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.term").value(mock.getTerm()))
                .andExpect(jsonPath("$.product.range").value(mock.getProduct().getRange()))
                .andExpect(jsonPath("$.product.title").value(mock.getProduct().getTitle()))
                .andExpect(jsonPath("$.product.id").value(mock.getProduct().getId()))
                .andExpect(jsonPath("$.warrants.size()").value(mock.getWarrants().size()))
                .andExpect(jsonPath("$.startDate").value(mock.getStartDate()))
                .andExpect(jsonPath("$.endDate").value(mock.getEndDate()))
                .andExpect(jsonPath("$.premium").value(mock.getPremium()))
                .andExpect(jsonPath("$.id").value(mock.getId()))
                .andExpect(jsonPath("$.state").value(mock.getState().name()))
        ;
    }

    @Test
    @DisplayName("계약 상세 조회 API 테스트 케이스 [컨텐츠가 없을 때 케이스]")
    public void getOne_no_content_case() throws Exception {

        given(contractService.getOne(any())).willReturn(Optional.empty());

        long mockId = 1L;

        String uri = "/contracts/" + mockId;

        ResultActions action = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON)).andDo(print());

        then(contractService).should().getOne(any());

        action.andExpect(status().isNoContent());
    }
}
