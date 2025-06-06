package guru.springframework.spring6coldservice.listeners;

import guru.springframework.spring6coldservice.config.KafkaConfig;
import guru.springframework.spring6restmvcapi.events.DrinkRequestEvent;
import guru.springframework.spring6restmvcapi.model.BeerDTO;
import guru.springframework.spring6restmvcapi.model.BeerOrderLineDTO;
import guru.springframework.spring6restmvcapi.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EmbeddedKafka(controlledShutdown = true, topics = {KafkaConfig.DRINK_REQUEST_COLD_TOPIC, KafkaConfig.DRINK_PREPARED_TOPIC}, partitions = 1)
class DrinkRequestListenerTest {

    @Autowired
    DrinkRequestListener drinkRequestListener;
    @Autowired
    private DrinkPreparedListener drinkPreparedListener;

    @Test
    void listenDrinkRequest() {

        drinkRequestListener.listenDrinkRequest(DrinkRequestEvent.builder()
                .beerOrderLineDTO(createBeerOrderLineDTO())
                .build());

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertEquals(1, drinkPreparedListener.coldMessageCounter.get()));

    }

    public BeerOrderLineDTO createBeerOrderLineDTO() {

        return BeerOrderLineDTO.builder()
                .beer(BeerDTO.builder()
                        .id(UUID.randomUUID())
                        .beerStyle(BeerStyle.IPA)
                        .beerName("Test Beer")
                        .build())
                .build();
    }

}
