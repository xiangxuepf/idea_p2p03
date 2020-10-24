package com.xiangxuepf.p2p.exterface.service.user;

import com.xiangxuepf.p2p.exterface.model.user.User;
import com.xiangxuepf.p2p.exterface.vo.ResultObjectVO;

/**
 * @author mhw
 */
public interface UserService {
    /**
     * 获取平台注册总人数；
     * @return
     */
    Long queryAllUerCount();

    /**
     * 根据手机号码查询用户信息；
     * @param phone
     * @return
     */
    User queryUserByPhone(String phone);

    /**
     * 用户注册；
     * @param phone
     * @param loginPassword
     * @return
     */
    ResultObjectVO register(String phone, String loginPassword);

    /**
     * 根据用户标识更新用户信息
     * @param user
     * @return
     */
    int modifyUserById(User user);

    /**
     * 用户登录
     * @param phone
     * @param loginPassword
     * @return
     */
    User login(String phone, String loginPassword);
}
