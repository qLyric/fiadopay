package edu.ucsal.fiadopay.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public interface PaymentMethod {
    String type(); // "CARD", "PIX", etc.
}
