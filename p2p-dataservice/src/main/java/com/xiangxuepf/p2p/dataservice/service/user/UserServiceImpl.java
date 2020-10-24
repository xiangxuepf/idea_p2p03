package com.xiangxuepf.p2p.dataservice.service.user;

import com.xiangxuepf.p2p.common.constants.Constants;
import com.xiangxuepf.p2p.dataservice.mapper.user.FinanceAccountMapper;
import com.xiangxuepf.p2p.dataservice.mapper.user.UserMapper;
import com.xiangxuepf.p2p.exterface.model.user.FinanceAccount;
import com.xiangxuepf.p2p.exterface.model.user.User;
import com.xiangxuepf.p2p.exterface.service.user.UserService;
import com.xiangxuepf.p2p.exterface.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author mhw
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public Long queryAllUerCount() {
        //首先去redis查询，有，直接用；
        //更改redis中key值的序列化方式；
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //获取指定操作某一个key的操作对象；    //@ 另一种操作redis方式
        BoundValueOperations<Object, Object> boundValueOps =
                redisTemplate.boundValueOps(Constants.ALL_USER_COUNT);
        //获取指定key的value值；
        Long allUserCount = (Long)boundValueOps.get();
        if (null == allUserCount) {
            //去数据库查询；
            allUserCount = userMapper.selectAllUserCount();
            //存在redis中；
            boundValueOps.set(allUserCount,15, TimeUnit.MINUTES);
        }

        return allUserCount;
    }

    @Override
    public User queryUserByPhone(String phone) {


        return userMapper.selectUserByPhone(phone);
    }

    @Override
    public ResultObjectVO register(String phone, String loginPassword) {
        ResultObjectVO resultObject = new ResultObjectVO();
        //默认值
        resultObject.setErrorCode(Constants.SUCCESS);
        //新增用户
        /*
            insertSelective
            给user,set数据；
            phone
            password
            注册时间
            最新登录时间；
         */
        User user = new User();
        user.setPhone(phone);
        user.setLoginPassword(loginPassword);
        user.setAddTime(new Date());
        user.setLastLoginTime(new Date());
        int insertUserCount = userMapper.insertSelective(user);
        //新增账号，且送金额；
        /*if(>0)则成功；
        则新增账号
        insertSelective();
        set账号数据，3个字段；
        uid
        Money,送金额；*/
        if(insertUserCount > 0){
            User userInfo = userMapper.selectUserByPhone(phone);
            FinanceAccount financeAccount = new FinanceAccount();
            financeAccount.setUid(userInfo.getId());
            financeAccount.setAvailableMoney(888.0);
            int insertFinanceCount = financeAccountMapper.insertSelective(financeAccount);
            if(insertFinanceCount < 0){
                resultObject.setErrorCode(Constants.FAIL);
            }

        }else {
            resultObject.setErrorCode(Constants.FAIL);
        }


        return resultObject;
    }

    @Override
    public int modifyUserById(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User login(String phone, String loginPassword) {
        //查询用户信息
        User user = userMapper.selectUserByPhoneAndLoginPassword(phone,loginPassword);

        //更新用户登录时间
        /*
        判断用户是否存在
	！= null
	更新登录时间
	我们不能用上面那个user，而是new一个user //@
	User updateUser = new User()
	set new Date
	updateByPrimaryKeySelective(updateUser);
         */
        if(null != user){
            User updateUser = new User();
            updateUser.setId(user.getId()); //通过旧对象getId
            updateUser.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(updateUser);
        }

        return user; //返回不是updateUser;
    }


}
