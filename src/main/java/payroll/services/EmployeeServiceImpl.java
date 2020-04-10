package payroll.services;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import payroll.controllers.EmployeeController;
import payroll.domain.Employee;
import payroll.exceptions.EmployeeNotFoundException;
import payroll.modelAssemblers.EmployeeModelAssembler;
import payroll.repositories.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

    public EmployeeServiceImpl(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
    public CollectionModel<EntityModel<Employee>> getAll() {
        List<EntityModel<Employee>> employees = repository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @Override
    public EntityModel<Employee> getOne(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
    }

    @Override
    public ResponseEntity<?> create(Employee newEmployee) {
        EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Override
    public ResponseEntity<?> replace(Employee newEmployee, Long id) {
        Employee updatedEmployee = repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });

        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);

    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
