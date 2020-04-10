package payroll.controllers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payroll.domain.Order;
import payroll.domain.Status;
import payroll.exceptions.OrderNotFoundException;
import payroll.services.OrderService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>> all() {
        return orderService.getAll();
    }

    @GetMapping("/orders/{id}")
    public EntityModel<Order> one(@PathVariable Long id) {
        return orderService.getOne(id);
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order) {
        return orderService.create(order);
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<RepresentationModel> cancel(@PathVariable Long id) {
        return orderService.cancel(id);
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<RepresentationModel> complete(@PathVariable Long id) {
        return orderService.complete(id);
    }
}