package edu.ucsal.fiadopay.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public interface WebhookSink {
    String value();  // canal ou tipo de webhook
}
