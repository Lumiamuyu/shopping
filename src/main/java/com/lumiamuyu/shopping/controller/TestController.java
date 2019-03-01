package com.lumiamuyu.shopping.controller;

import com.lumiamuyu.shopping.dao.UserMapper;
import com.lumiamuyu.shopping.pojo.User;
import com.lumiamuyu.shopping.utils.BigDecimalUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    UserMapper userMapper;

    @RequestMapping(value = "/user/{id}")
    public User findUser(@PathVariable Integer id){
        return userMapper.selectByPrimaryKey(id);
    }

    @RequestMapping("/login.do")
    public User userInfo(){
        User user = new User();
        user.setId(1);
        user.setUsername("ssds");
        return user;
    }

}
