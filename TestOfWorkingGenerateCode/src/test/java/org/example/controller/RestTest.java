package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
class RestTest extends BaseTest {

    protected void postTest(String request,String params,String body) {
        request+=params.isEmpty()?(params):"?"+params;
        try {
            this.mockMvc.perform(post(request)
                            .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isOk());
        } catch (Exception ex) {
            log.info(ex.getMessage());
            assert false;
        }
    }
    protected void putTest(String request,String params,String body) {
        request+=params.isEmpty()?(params):"?"+params;
        try {
            this.mockMvc.perform(put(request)
                            .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isOk());
        } catch (Exception ex) {
            log.info(ex.getMessage());
            assert false;
        }
    }
    protected void patchTest(String request,String params,String body) {
        request+=params.isEmpty()?(params):"?"+params;
        try {
            this.mockMvc.perform(patch(request)
                            .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isOk());
        } catch (Exception ex) {
            log.info(ex.getMessage());
            assert false;
        }
    }
    protected void deleteTest(String request,String params) {
        request+=params.isEmpty()?(params):"?"+params;
        try {
            this.mockMvc.perform(delete(request))
                    .andDo(print())
                    .andExpect(status().isOk());
        } catch (Exception ex) {
            log.info(ex.getMessage());
            assert false;
        }
    }
    protected void getTest(String request,String params) {
        request+=params.isEmpty()?(params):"?"+params;
        try {
            this.mockMvc.perform(get(request))
                    .andDo(print())
                    .andExpect(status().isOk());
        } catch (Exception ex) {
            log.info(ex.getMessage());
            assert false;
        }
    }

}