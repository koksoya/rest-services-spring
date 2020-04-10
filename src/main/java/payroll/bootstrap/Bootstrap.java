package payroll.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import payroll.domain.Employee;
import payroll.domain.Order;
import payroll.domain.Status;
import payroll.repositories.EmployeeRepository;
import payroll.repositories.OrderRepository;

@Configuration
@Slf4j
class Bootstrap {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {

        orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
        orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));

        orderRepository.findAll().forEach(order -> {
            log.info("Preloaded " + order);
        });

        return args -> {
            log.info("Preloading " + employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar")));
            log.info("Preloading " + employeeRepository.save(new Employee("Frodo", "Baggins", "thief")));
        };
    }
}