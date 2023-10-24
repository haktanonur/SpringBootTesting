package com.onurhaktan.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onurhaktan.springboot.model.Employee;
import com.onurhaktan.springboot.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTests {

    // we can create HTTP requests with MockMvc to check if Controller methods provide the correct responses
    @Autowired
    private MockMvc mockMvc;

    // Components mocked with @MockBean are used to determine how a specific Controller method will behave in a real application.
    @MockBean
    private EmployeeService employeeService;

    // The ObjectMapper is used to handle JSON data, ensuring that Controller methods interact correctly with JSON data
    @Autowired
    private ObjectMapper objectMapper;

    // JUnit test for createEmployee controller
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Onur")
                .lastName("Haktan")
                .email("onur@email.com")
                .build();

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behaviour that we  are going test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or output using assert statements
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));

    }

    // JUnit test for getAllEmployees controller
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {

        // given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder()
                .firstName("Onur")
                .lastName("Haktan")
                .email("onur@email.com")
                .build());

        listOfEmployees.add(Employee.builder()
                .firstName("Göksu")
                .lastName("Turaç")
                .email("göksu@email.com")
                .build());

        BDDMockito.given(employeeService.getAllEmployees())
                .willReturn(listOfEmployees);

        // when - action or behaviour that we  are going test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the result or output using assert statements
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(listOfEmployees.size())))
                .andDo(MockMvcResultHandlers.print());
    }

}
