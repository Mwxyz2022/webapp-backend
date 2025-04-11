package com.example.kafka.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({KafkaProducerConfig.class, KafkaConsumerConfig.class})
public @interface EnableKafkaModule {
}
