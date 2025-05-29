package guru.springframework.spring6coldservice.services;

import guru.springframework.spring6restmvcapi.events.DrinkRequestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DrinkRequestProcessorImpl implements DrinkRequestProcessor {
    @Override
    public void processDrinkRequest(DrinkRequestEvent drinkRequestEvent) {

        log.debug("Processing Drink Request Event in Drink Request Processor Impl");

        //Simulating processing
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
