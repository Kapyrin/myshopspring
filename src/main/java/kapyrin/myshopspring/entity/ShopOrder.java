package kapyrin.myshopspring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"customer", "status"})
@ToString(exclude = {"customer", "status"})

@Entity
@Table(name = "shop_order")
public class ShopOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Column(name = "order_creation_date", nullable = false)
    private Date orderCreationDate;

    @Column(name = "order_close_date")
    private Date orderCloseDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="status_id")
    private OrderStatus status;
}
