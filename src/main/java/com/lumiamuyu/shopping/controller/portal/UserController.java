package com.lumiamuyu.shopping.controller.portal;

import com.lumiamuyu.shopping.common.Const;
import com.lumiamuyu.shopping.common.ServerResponse;
import com.lumiamuyu.shopping.pojo.User;
import com.lumiamuyu.shopping.service.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Resource
    IUserService userService;

    /*登录*/

    @RequestMapping(value = "/login.do")
    public ServerResponse login(HttpSession session, @RequestParam(value ="username")String username,
                                @RequestParam("password")String password){
        ServerResponse serverResponse = userService.login(username,password);
        if (serverResponse.isSuccess()){
            User userInfo = (User) serverResponse.getData();
            session.setAttribute(Const.CURRENTUSER,userInfo);
        }
        return serverResponse;
    }

    /**
     * 注册
     * */
    @RequestMapping(value = "/register.do")
    public ServerResponse register(HttpSession session, User userInfo){
        ServerResponse serverResponse = userService.register(userInfo);
        return serverResponse;
    }

    /**
     * 根据用户名查询密保问题
     * */
    @RequestMapping(value = "/forget_get_question.do")
    public ServerResponse forget_get_question(String username){
        ServerResponse serverResponse = userService.forget_get_question(username);
        return serverResponse;
    }

    /**
     * 提交问题答案，返回一个token
     * */
    @RequestMapping(value = "/forget_check_answer.do")
    public ServerResponse forget_check_answer(String username, String question, String answer){
        ServerResponse serverResponse = userService.forget_check_answer(username,question,answer);
        return serverResponse;
    }

    /**
     *重置密码
     **/
    @RequestMapping(value = "/forget_reset_password.do")
    public ServerResponse forget_reset_password(String username, String passwordNew, String forgetToken){
        ServerResponse serverResponse = userService.forget_reset_password(username, passwordNew, forgetToken);
        return serverResponse;
    }

}
