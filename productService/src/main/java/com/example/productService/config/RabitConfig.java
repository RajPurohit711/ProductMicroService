
package com.example.productService.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabitConfig {
    public static final String ROUTING_PRODUCT_SEARCH ="routing.ProductSearch";


    public static final String ROUTING_ORDER_PRODUCT ="routing.OrderProduct";




    @Bean
    Queue queueProductSearch(){
        return new Queue("queue.ProductSearch",false);
    }

    @Bean
    DirectExchange exchange(){
        return new DirectExchange("direct.exchange");
    }

    @Bean
    Binding bindingOrderEmail(Queue queueProductSearch, DirectExchange exchange){
        return BindingBuilder.bind(queueProductSearch).to(exchange).with(ROUTING_PRODUCT_SEARCH);
    }


    @Bean
    Queue queueOrderProduct(){
        return new Queue("queue.OrderProduct",false);
    }

    @Bean
    Binding bindingOrderProduct(Queue queueOrderProduct, DirectExchange exchange){
        return BindingBuilder.bind(queueOrderProduct).to(exchange).with(ROUTING_ORDER_PRODUCT);
    }


    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory factory){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}