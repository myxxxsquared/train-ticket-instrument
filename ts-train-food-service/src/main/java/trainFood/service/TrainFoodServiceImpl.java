package trainFood.service;

import edu.fudan.common.util.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import trainFood.entity.*;
import trainFood.repository.TrainFoodRepository;

import java.util.List;

@Service
public class TrainFoodServiceImpl implements TrainFoodService{ 
    private static final Logger logger = LoggerFactory.getLogger(TrainFoodServiceImpl.class);


    @Autowired
    TrainFoodRepository trainFoodRepository;

    String success = "Success";
    String noContent = "No content";

    @Override
    public TrainFood createTrainFood(TrainFood tf, HttpHeaders headers) {
        logger.info("[function name:{}][tf:{}, headers:{}]","createTrainFood",tf.toString(), headers.toString());
        TrainFood tfTemp = trainFoodRepository.findByTripId(tf.getTripId());
        if (tfTemp != null) {
            if(tfTemp.getFoodList().equals(tf.getFoodList())){
                TrainFoodServiceImpl.logger.error("[Init TrainFood] Already Exists TripId: {}", tf.getTripId());
            }else{
                tfTemp.setFoodList(tf.getFoodList());
                
      logger.info("the tfTemp is: {}", tfTemp.toString());
      trainFoodRepository.save(tfTemp);
            }
        } else {
            trainFoodRepository.save(tf);
        }
        return tf;
    }

    @Override
    public Response listTrainFood(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","listTrainFood",headers.toString());
        List<TrainFood> trainFoodList = trainFoodRepository.findAll();
      logger.info("the trainFoodList is: {}", trainFoodList.toString());
      
        if (trainFoodList != null && !trainFoodList.isEmpty()) {
            return new Response<>(1, success, trainFoodList);
        } else {
            TrainFoodServiceImpl.logger.error("List train food error: {}", noContent);
            return new Response<>(0, noContent, null);
        }
    }

    @Override
    public Response listTrainFoodByTripId(String tripId, HttpHeaders headers) {
        logger.info("[function name:{}][tripId:{}, headers:{}]","listTrainFoodByTripId",tripId, headers.toString());
        TrainFood tf = trainFoodRepository.findByTripId(tripId);
      logger.info("the tf is: {}", tf.toString());
      
        if(tf == null){
            return new Response<>(0, noContent, null);
        }else{
            return new Response<>(1, success, tf.getFoodList());
        }
    }
}
