package food.service;

import edu.fudan.common.util.Response;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import food.entity.StationFoodStore;
import food.repository.StationFoodRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StationFoodServiceImpl implements StationFoodService { 
    private static final Logger logger = LoggerFactory.getLogger(StationFoodServiceImpl.class);





    @Autowired
    StationFoodRepository stationFoodRepository;

//    @Autowired
//    TrainFoodRepository trainFoodRepository;

    String success = "Success";
    String noContent = "No content";

    @Override
    public Response createFoodStore(StationFoodStore fs, HttpHeaders headers) {
        logger.info("[function name:{}][fs:{}, headers:{}]","createFoodStore",(fs != null ? fs.toString(): null), (headers != null ? headers.toString(): null));
        StationFoodStore fsTemp = stationFoodRepository.findById(fs.getId()).orElse(null);
      logger.info("the fsTemp is: {}", (fsTemp != null ? fsTemp : null));
      
      
      
      
        if (fsTemp != null) {
            StationFoodServiceImpl.logger.error("[Init FoodStore] Already Exists Id: {}", fs.getId());
            return new Response<>(0, "Already Exists Id", null);
        } else {
            try{
                stationFoodRepository.save(fs);
                return new Response<>(1, "Save Success", fs);
            }catch(Exception e){
                return new Response<>(0, "Save failed", e.getMessage());
            }
        }
    }


    @Override
    public Response listFoodStores(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","listFoodStores",(headers != null ? headers.toString(): null));
        List<StationFoodStore> stationFoodStores = stationFoodRepository.findAll();
      logger.info("the stationFoodStores is: {}", (stationFoodStores != null ? stationFoodStores : null));
      
      
      
        if (stationFoodStores != null && !stationFoodStores.isEmpty()) {
            return new Response<>(1, success, stationFoodStores);
        } else {
            StationFoodServiceImpl.logger.error("List food stores error: {}", "Food store is empty");
            return new Response<>(0, "Food store is empty", null);
        }
    }

    @Override
    public Response listFoodStoresByStationName(String stationName, HttpHeaders headers) {
        logger.info("[function name:{}][stationName:{}, headers:{}]","listFoodStoresByStationName",stationName, (headers != null ? headers.toString(): null));
        List<StationFoodStore> stationFoodStoreList = stationFoodRepository.findByStationName(stationName);
      logger.info("the stationFoodStoreList is: {}", (stationFoodStoreList != null ? stationFoodStoreList : null));
      
      
      
        if (stationFoodStoreList != null && !stationFoodStoreList.isEmpty()) {
            return new Response<>(1, success, stationFoodStoreList);
        } else {
            StationFoodServiceImpl.logger.error("List food stores by station id error: {}, stationName: {}", "Food store is empty", stationName);
            return new Response<>(0, "Food store is empty", null);
        }
    }


    @Override
    public Response getFoodStoresByStationNames(List<String> stationNames) {
        logger.info("[function name:{}][stationNames:{}]","getFoodStoresByStationNames",(stationNames != null ? stationNames.toString(): null));
        List<StationFoodStore> stationFoodStoreList = stationFoodRepository.findByStationNameIn(stationNames);
      logger.info("the stationFoodStoreList is: {}", (stationFoodStoreList != null ? stationFoodStoreList : null));
      
      
      
        if (stationFoodStoreList != null) {
            return new Response<>(1, success, stationFoodStoreList);
        } else {
            StationFoodServiceImpl.logger.error("List food stores by station ids error: {}, stationName list: {}", "Food store is empty", stationNames);
            return new Response<>(0, noContent, null);
        }
    }

    @Override
    public Response getStaionFoodStoreById(String id) {
        logger.info("[function name:{}][id:{}]","getStaionFoodStoreById",id);
        StationFoodStore stationFoodStore = stationFoodRepository.findById(id).orElse(null);
      logger.info("the stationFoodStore is: {}", (stationFoodStore != null ? stationFoodStore : null));
      
      
      
        if (stationFoodStore == null) {
            logger.error("no such staionFoodStoreId: {}", id);
            return new Response<>(0, noContent, null);
        } else {
            return new Response<>(1, success, stationFoodStore);
        }
    }
}
