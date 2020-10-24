package com.xiangxuepf.p2p.web;

import com.xiangxuepf.p2p.exterface.service.loan.BidInfoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mhw
 */
public class TestOverSell {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
//        ConfigWeb configWeb = (ConfigWeb) context.getBean("configWeb");
//        System.out.println(configWeb.getRealNameAppkey());
//        System.out.println(configWeb.getRealNameUrl());
        BidInfoService bidInfoService = (BidInfoService) context.getBean("bidInfoServiceImpl");
//        创建一个固定的线程池

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("uid","");
        paramMap.put("loanId","");
        paramMap.put("bidMoney","");

        for(int i = 0; i < 1000; i++){
            executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {

                    return bidInfoService.invest(paramMap);
                }
            });
        }

        executorService.shutdown();

    }
}
