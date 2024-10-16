//package com.ticketPing.order.application;
//
//import com.ticketPing.order.domain.entity.Order;
//import com.ticketPing.order.domain.entity.OrderSeat;
//import com.ticketPing.order.domain.entity.OrderStatus;
//import com.ticketPing.order.domain.repository.OrderRepository;
//import com.ticketPing.order.domain.repository.OrderSeatRepository;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Component
//@AllArgsConstructor
//public class DummyDataLoader implements CommandLineRunner {
//
//    private final OrderRepository orderRepository;
//    private final OrderSeatRepository orderSeatRepository;
//
//    @Override
//    @Transactional
//    public void run(String... args) throws Exception {
//        // 더미 좌석 데이터 생성
//        OrderSeat seat1 = OrderSeat.from(1, 1, "S", 100);
//        OrderSeat seat2 = OrderSeat.from(1, 2, "A", 50);
//        OrderSeat seat3 = OrderSeat.from(2, 1, "B", 30);
//
//        // 좌석 저장
//        orderSeatRepository.save(seat1);
//        orderSeatRepository.save(seat2);
//        orderSeatRepository.save(seat3);
//
//        // 더미 주문 데이터 생성
//        Order order1 = Order.from(
//            OrderStatus.NO_RESERVATION,
//            LocalDateTime.now(),
//            UUID.fromString("3f1b7f0a-7c8a-4b2e-bb5d-986f9c7c8b45"), // 사용자 ID
//            UUID.fromString("c9b8f8f5-d6b6-4e5e-b8f8-6a3f8e5b8c9d"), // 일정 ID
//            "세종문화회관",
//            UUID.fromString("d7f3b8f8-c5b5-4f8f-b8c5-b8f8d8c5f8b6")// 회사 ID
//
//        );
//        order1.setOrderSeat(seat1); // 첫 번째 주문에 첫 번째 좌석 할당
//
//        Order order2 = Order.from(
//            OrderStatus.NO_RESERVATION,
//            LocalDateTime.now().minusDays(1),
//            UUID.fromString("4f1b7f0a-7c8a-4b2e-bb5d-986f9c7c8b46"), // 사용자 ID
//            UUID.fromString("d9b8f8f5-d6b6-4e5e-b8f8-6a3f8e5b8c9e"), // 일정 ID
//            "세종문화회관",
//            UUID.fromString("e7f3b8f8-c5b5-4f8f-b8c5-b8f8d8c5f8b7")  // 회사 ID
//        );
//        order2.setOrderSeat(seat2); // 두 번째 주문에 두 번째 좌석 할당
//
//        // 주문 저장
//        orderRepository.save(order1);
//        orderRepository.save(order2);
//    }
//}
//
