package trainFood.service;

import edu.fudan.common.util.Response;


















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import trainFood.entity.*;
import trainFood.repository.TrainFoodRepository;

import java.util.List;

@Service
public class TrainFoodServiceImpl implements TrainFoodService{ 
    private static final Logger logger = LogManager.getLogger(TrainFoodServiceImpl.class);










    @Autowired
    TrainFoodRepository trainFoodRepository;

    String success = "Success";
    String noContent = "No content";

    @Override
    public TrainFood createTrainFood(TrainFood tf, HttpHeaders headers) {
        TrainFood tfTemp = trainFoodRepository.findByTripId(tf.getTripId());
        if (tfTemp != null) {
            if(tfTemp.getFoodList().equals(tf.getFoodList())){
                TrainFoodServiceImpl.logger.error("[Init TrainFood] Already Exists TripId: {}", tf.getTripId());
            }else{
                tfTemp.setFoodList(tf.getFoodList());
      trainFoodRepository.save(tfTemp);
            }
        } else {
            trainFoodRepository.save(tf);
        }
        return tf;
    }

    @Override
    public Response listTrainFood(HttpHeaders headers) {
        List<TrainFood> trainFoodList = trainFoodRepository.findAll();
      
      
      
      
      
      
      
      
        if (trainFoodList != null && !trainFoodList.isEmpty()) {
            return new Response<>(1, success, trainFoodList);
        } else {
            TrainFoodServiceImpl.logger.error("List train food error: {}", noContent);
            return new Response<>(0, noContent, null);
        }
    }

    @Override
    public Response listTrainFoodByTripId(String tripId, HttpHeaders headers) {
        TrainFood tf = trainFoodRepository.findByTripId(tripId);
      
      
      
      
      
      
      
      
        if(tf == null){
            return new Response<>(0, noContent, null);
        }else{
            return new Response<>(1, success, tf.getFoodList());
        }
    }
}
