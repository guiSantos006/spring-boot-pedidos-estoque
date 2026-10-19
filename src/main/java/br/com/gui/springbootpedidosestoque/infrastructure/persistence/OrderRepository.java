package br.com.gui.springbootpedidosestoque.infrastructure.persistence;

import br.com.gui.springbootpedidosestoque.domain.order.Order;
import br.com.gui.springbootpedidosestoque.domain.orderitem.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
}
