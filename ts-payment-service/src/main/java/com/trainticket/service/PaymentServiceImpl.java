package com.trainticket.service;

import com.trainticket.entity.Money;

















import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.trainticket.entity.Payment;
import com.trainticket.repository.AddMoneyRepository;
import com.trainticket.repository.PaymentRepository;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author  Administrator
 * @date 2017/6/23.
 */
@Service
public class PaymentServiceImpl implements PaymentService{ 
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);



















    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    AddMoneyRepository addMoneyRepository;

    @Override
    public Response pay(Payment info, HttpHeaders headers){
        logger.info("[function name:{}][info:{}, headers:{}]","pay",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));

        if(paymentRepository.findByOrderId(info.getOrderId()) == null){
            Payment payment = new Payment();
            payment.setOrderId(info.getOrderId());
            payment.setPrice(info.getPrice());
            payment.setUserId(info.getUserId());
            paymentRepository.save(payment);
            return new Response<>(1, "Pay Success", null);
        }else{
            PaymentServiceImpl.logger.warn("[pay][Pay Failed][Order not found with order id][PaymentId: {}, OrderId: {}]",info.getId(),info.getOrderId());
            return new Response<>(0, "Pay Failed, order not found with order id" +info.getOrderId(), null);
        }
    }

    @Override
    public Response addMoney(Payment info, HttpHeaders headers){
        logger.info("[function name:{}][info:{}, headers:{}]","addMoney",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        Money addMoney = new Money();
        addMoney.setUserId(info.getUserId());
        addMoney.setMoney(info.getPrice());
        addMoneyRepository.save(addMoney);
        return new Response<>(1,"Add Money Success", addMoney);
    }

    @Override
    public Response query(HttpHeaders headers){
        logger.info("[function name:{}][headers:{}]","query",(headers != null ? headers.toString(): null));
        List<Payment> payments = paymentRepository.findAll();
        if(payments!= null && !payments.isEmpty()){
            return new Response<>(1,"Query Success",  payments);
        }else {
            PaymentServiceImpl.logger.warn("[query][Find all payment warn][{}]","No content");
            return new Response<>(0, "No Content", null);
        }
    }

    @Override
    public void initPayment(Payment payment, HttpHeaders headers){
        logger.info("[function name:{}][payment:{}, headers:{}]","initPayment",(payment != null ? payment.toString(): null), (headers != null ? headers.toString(): null));
        Optional<Payment> paymentTemp = paymentRepository.findById(payment.getId());
        if(!paymentTemp.isPresent()){
            paymentRepository.save(payment);
            PaymentServiceImpl.logger.error("[initPayment][Init payment error][Payment not found][PaymentId: {}]",payment.getId());
        }else{
        }
    }
}
