package payroll.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import payroll.domain.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {
}
