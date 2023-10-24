package com.onurhaktan.springboot.service;

import com.onurhaktan.springboot.exception.ResourceNotFoundException;
import com.onurhaktan.springboot.model.Employee;
import com.onurhaktan.springboot.repository.EmployeeRepository;
import com.onurhaktan.springboot.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
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
        Assertions.assertThat(savedEmployee).isNotNull();

    }

    // JUnit test for saveEmployee method which throws exception
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){

        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        // when - action or behaviour that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeServiceImpl.saveEmployee(employee);
        });

        // then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method ")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeesList(){

        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Akın")
                .lastName("Haktan")
                .email("akın@email.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        // when - action or behaviour that we are going test
        List<Employee> employeeList = employeeServiceImpl.getAllEmployees();

        // then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
    }

    // JUnit test for getAllEmployees method negative scenario
    @DisplayName("JUnit test for getAllEmployees method negative scenario")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){

        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Akın")
                .lastName("Haktan")
                .email("akın@email.com")
                .build();

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or behaviour that we are going test
        List<Employee> employeeList = employeeServiceImpl.getAllEmployees();

        // then - verify the output
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

    // JUnit test for getEmployeeById method
    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){

        // given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when - action or behaviour that we are going test
        Employee savedEmployee = employeeServiceImpl.getEmployeeById(employee.getId()).get();

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for updatedEmployee method
    @DisplayName("JUnit test for updatedEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdatedEmployee_thenReturnUpdatedEmployee(){

        // given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstName("Göksu");
        employee.setEmail("goksu@email");

        // when - action or behaviour that we are going test
        Employee updatedEmployee = employeeServiceImpl.updateEmployee(employee);

        // then - verify the output
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Göksu");
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("goksu@email");
    }

    // JUnit test for deleteEmployee method
    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){

        // given - precondition or setup
        long employeeId = 1L;
        // willDoNothing() ensures that the specified method does not perform any operation.
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when - action or behaviour that we are going test
        employeeServiceImpl.deleteEmployee(employeeId);

        // then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);

    }
}
