package com.xiangxuepf.p2p.dataservice.test;

import com.xiangxuepf.p2p.exterface.service.loan.BidInfoService;
import com.xiangxuepf.p2p.exterface.vo.ResultObjectVO;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mhw
 */
public class TestOversell {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
//        ConfigWeb configWeb = (ConfigWeb) context.getBean("configWeb");
//        System.out.println(configWeb.getRealNameAppkey());
//        System.out.println(configWeb.getRealNameUrl());
        BidInfoService bidInfoService = (BidInfoService) context.getBean("bidInfoServiceImpl");
//        创建一个固定的线程池

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("uid",29);
        paramMap.put("loanId",6);
        paramMap.put("bidMoney",1.0);
//        ResultObjectVO resultObjectVO = bidInfoService.invest(paramMap);

        for(int i = 0; i < 1000; i++){
            executorService.submit(new Callable<Object>() {  //每次提交任务则会从池了抽1个线程给这任务，同时主线程也在并发执行；
                @Override
                public Object call() throws Exception {

                    ResultObjectVO resultObjectVO = bidInfoService.invest(paramMap);


                    return resultObjectVO;
                }
            });
        }

        executorService.shutdown();

    }
}
