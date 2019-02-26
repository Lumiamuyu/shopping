package com.lumiamuyu.shopping.service;

import com.lumiamuyu.shopping.common.ServerResponse;
import com.lumiamuyu.shopping.pojo.User;

public interface IUserService {

    /**
     * 登录接口
     * */
    ServerResponse login(String username, String password);

    /**
     * 注册接口
     * */
    ServerResponse register(User userInfo);

    /**
     * 根据用户名找回密码
     * */

    ServerResponse forget_get_question(String username);
    /**
     * 提交问题答案
     * */

    ServerResponse forget_check_answer(String username, String question, String answer);

    /**
     *忘记密码的重置密码
     * */
    ServerResponse forget_reset_password(String username, String passwordNew, String forgetToken);

}
