package com.onurhaktan.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onurhaktan.springboot.model.Employee;
import com.onurhaktan.springboot.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }

    // Integration test for createEmployee controller
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Onur")
                .lastName("Haktan")
                .email("onur@email.com")
                .build();

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

    // Integration test for getAllEmployees controller
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

        employeeRepository.saveAll(listOfEmployees);

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
        Employee employee = Employee.builder()
                .firstName("Onur")
                .lastName("Haktan")
                .email("onur@email.com")
                .build();
        employeeRepository.save(employee);

        // when - action or behaviour that we  are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

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
        employeeRepository.save(employee);

        // when - action or behaviour that we  are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the result or output using assert statements
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // Integration test for updatedEmployee REST API - positive scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {

        // given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Onur")
                .lastName("Haktan")
                .email("onur@email.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Göksu")
                .lastName("Turaç")
                .email("goxu@email.com")
                .build();

        // when - action or behaviour that we  are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())));
    }

    // Integration test for updatedEmployee REST API - negative scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnEmpty() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Onur")
                .lastName("Haktan")
                .email("onur@email.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Göksu")
                .lastName("Turaç")
                .email("goxu@email.com")
                .build();

        // when - action or behaviour that we  are going test

        // This line sends a PUT request to the specified URL. It ensures that the body of this request contains the updatedEmployee object in JSON format.
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the result or output using assert statements
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
}
