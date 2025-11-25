package com.infosysSpringboard.EcoBazarX.repo;

import com.infosysSpringboard.EcoBazarX.model.Order;
import com.infosysSpringboard.EcoBazarX.model.OrderStatus;
import com.infosysSpringboard.EcoBazarX.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(Users user);

    List<Order> findByUserUsername(String username);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.items i WHERE i.product.postedBy = :seller")
    List<Order> findOrdersBySeller(@Param("seller") Users seller);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o")
    double sumTotalPrice();

    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);


    @Query(
            value = """
               WITH order_months AS (
                    SELECT date_trunc('month', created_at) AS bucket,
                            COALESCE(SUM(total_price), 0) AS revenue,
                            COUNT(*) AS orders
                    FROM orders
                    GROUP BY bucket
                ),
                user_months AS (
                    SELECT date_trunc('month', created_at) AS bucket,
                            COUNT(*) AS new_users
                    FROM users
                    GROUP BY bucket
                )
                SELECT TO_CHAR(om.bucket, 'YYYY-MM')     AS month,
                        om.revenue,
                        om.orders,
                        COALESCE(um.new_users, 0)         AS newUsers
                FROM order_months om
                LEFT JOIN user_months um ON um.bucket = om.bucket
                ORDER BY om.bucket;
            """,
            nativeQuery = true
    )
    List<Object[]> getMonthlyTrends();

    @Query("""
    SELECT COUNT(DISTINCT oi.order.user.id)
    FROM OrderItem oi
    WHERE oi.order.createdAt >= :cutoff
""")
    long countActiveCustomersLast30Days(LocalDateTime cutoff);

    @Query("""
    SELECT COUNT(DISTINCT oi.product.postedBy.id)
    FROM OrderItem oi
    WHERE oi.order.createdAt >= :cutoff
""")
    long countActiveSellersLast30Days(LocalDateTime cutoff);


    long countByStatus(OrderStatus status);





//    @Query("SELECT SUM(o.totalPrice) FROM Order o")
//    Double sumTotalPrice();

}
