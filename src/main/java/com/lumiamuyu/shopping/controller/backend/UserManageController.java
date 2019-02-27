package com.lumiamuyu.shopping.controller.backend;


import com.lumiamuyu.shopping.common.Const;
import com.lumiamuyu.shopping.common.ServerResponse;
import com.lumiamuyu.shopping.pojo.User;
import com.lumiamuyu.shopping.service.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 后台用户控制类
 * */
@RestController
@RequestMapping(value = "/manage/user")
public class UserManageController {

    @Resource
    IUserService userService;


    /**
     * 管理员登录
     * */
    @RequestMapping(value = "/login.do")
    public ServerResponse login(HttpSession session, String username, String password){
        ServerResponse serverResponse = userService.login(username,password);
        if (serverResponse.isSuccess()){
            User userInfo = (User) serverResponse.getData();
            if (userInfo.getRole() == Const.RoleEnum.ROLE_CUSTOMER.getCode()){
                return ServerResponse.serverResponseByError("无权限登录");
            }
            session.setAttribute(Const.CURRENTUSER,userInfo);
        }
        return serverResponse;
    }

}
