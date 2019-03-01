package com.lumiamuyu.shopping.controller.portal;

import com.lumiamuyu.shopping.common.ServerResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cart")
public class CartController {
    /*
    * 购物车中添加商品
    * */

    @RequestMapping(value = "/add.do")
    public ServerResponse add(Integer productId,Integer count){
        return null;
    }
}
