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

    /**
     * 检查用户名或邮箱是否有效
     * */
    @RequestMapping(value = "/check_valid.do")
    public ServerResponse check_valid(String str, String type){
        ServerResponse serverResponse = userService.check_valid(str,type);
        return serverResponse;
    }

    /**
     * 获取用户登录信息
     * */
    @RequestMapping(value = "/get_user_info.do")
    public ServerResponse get_user_info(HttpSession session){
        User userInfo = (User) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("用户未登录或登录已过期");
        }
        User userInfo1 = new User();
        userInfo1.setUsername(userInfo.getUsername());
        userInfo1.setPassword(userInfo.getPassword());
        userInfo1.setQuestion(userInfo.getQuestion());
        userInfo1.setAnswer(userInfo.getAnswer());
        userInfo1.setId(userInfo.getId());
        userInfo1.setRole(userInfo.getRole());
        userInfo1.setUpdateTime(userInfo.getUpdateTime());
        userInfo1.setCreateTime(userInfo.getCreateTime());
        return ServerResponse.serverResponseBySuccess(userInfo1);
    }

    /**
     * 登录状态下修改密码
     * */
    @RequestMapping(value = "/reset_password.do")
    public ServerResponse reset_password(HttpSession session, String passwordOld, String passwordNew){
        User userInfo = (User) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("用户未登录或登录已过期");
        }
        ServerResponse serverResponse = userService.reset_password(userInfo.getUsername(),passwordOld,passwordNew);
        return serverResponse;
    }



    /**
     *登录状态更新个人信息
     * */
    @RequestMapping(value = "/update_information.do")
    public ServerResponse update_information(HttpSession session, User user){
        User userInfo = (User) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("用户未登录");
        }
        user.setId(userInfo.getId());
        ServerResponse serverResponse = userService.update_information(user);
        if (serverResponse.isSuccess()){
            //更新session中的用户
            User userInfo1 = userService.selectUserInfoByUserId(userInfo);
            session.setAttribute(Const.CURRENTUSER,userInfo1);
        }
        return serverResponse;
    }

    /**
     * 获取当前登录用户详细信息
     * */
    @RequestMapping(value = "/get_information.do")
    public ServerResponse get_information(HttpSession session){
        User userInfo = (User) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("用户未登录");
        }
        userInfo.setPassword("");
        return ServerResponse.serverResponseBySuccess(userInfo);
    }

    /**
     * 退出登录
     * */
    @RequestMapping(value = "/logout.do")
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(Const.CURRENTUSER);
        return ServerResponse.serverResponseBySuccess();
    }





}
