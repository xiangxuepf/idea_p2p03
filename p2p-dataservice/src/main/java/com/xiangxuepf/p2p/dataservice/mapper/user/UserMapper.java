package com.xiangxuepf.p2p.dataservice.mapper.user;

import com.xiangxuepf.p2p.exterface.model.user.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    Long selectAllUserCount();

    /**
     * 根据手机号查询用户信息；
     * @param phone
     * @return
     */
    User selectUserByPhone(String phone);

    /**
     * 根据手机号和密码查询用户信息
     * @param phone
     * @param loginPassword
     * @return
     */
    User selectUserByPhoneAndLoginPassword(String phone, String loginPassword);
}