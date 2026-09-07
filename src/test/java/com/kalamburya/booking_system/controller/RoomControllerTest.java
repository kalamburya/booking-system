package com.kalamburya.booking_system.controller;

import com.kalamburya.booking_system.config.SecurityConfig;
import com.kalamburya.booking_system.service.CustomUserDetailsService;
import com.kalamburya.booking_system.service.JwtService;
import com.kalamburya.booking_system.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
@Import(SecurityConfig.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "USER")
    void createRoom_shouldReturn403_whenUserIsNotAdmin() throws Exception {
        mockMvc.perform(post("/api/rooms")
                        .contentType("application/json")
                        .content("""
                                {"number":"101","type":"SINGLE","price":50.0,"capacity":2,"description":"test"}
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllRooms_shouldReturn200_forAnyAuthenticatedUser() throws Exception {
        when(roomService.getAllRooms()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk());
    }
}