package com.xiangxuepf.p2p.timer.timer;

import com.xiangxuepf.p2p.exterface.service.loan.IncomeRecordService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author mhw
 */
@Component
public class TimerManager {
    @Autowired
    private IncomeRecordService incomeRecordService;
    private Logger logger = LogManager.getLogger(com.xiangxuepf.p2p.timer.test.TimerManager.class);

    /**
     * 生成收益计划；
     */
    @Scheduled(cron = "0/5 * * * * ?") //每5秒执行一次
    public void generateIncomePlan(){
        logger.info("--生成收益计划开始--");
        incomeRecordService.generateIncomePlan();
        logger.info("--生成收益计划结束--");

    }

    /**
     * 生成收益返还
     */
    @Scheduled(cron = "0/10 * * * * ?") //每5秒执行一次
    public void generateIncomeBack(){
        logger.info("--生成收益返还开始--");
        incomeRecordService.generateIncomeBack();
        logger.info("--生成收益返还结束--");

    }

}
