package com.lumiamuyu.shopping.controller.portal;


import com.lumiamuyu.shopping.common.ServerResponse;
import com.lumiamuyu.shopping.service.IProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Resource
    IProductService productService;
    /**
     *前台-商品详情
     * */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(Integer productId){
        return productService.detail_portal(productId);
    }

    /**
     * 产品搜索及动态排序List
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(@RequestParam(required = false) Integer categoryId,
                               @RequestParam(required = false)String keyword,
                               @RequestParam(required = false,defaultValue="1")Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                               @RequestParam(required = false,defaultValue = "")String orderBy){

        return productService.list(categoryId,keyword,pageNum,pageSize,orderBy);
    }


}
