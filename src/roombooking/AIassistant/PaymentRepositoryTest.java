package roombooking.AIassistant;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.*;

import roombooking.enums.*;
import roombooking.model.Payment;
import roombooking.repository.PaymentRepository;
import roombooking.strategy.CreditCardProcessorStrategy;

public class PaymentRepositoryTest {


    private PaymentRepository repository;

    private Payment payment;


    @BeforeEach
    public void setup(){

        repository = new PaymentRepository();


        payment =
            new Payment(
                "TEST_PAYMENT_999",
                "B999",
                100,
                LocalDateTime.now(),
                PaymentType.DEPOSIT,
                PaymentMethod.CREDITCARD,
                new CreditCardProcessorStrategy()
            );


        repository.savePayment(payment);
    }



    @AfterEach
    public void cleanup(){

        repository.deletePayment(
                "TEST_PAYMENT_999"
        );
    }



    @Test
    public void testFindPaymentById(){


        Payment result =
                repository.findPaymentById(
                        "TEST_PAYMENT_999"
                );


        assertNotNull(result);

        assertEquals(
                100,
                result.getAmount()
        );

    }



    @Test
    public void testFindPaymentsByBookingId(){


        assertEquals(
            1,
            repository.findPaymentsByBookingId("B999")
            .size()
        );

    }



    @Test
    public void testGeneratePaymentId(){

        String id =
                repository.generatePaymentId();


        assertNotNull(id);

        assertTrue(
            id.startsWith("P")
        );

    }



    @Test
    public void testDeletePayment(){


        repository.deletePayment(
                "TEST_PAYMENT_999"
        );


        assertNull(
            repository.findPaymentById(
                "TEST_PAYMENT_999"
            )
        );

    }
}