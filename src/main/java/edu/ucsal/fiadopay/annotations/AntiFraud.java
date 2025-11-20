package edu.ucsal.fiadopay.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public interface AntiFraud {
    String name();
    double threshold(); // valor a partir do qual aplicar regra
}
