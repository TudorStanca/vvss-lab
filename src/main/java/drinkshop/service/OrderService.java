package drinkshop.service;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;

import java.util.List;

public class OrderService {

    private final Repository<Integer, Order> orderRepo;
    private final Repository<Integer, Product> productRepo;
    private final Validator<Order> validator;

    public OrderService(Repository<Integer, Order> orderRepo,
                        Repository<Integer, Product> productRepo,
                        Validator<Order> validator) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.validator = validator;
    }

    public void addOrder(Order o) {
        validator.validate(o);
        orderRepo.save(o);
    }

    public void updateOrder(Order o) {
        orderRepo.update(o);
    }

    public void deleteOrder(int id) {
        orderRepo.delete(id);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order findById(int id) {
        return orderRepo.findOne(id);
    }

    public double computeTotal(Order o) {
        return o.getItems().stream()
                .mapToDouble(OrderItem::getTotal)
                .sum();
    }

    public void addItem(Order o, OrderItem item) {
        o.getItems().add(item);
        orderRepo.update(o);
    }

    public void removeItem(Order o, OrderItem item) {
        o.getItems().remove(item);
        orderRepo.update(o);
    }
}
