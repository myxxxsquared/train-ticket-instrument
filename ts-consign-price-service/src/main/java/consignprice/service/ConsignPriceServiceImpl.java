package consignprice.service;

import consignprice.entity.ConsignPrice;














import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import consignprice.repository.ConsignPriceConfigRepository;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 * @author fdse
 */
@Service
public class ConsignPriceServiceImpl implements ConsignPriceService { 
    private static final Logger logger = LogManager.getLogger(ConsignPriceServiceImpl.class);
















    @Autowired
    private ConsignPriceConfigRepository repository;

    String success = "Success";

    @Override
    public Response getPriceByWeightAndRegion(double weight, boolean isWithinRegion, HttpHeaders headers) {
        logger.info("[function name:{}][weight:{}, isWithinRegion:{}, headers:{}]","getPriceByWeightAndRegion",weight, isWithinRegion, (headers != null ? headers.toString(): null));
        ConsignPrice priceConfig = repository.findByIndex(0);
      logger.info("[priceConfig:{},headers:{}]", (priceConfig != null ? priceConfig : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        double price = 0;
        double initialPrice = priceConfig.getInitialPrice();
        if (weight <= priceConfig.getInitialWeight()) {
            price = initialPrice;
        } else {
            double extraWeight = weight - priceConfig.getInitialWeight();
            if (isWithinRegion) {
                price = initialPrice + extraWeight * priceConfig.getWithinPrice();
            }else {
                price = initialPrice + extraWeight * priceConfig.getBeyondPrice();
            }
        }
        return new Response<>(1, success, price);
    }

    @Override
    public Response queryPriceInformation(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","queryPriceInformation",(headers != null ? headers.toString(): null));
        StringBuilder sb = new StringBuilder();
        ConsignPrice price = repository.findByIndex(0);
      logger.info("[price:{},headers:{}]", (price != null ? price : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        sb.append("The price of weight within ");
        sb.append(price.getInitialWeight());
        sb.append(" is ");
        sb.append(price.getInitialPrice());
        sb.append(". The price of extra weight within the region is ");
        sb.append(price.getWithinPrice());
        sb.append(" and beyond the region is ");
        sb.append(price.getBeyondPrice());
        sb.append("\n");
        return new Response<>(1, success, sb.toString());
    }

    @Override
    public Response createAndModifyPrice(ConsignPrice config, HttpHeaders headers) {
        logger.info("[function name:{}][config:{}, headers:{}]","createAndModifyPrice",(config != null ? config.toString(): null), (headers != null ? headers.toString(): null));
        //update price
        ConsignPrice originalConfig;
        if (repository.findByIndex(0) != null) {
        logger.info("[ConsignPrice:{},headers:{}]", (repository.findByIndex(0) != null ? repository.findByIndex(0) : null),headers);
            originalConfig = repository.findByIndex(0);
        } else {
            originalConfig = new ConsignPrice();
        }
        originalConfig.setId(config.getId());
        originalConfig.setIndex(0);
        originalConfig.setInitialPrice(config.getInitialPrice());
        originalConfig.setInitialWeight(config.getInitialWeight());
        originalConfig.setWithinPrice(config.getWithinPrice());
        originalConfig.setBeyondPrice(config.getBeyondPrice());
        repository.save(originalConfig);
        return new Response<>(1, success, originalConfig);
    }

    @Override
    public Response getPriceConfig(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getPriceConfig",(headers != null ? headers.toString(): null));
        return new Response<>(1, success, repository.findByIndex(0));
    }
}
