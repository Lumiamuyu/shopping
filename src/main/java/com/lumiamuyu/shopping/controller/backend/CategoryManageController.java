package com.lumiamuyu.shopping.controller.backend;

import com.lumiamuyu.shopping.common.Const;
import com.lumiamuyu.shopping.common.ServerResponse;
import com.lumiamuyu.shopping.pojo.User;
import com.lumiamuyu.shopping.service.ICategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/category")
public class CategoryManageController {

    @Resource
    ICategoryService categoryService;

    /**
     * 获取品类子节点(平级)
     * */
    @RequestMapping(value = "/get_category.do")
    public ServerResponse get_category(HttpSession session,Integer categoryId){
        User userInfo = (User) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PERMISSION.getCode(),Const.ResponseCodeEnum.NO_PERMISSION.getDesc());
        }

        return categoryService.get_category(categoryId);
    }

    /**
     * 增加节点
     * */
    @RequestMapping(value = "/add_category.do")
    public ServerResponse add_category(HttpSession session,
                                       @RequestParam(required = false,defaultValue = "0") Integer parentId,
                                       String categoryName){
        User userInfo = (User) session.getAttribute(Const.CURRENTUSER);
        //判断是否登录
        if (userInfo == null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),
                    Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断是否为管理员
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PERMISSION.getCode(),
                    Const.ResponseCodeEnum.NO_PERMISSION.getDesc());
        }
        ServerResponse serverResponse = categoryService.add_category(parentId,categoryName);
        return serverResponse;

    }

    /**
     * 修改节点
     * */
    @RequestMapping(value = "/set_category_name.do")
    public ServerResponse set_category_name(HttpSession session, Integer categoryId, String categoryName){
        User userInfo = (User) session.getAttribute(Const.CURRENTUSER);
        //判断是否登录
        if (userInfo == null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),
                    Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断是否为管理员
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PERMISSION.getCode(),
                    Const.ResponseCodeEnum.NO_PERMISSION.getDesc());
        }

        ServerResponse serverResponse = categoryService.set_category_name(categoryId,categoryName);
        return serverResponse;
    }

    /**
     * 获取当前分类id及递归子节点categoryId
     * */
    @RequestMapping(value = "/get_deep_category.do")
    public ServerResponse get_deep_category(HttpSession session, Integer categoryId){
        User userInfo = (User) session.getAttribute(Const.CURRENTUSER);
        //判断是否登录
        if (userInfo == null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),
                    Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断是否为管理员
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PERMISSION.getCode(),
                    Const.ResponseCodeEnum.NO_PERMISSION.getDesc());
        }
        return categoryService.get_deep_category(categoryId);
    }


}
