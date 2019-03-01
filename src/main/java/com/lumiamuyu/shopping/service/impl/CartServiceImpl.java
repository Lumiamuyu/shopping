package com.lumiamuyu.shopping.service.impl;

import com.google.common.collect.Lists;
import com.lumiamuyu.shopping.common.Const;
import com.lumiamuyu.shopping.common.ServerResponse;
import com.lumiamuyu.shopping.dao.CartMapper;
import com.lumiamuyu.shopping.dao.ProductMapper;
import com.lumiamuyu.shopping.pojo.Cart;
import com.lumiamuyu.shopping.pojo.Product;
import com.lumiamuyu.shopping.service.ICartService;
import com.lumiamuyu.shopping.utils.BigDecimalUtils;
import com.lumiamuyu.shopping.vo.CartProductVO;
import com.lumiamuyu.shopping.vo.CartVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Resource
    CartMapper cartMapper;

    @Resource
    ProductMapper productMapper;

    @Override
    public ServerResponse add(Integer userId, Integer productId, Integer count) {
        //1.参数非空校验
        if (productId==null || count == null){
            ServerResponse.serverResponseByError("参数为空");
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.serverResponseByError("商品无效");
        }
        //2.根据userId和productId查询购物车信息

        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart == null){
            //添加
            //查询结果为空，则为添加
            Cart cart1 = new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
            cartMapper.insert(cart1);

        }else {
            //更新
            //查询结果不为空，则为更新
            Cart cart1 = new Cart();
            cart1.setId(cart.getId());
            cart1.setProductId(productId);
            cart1.setQuantity(count+cart.getQuantity());
            cart1.setUserId(userId);
            cart1.setChecked(cart.getChecked());
            cartMapper.updateByPrimaryKey(cart1);
        }
        CartVO cartVO = getCartVOLimit(userId);
        return ServerResponse.serverResponseBySuccess(cartVO);

    }


    /**
     * 获取CartVO
     * */
    private CartVO getCartVOLimit(Integer userId){
        CartVO cartVO = new CartVO();
        //1.根据userId查询给用户的购物车信息
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVO> cartProductVOList = Lists.newArrayList();
        //总价格
        BigDecimal cartTotalPrice = new BigDecimal("0");
        //2.转换为CartVO
        if (cartList != null && cartList.size()>0){
            for (Cart cart:cartList) {
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(cart.getId());
                cartProductVO.setUserId(cart.getUserId());
                cartProductVO.setProductId(cart.getProductId());
                cartProductVO.setQuantity(cart.getQuantity());
                cartProductVO.setProductChecked(cart.getChecked());

                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if (product != null){
                    cartProductVO.setProductId(product.getId());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductSubtitle(product.getSubtitle());
                    cartProductVO.setProductStock(product.getStock());

                    int stock = product.getStock();
                    int limitProductCount = 0;
                    if (stock >= cart.getQuantity()){
                        //库存充足
                        limitProductCount = cart.getQuantity();
                        cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
                    }else {
                        //库存不足
                        limitProductCount = stock;
                        //更新购物车中商品数量
                        Cart cart1 = new Cart();
                        cart1.setId(cart.getId());
                        cart1.setUserId(cart.getUserId());
                        cart1.setProductId(cart.getProductId());
                        cart1.setQuantity(stock);
                        cart1.setChecked(cart.getChecked());
                        cartMapper.updateByPrimaryKey(cart1);
                        cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
                    }
                    cartProductVO.setQuantity(limitProductCount);
                    cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),Double.valueOf(cartProductVO.getQuantity())));
                    BigDecimal total = BigDecimalUtils.mul(product.getPrice().doubleValue(),Double.valueOf(cartProductVO.getQuantity()));
                }
                //购物车总价格
                if (cartProductVO.getProductChecked() == Const.CartCheckedEnum.PRODUCT_CHECKED.getCode()){

                    cartTotalPrice = BigDecimalUtils.add(cartTotalPrice,cartProductVO.getProductTotalPrice());
                }
                cartProductVOList.add(cartProductVO);

            }
        }
        //3.计算购物车总价格
        cartVO.setCartTotalPrice(cartTotalPrice);
        cartVO.setCartProductVOList(cartProductVOList);
        //4.判断购物车里的商品是否全选
        //判断checked的 “ 0 ” 是否大于 0
        int countUnchecked = cartMapper.countUnchecked(userId);
        if (countUnchecked > 0){
            cartVO.setAllChecked(false);
        }else {
            cartVO.setAllChecked(true);
        }
        //5.返回结果
        return cartVO;
    }


    /**
     * 购物车列表
     * */
    @Override
    public ServerResponse list(Integer userId) {
        CartVO cartVO = getCartVOLimit(userId);
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    /**
     * 更新购物车中某个产品的数量
     * */
    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {
        //1.参数非空校验
        if (productId == null || count == null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //2.根据userId和productId查询购物车信息
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart != null){
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }
        return ServerResponse.serverResponseBySuccess(getCartVOLimit(userId));
    }

    /**
     *移除购物车某个产品
     **/
    @Override
    public ServerResponse delete_product(Integer userId, String productIds) {
        //1.参数非空校验
        if (productIds == null || productIds.equals("")){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        List<Integer> productIdList = Lists.newArrayList();
        //2.将字符串productIds转为
        String[] productIdArr = productIds.split(",");
        if (productIdArr != null && productIdArr.length>0){
            for (String productIdstr:productIdArr) {
                Integer productId = Integer.parseInt(productIdstr);
                productIdList.add(productId);
            }
        }
        //3.数据库操作
        cartMapper.deleteByUserIdAndProductIds(userId,productIdList);

        return ServerResponse.serverResponseBySuccess(getCartVOLimit(userId));
    }

    /**
     *购物车中选中某个商品
     * */
    /**
     * 取消选中某个商品
     * */
    @Override
    public ServerResponse select(Integer userId, Integer productId, Integer checked) {
        //1.参数非空校验
        if (productId == null ){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //2.查询是否存在这个商品

        //3.修改状态checked
        cartMapper.selectOrUnselectProduct(userId,productId,checked);
        //返回结果
        return ServerResponse.serverResponseBySuccess(getCartVOLimit(userId));
    }

    /**
     * 取消全选
     * */

    /**
     * 全选商品
     * */
    @Override
    public ServerResponse select_all(Integer userId, Integer checked) {
        cartMapper.selectAllByUserId(userId,checked);
        return ServerResponse.serverResponseBySuccess(getCartVOLimit(userId));
    }

    /**
     * 购物车中产品数量
     * */
    @Override
    public ServerResponse get_cart_product_count(Integer userId) {
        int quantity = cartMapper.get_cart_product_count(userId);
        return ServerResponse.serverResponseBySuccess(quantity);
    }


}
