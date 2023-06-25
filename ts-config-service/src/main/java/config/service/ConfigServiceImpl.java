package config.service;

import config.entity.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import config.repository.ConfigRepository;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author fdse
 */
@Service
public class ConfigServiceImpl implements ConfigService { 
    private static final Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);


    @Autowired
    ConfigRepository repository;

    String config0 = "Config ";

    @Override
    public Response create(Config info, HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","create",info.toString(), headers.toString());
        if (repository.findByName(info.getName()) != null) {
        logger.info("the Config is: {}", repository.findByName(info.getName()).toString());
            String result = config0 + info.getName() + " already exists.";
            logger.warn("[create][{} already exists][config info: {}]", config0, info.getName());
            return new Response<>(0, result, null);
        } else {
            Config config = new Config(info.getName(), info.getValue(), info.getDescription());
            repository.save(config);
            return new Response<>(1, "Create success", config);
        }
    }

    @Override
    public Response update(Config info, HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","update",info.toString(), headers.toString());
        if (repository.findByName(info.getName()) == null) {
        logger.info("the Config is: {}", repository.findByName(info.getName()).toString());
            String result = config0 + info.getName() + " doesn't exist.";
            logger.warn(result);
            return new Response<>(0, result, null);
        } else {
            Config config = new Config(info.getName(), info.getValue(), info.getDescription());
            repository.save(config);
            return new Response<>(1, "Update success", config);
        }
    }


    @Override
    public Response query(String name, HttpHeaders headers) {
        logger.info("[function name:{}][name:{}, headers:{}]","query",name, headers.toString());
        Config config = repository.findByName(name);
      logger.info("the config is: {}", config.toString());
      
        if (config == null) {
            logger.warn("[query][Config does not exist][name: {}, message: {}]", name, "No content");
            return new Response<>(0, "No content", null);
        } else {
            return new Response<>(1, "Success", config);
        }
    }

    @Override
    @Transactional
    public Response delete(String name, HttpHeaders headers) {
        logger.info("[function name:{}][name:{}, headers:{}]","delete",name, headers.toString());
        Config config = repository.findByName(name);
      logger.info("the config is: {}", config.toString());
      
        if (config == null) {
            String result = config0 + name + " doesn't exist.";
            logger.warn("[delete][config doesn't exist][config name: {}]", name);
            return new Response<>(0, result, null);
        } else {
            repository.deleteByName(name);
            return new Response<>(1, "Delete success", config);
        }
    }

    @Override
    public Response queryAll(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","queryAll",headers.toString());
        List<Config> configList = repository.findAll();
      logger.info("the configList is: {}", configList.toString());
      

        if (configList != null && !configList.isEmpty()) {
            return new Response<>(1, "Find all  config success", configList);
        } else {
            logger.warn("[queryAll][Query config, No content]");
            return new Response<>(0, "No content", null);
        }
    }
}
