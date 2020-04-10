package payroll.services;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;

import payroll.domain.Order;

public interface OrderService {

    CollectionModel<EntityModel<Order>> getAll();

    EntityModel<Order> getOne(Long id);

    ResponseEntity<EntityModel<Order>> create(Order order);

    ResponseEntity<RepresentationModel> cancel(Long id);

    ResponseEntity<RepresentationModel> complete(Long id);




    }
