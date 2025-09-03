package com.junit_3;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.junit_3.controller.UserController;
import com.junit_3.entity.User;
import com.junit_3.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@Test
	void getUser_Found_Returns200() throws Exception {
		when(userService.getUserName(1)).thenReturn("Alice");

		mockMvc.perform(get("/users/1")).andExpect(status().isOk()).andExpect(content().string("Alice"));
	}

	@Test
	void getUser_NotFound_Returns404() throws Exception {
		when(userService.getUserName(2)).thenReturn("Unknown User");

		mockMvc.perform(get("/users/2")).andExpect(status().isNotFound());
	}

	@Test
	void createUser_Found_Returns201() throws Exception{
		when(userService.createUser(any(User.class))).thenReturn(new User(10,"Alice"));
		
		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":10,\"name\":\"Alice\"}"))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").value(10))
		.andExpect(jsonPath("$.name").value("Alice"));

	}
	@Test
	public void updateUser_Test() throws Exception {
		when(userService.updateUser(eq(1), any(User.class))).thenReturn(Optional.of(new User(1,"Sam")) );
		
		mockMvc.perform(put("/users/1")
		.contentType(MediaType.APPLICATION_JSON)
		.content("{\"id\":1,\"name\":\"Sam\"}"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.name").value("Sam"));
	}
	@Test
	public void updateUser_Test_NotFound() throws Exception {
		when(userService.updateUser(eq(99), any(User.class))).thenReturn(Optional.empty());
		
		mockMvc.perform(put("/users/99")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":99,\"name\":\"Sam\"}"))
		.andExpect(status().isNotFound());
	}

}
