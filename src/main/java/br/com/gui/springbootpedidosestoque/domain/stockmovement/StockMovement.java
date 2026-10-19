package br.com.gui.springbootpedidosestoque.domain.stockmovement;

import br.com.gui.springbootpedidosestoque.domain.order.Order;
import br.com.gui.springbootpedidosestoque.domain.orderitem.OrderItem;
import br.com.gui.springbootpedidosestoque.domain.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Table(name = "stock_movements")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne
    private Product product;

    @ManyToOne
    private Order order;

    @ManyToOne
    private OrderItem orderItem;

    @NotNull
    @Positive
    private Long quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MovementTypeEnum type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MovementReasonEnum reason;

    private String justification;

    @CreationTimestamp
    private Instant createdAt;

}
