package consignprice.init;

import consignprice.entity.ConsignPrice;
import consignprice.service.ConsignPriceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author fdse
 */
@Component
public class InitData implements CommandLineRunner {
    @Autowired
    ConsignPriceService service;

    private static final Logger LOGGER = LogManager.getLogger(InitData.class);

    @Override
    public void run(String... strings) throws Exception {
        InitData.LOGGER.info("[InitData.run][Consign price service][Init data operation]");
        ConsignPrice config = new ConsignPrice();
        config.setId(UUID.randomUUID().toString());
        config.setIndex(0);
        config.setInitialPrice(8);
        config.setInitialWeight(1);
        config.setWithinPrice(2);
        config.setBeyondPrice(4);

        service.createAndModifyPrice(config, null);
    }
}
