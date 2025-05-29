package guru.springframework.spring6coldservice.listeners;

import guru.springframework.spring6coldservice.config.KafkaConfig;
import guru.springframework.spring6coldservice.services.DrinkRequestProcessor;
import guru.springframework.spring6restmvcapi.events.DrinkPreparedEvent;
import guru.springframework.spring6restmvcapi.events.DrinkRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DrinkRequestListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final DrinkRequestProcessor drinkRequestProcessor;

    @KafkaListener(groupId = "ColdListener", topics = KafkaConfig.DRINK_REQUEST_COLD_TOPIC)
    public void listenDrinkRequest(DrinkRequestEvent drinkRequestEvent) {
        log.debug("Listening DrinkRequestEvent in DrinkRequestListener");

        drinkRequestProcessor.processDrinkRequest(drinkRequestEvent);

        kafkaTemplate.send(KafkaConfig.DRINK_PREPARED_TOPIC, DrinkPreparedEvent.builder()
                .beerOrderLine(drinkRequestEvent.getBeerOrderLineDTO())
                .build());
    }

}
