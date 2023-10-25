package com.onurhaktan.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onurhaktan.springboot.model.Employee;
import com.onurhaktan.springboot.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import  org.hamcrest.CoreMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
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

        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behaviour that we  are going test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or output using assert statements
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
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

        given(employeeService.getAllEmployees())
                .willReturn(listOfEmployees);

        // when - action or behaviour that we  are going test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",
                        CoreMatchers.is(listOfEmployees.size())))
                .andDo(print());
    }

    // positive scenario - valid employee id
    // JUnit test for getEmployeeById controller
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .firstName("Onur")
                .lastName("Haktan")
                .email("onur@email.com")
                .build();

        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(employee));

        // when - action or behaviour that we  are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }

    // negative scenario - invalid employee id
    // JUnit test for getEmployeeById controller
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .firstName("Onur")
                .lastName("Haktan")
                .email("onur@email.com")
                .build();

        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.empty());

        // when - action or behaviour that we  are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the result or output using assert statements
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // Junit test for updatedEmployee REST API - positive scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;

        Employee savedEmployee = Employee.builder()
                .firstName("Onur")
                .lastName("Haktan")
                .email("onur@email.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Göksu")
                .lastName("Turaç")
                .email("goxu@email.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behaviour that we  are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())));
    }

    // Junit test for updatedEmployee REST API - negative scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnEmpty() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;

        Employee savedEmployee = Employee.builder()
                .firstName("Onur")
                .lastName("Haktan")
                .email("onur@email.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Göksu")
                .lastName("Turaç")
                .email("goxu@email.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behaviour that we  are going test

        // This line sends a PUT request to the specified URL. It ensures that the body of this request contains the updatedEmployee object in JSON format.
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the result or output using assert statements
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // Junit test for deleteEmployee REST API
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {

        // given - precondition or setup
        // we should use "willDoNothing()" because deleteEmployee returns a void
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // when - action or behaviour that we  are going test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}",employeeId));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print());

    }
}
