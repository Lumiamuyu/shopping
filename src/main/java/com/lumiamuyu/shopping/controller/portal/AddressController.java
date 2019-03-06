package com.lumiamuyu.shopping.controller.portal;

import com.lumiamuyu.shopping.common.Const;
import com.lumiamuyu.shopping.common.ServerResponse;
import com.lumiamuyu.shopping.pojo.Shipping;
import com.lumiamuyu.shopping.pojo.User;
import com.lumiamuyu.shopping.service.IAddressService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/shipping")
public class AddressController {

    @Resource
    IAddressService addressService;

    /**
     * 添加地址
     * */
    @RequestMapping(value = "/add.do")
    public ServerResponse add(HttpSession session, Shipping shipping){
        User userInfo = (User) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null)
        {
            return ServerResponse.serverResponseByError("请登录");
        }
        return addressService.add(userInfo.getId(), shipping);
    }

    /**
     * 删除地址
     * */
    @RequestMapping(value = "/del.do")
    public ServerResponse del(HttpSession session, Integer shippingId){
        User userInfo = (User) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null)
        {
            return ServerResponse.serverResponseByError("请登录");
        }
        return addressService.del(userInfo.getId(),shippingId);
    }


    /**
     * 登录状态更新地址
     * */
    @RequestMapping(value = "/update.do")
    public ServerResponse update(HttpSession session, Shipping shipping){
        User userInfo = (User) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null)
        {
            return ServerResponse.serverResponseByError("请登录");
        }
        return addressService.update(shipping,userInfo.getId());
    }


    /**
     * 选中查看具体的地址
     * */
    @RequestMapping(value = "/select.do")
    public ServerResponse select(HttpSession session, Integer shippingId){
        User userInfo = (User) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null)
        {
            return ServerResponse.serverResponseByError("请登录");
        }
        return addressService.select(shippingId);
    }
    /**
     * 地址列表,分页查询
     **/
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        User userInfo = (User) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null)
        {
            return ServerResponse.serverResponseByError("请登录");
        }
        return addressService.list(userInfo.getId(),pageNum,pageSize);
    }
}
