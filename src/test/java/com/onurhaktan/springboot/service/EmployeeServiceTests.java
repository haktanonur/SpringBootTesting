package com.onurhaktan.springboot.service;

import com.onurhaktan.springboot.model.Employee;
import com.onurhaktan.springboot.repository.EmployeeRepository;
import com.onurhaktan.springboot.service.impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    // We use @Mock annotation mock the object
    private EmployeeRepository employeeRepository;

    @InjectMocks
    // @InjectMocks creates the mock object of the class and injects the mocks that are marked with the annotation @Mock into it
    private EmployeeServiceImpl employeeServiceImpl;

    private Employee employee;

    @BeforeEach
    public void setup(){
        // The Mockito.mock() method creates a mock object of the EmployeeRepository class. This can be used in tests instead of performing actual database operations.
        //employeeRepository = Mockito.mock(EmployeeRepository.class);

        // An instance of the EmployeeServiceImpl class is created. This class executes business logic using the EmployeeRepository. This class will be used in the tests.
        // The crucial point here is that instead of using the actual EmployeeRepository, a mock one is used. This allows the tests to run without actual database access.
        //employeeService = new EmployeeServiceImpl(employeeRepository);

        employee = Employee.builder()
                .id(1L)
                .firstName("Onur")
                .lastName("Haktan")
                .email("onur@email.com")
                .build();
    }

    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){

        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or behaviour that we are going test
        Employee savedEmployee = employeeServiceImpl.saveEmployee(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();

    }
}
