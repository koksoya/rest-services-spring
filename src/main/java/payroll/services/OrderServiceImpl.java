package payroll.services;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import payroll.controllers.OrderController;
import payroll.domain.Order;
import payroll.domain.Status;
import payroll.exceptions.OrderNotFoundException;
import payroll.modelAssemblers.OrderModelAssembler;
import payroll.repositories.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderModelAssembler assembler;

    public OrderServiceImpl(OrderRepository orderRepository, OrderModelAssembler assembler) {
        this.orderRepository = orderRepository;
        this.assembler = assembler;
    }

    @Override
    public CollectionModel<EntityModel<Order>> getAll() {
        List<EntityModel<Order>> orders = orderRepository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    @Override
    public EntityModel<Order> getOne(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return assembler.toModel(order);
    }

    @Override
    public ResponseEntity<EntityModel<Order>> create(Order order) {
        order.setStatus(Status.IN_PROGRESS);
        Order newOrder = orderRepository.save(order);

        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
                .body(assembler.toModel(newOrder));
    }

    @Override
    public ResponseEntity<RepresentationModel> cancel(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
        }
        // VndError, a hypermedia-supporting error container
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + order.getStatus() + " status"));

    }

    @Override
    public ResponseEntity<RepresentationModel> complete(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status"));


    }
}
