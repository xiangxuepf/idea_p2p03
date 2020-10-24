package com.xiangxuepf.p2p.dataservice.service.loan;

import com.xiangxuepf.p2p.common.constants.Constants;
import com.xiangxuepf.p2p.exterface.service.loan.OnlyNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author mhw
 */
@Service("onlyNumberServiceImpl")
public class OnlyNumberServiceImpl implements OnlyNumberService {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    /**
     * 获取redis的全局唯一数字；
     * @return
     */
    @Override
    public Long getOnlyNumber() {



        return redisTemplate.opsForValue().increment(Constants.ONLY_NUMBER,1);
    }
}
