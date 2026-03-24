package drinkshop.receipt;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;

import java.util.List;

public class ReceiptGenerator {
    public static String generate(Order o, List<Product> products) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== BON FISCAL =====\n").append("Comanda #").append(o.getId()).append("\n");
        for (OrderItem i : o.getItems()) {
            Product p = i.getProduct();
            if (p == null) {
                throw new IllegalStateException("OrderItem has null product in order " + o.getId());
            }
            sb.append(p.getNume()).append(": ").append(p.getPret()).append(" x ").append(i.getQuantity()).append(" = ").append(i.getTotal()).append(" RON\n");
        }
        sb.append("---------------------\nTOTAL: ").append(o.getTotalPrice()).append(" RON\n=====================\n");
        return sb.toString();
    }
}