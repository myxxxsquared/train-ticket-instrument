package travel.async;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import edu.fudan.common.util.Response;
import travel.entity.Travel;
import travel.service.TravelServiceImpl;

import java.util.concurrent.Future;
import java.util.List;

@Component  
public class AsyncTask {  
    private static final Logger logger = LogManager.getLogger(TravelServiceImpl.class);

    @Autowired
	private RestTemplate restTemplate;

    public static int size;

    @Async("asyncTaskExecutor")
    public Future<ResponseEntity<Response>> query(List<Travel> infos, HttpHeaders headers) {
        size += 1;
        logger.info("thread Size: {}", size);

        HttpEntity requestEntity = new HttpEntity(infos, null);
        String basic_service_url = getServiceUrl("ts-basic-service");
        ResponseEntity<Response> re = restTemplate.exchange(
                basic_service_url + "/api/v1/basicservice/basic/travels",
                HttpMethod.POST,
                requestEntity,
                Response.class);

        logger.info("[status code:{}, url:{}, type:{}, headers:{}]", re.getStatusCode(),
            basic_service_url + "/api/v1/basicservice/basic/travels","POST", headers);

        size -= 1;
        return new AsyncResult<>(re);
    }

    //Original, unmodified code from TravelServiceImpl
    private String getServiceUrl(String serviceName) {
        logger.info("[function name:{}][serviceName:{}]","getServiceUrl",serviceName);
        return "http://" + serviceName;
    }
}  
