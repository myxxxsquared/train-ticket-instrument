package price.service;

import edu.fudan.common.util.Response;

















import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import price.entity.PriceConfig;
import price.repository.PriceConfigRepository;

import java.util.*;


/**
 * @author fdse
 */
@Service
public class PriceServiceImpl implements PriceService { 
    private static final Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);


















    @Autowired(required=true)
    private PriceConfigRepository priceConfigRepository;

    String noThatConfig = "No that config";

    @Override
    public Response createNewPriceConfig(PriceConfig createAndModifyPriceConfig, HttpHeaders headers) {
        logger.info("[function name:{}][createAndModifyPriceConfig:{}, headers:{}]","createNewPriceConfig",(createAndModifyPriceConfig != null ? createAndModifyPriceConfig.toString(): null), (headers != null ? headers.toString(): null));
        PriceConfig priceConfig = null;
        // create
        if (createAndModifyPriceConfig.getId() == null || createAndModifyPriceConfig.getId().toString().length() < 10) {
            priceConfig = new PriceConfig();
            priceConfig.setId(UUID.randomUUID().toString());
            priceConfig.setBasicPriceRate(createAndModifyPriceConfig.getBasicPriceRate());
            priceConfig.setFirstClassPriceRate(createAndModifyPriceConfig.getFirstClassPriceRate());
            priceConfig.setRouteId(createAndModifyPriceConfig.getRouteId());
            priceConfig.setTrainType(createAndModifyPriceConfig.getTrainType());
            priceConfigRepository.save(priceConfig);
        } else {
            // modify
            Optional<PriceConfig> op = priceConfigRepository.findById(createAndModifyPriceConfig.getId());
      logger.info("[op:{},headers:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
            if (!op.isPresent()) {
                priceConfig = new PriceConfig();
                priceConfig.setId(createAndModifyPriceConfig.getId());
            }else{
                priceConfig = op.get();
            }
            priceConfig.setBasicPriceRate(createAndModifyPriceConfig.getBasicPriceRate());
            priceConfig.setFirstClassPriceRate(createAndModifyPriceConfig.getFirstClassPriceRate());
            priceConfig.setRouteId(createAndModifyPriceConfig.getRouteId());
            priceConfig.setTrainType(createAndModifyPriceConfig.getTrainType());
            priceConfigRepository.save(priceConfig);
        }
        return new Response<>(1, "Create success", priceConfig);
    }

    @Override
    public PriceConfig findById(String id, HttpHeaders headers) {
        logger.info("[function name:{}][id:{}, headers:{}]","findById",id, (headers != null ? headers.toString(): null));
        Optional<PriceConfig> op = priceConfigRepository.findById(UUID.fromString(id).toString());
      logger.info("[op:{},headers:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if(op.isPresent()){
            return op.get();
        }
        return null;
    }

    @Override
    public Response findByRouteIdAndTrainType(String routeId, String trainType, HttpHeaders headers) {
        logger.info("[function name:{}][routeId:{}, trainType:{}, headers:{}]","findByRouteIdAndTrainType",routeId, trainType, (headers != null ? headers.toString(): null));
        PriceConfig priceConfig = priceConfigRepository.findByRouteIdAndTrainType(routeId, trainType);
      logger.info("[priceConfig:{},headers:{}]", (priceConfig != null ? priceConfig : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      

        if (priceConfig == null) {
            PriceServiceImpl.logger.warn("[findByRouteIdAndTrainType][Find by route and train type warn][PricrConfig not found][RouteId: {}, TrainType: {}]",routeId,trainType);
            return new Response<>(0, noThatConfig, null);
        } else {
            return new Response<>(1, "Success", priceConfig);
        }
    }

    @Override
    public Response findByRouteIdsAndTrainTypes(List<String> ridsAndTts, HttpHeaders headers){
        logger.info("[function name:{}][ridsAndTts:{}, headers:{}]","findByRouteIdsAndTrainTypes",(ridsAndTts != null ? ridsAndTts.toString(): null), (headers != null ? headers.toString(): null));
        List<String> routeIds = new ArrayList<>();
        List<String> trainTypes = new ArrayList<>();
        for(String rts: ridsAndTts){
            List<String> r_t  = Arrays.asList(rts.split(":"));
            routeIds.add(r_t.get(0));
            trainTypes.add(r_t.get(1));
        }
        List<PriceConfig> pcs = priceConfigRepository.findByRouteIdsAndTrainTypes(routeIds, trainTypes);
      logger.info("[pcs:{},headers:{}]", (pcs != null ? pcs : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        Map<String, PriceConfig> pcMap = new HashMap<>();
        for(PriceConfig pc: pcs){
            String key = pc.getRouteId() + ":" + pc.getTrainType();
            if(ridsAndTts.contains(key)){
                pcMap.put(key, pc);
            }
        }
        if (pcMap == null) {
            PriceServiceImpl.logger.warn("[findByRouteIdsAndTrainTypes][Find by routes and train types warn][PricrConfig not found][RouteIds: {}, TrainTypes: {}]",routeIds,trainTypes);
            return new Response<>(0, noThatConfig, null);
        } else {
            return new Response<>(1, "Success", pcMap);
        }
    }


    @Override
    public Response findAllPriceConfig(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","findAllPriceConfig",(headers != null ? headers.toString(): null));
        List<PriceConfig> list = priceConfigRepository.findAll();
      logger.info("[list:{},headers:{}]", (list != null ? list : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (list == null) {
            list = new ArrayList<>();
        }

        if (!list.isEmpty()) {
            PriceServiceImpl.logger.warn("[findAllPriceConfig][Find all price config warn][{}]","No Content");
            return new Response<>(1, "Success", list);
        } else {
            return new Response<>(0, "No price config", null);
        }

    }

    @Override
    public Response deletePriceConfig(String pcId, HttpHeaders headers) {
        logger.info("[function name:{}][pcId:{}, headers:{}]","deletePriceConfig",pcId, (headers != null ? headers.toString(): null));
        Optional<PriceConfig> op = priceConfigRepository.findById(pcId);
      logger.info("[op:{},headers:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (!op.isPresent()) {
            PriceServiceImpl.logger.error("[deletePriceConfig][Delete price config error][Price config not found][PriceConfigId: {}]",pcId);
            return new Response<>(0, noThatConfig, null);
        } else {
            PriceConfig pc = op.get();
            priceConfigRepository.delete(pc);
            return new Response<>(1, "Delete success", pc);
        }
    }

    @Override
    public Response updatePriceConfig(PriceConfig c, HttpHeaders headers) {
        logger.info("[function name:{}][c:{}, headers:{}]","updatePriceConfig",(c != null ? c.toString(): null), (headers != null ? headers.toString(): null));
        Optional<PriceConfig> op = priceConfigRepository.findById(c.getId());
      logger.info("[op:{},headers:{}]", (op != null ? op : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (!op.isPresent()) {
            PriceServiceImpl.logger.error("[updatePriceConfig][Update price config error][Price config not found][PriceConfigId: {}]",c.getId());
            return new Response<>(0, noThatConfig, null);
        } else {
            PriceConfig priceConfig = op.get();
            priceConfig.setId(c.getId());
            priceConfig.setBasicPriceRate(c.getBasicPriceRate());
            priceConfig.setFirstClassPriceRate(c.getFirstClassPriceRate());
            priceConfig.setRouteId(c.getRouteId());
            priceConfig.setTrainType(c.getTrainType());
            priceConfigRepository.save(priceConfig);
            return new Response<>(1, "Update success", priceConfig);
        }
    }
}
