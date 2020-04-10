package payroll.services;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import payroll.domain.Employee;


public interface EmployeeService {

    CollectionModel<EntityModel<Employee>> getAll();

    EntityModel<Employee> getOne(Long id);

    ResponseEntity<?> create(Employee newEmployee);

    ResponseEntity<?> replace(Employee newEmployee, Long id);

    ResponseEntity<?> delete(Long id);








    }
