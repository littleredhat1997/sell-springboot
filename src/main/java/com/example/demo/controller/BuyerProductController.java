package com.example.demo.controller;

import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.PageRequest;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.example.demo.utils.ConvertUtils;
import com.example.demo.vo.ProductVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "买家商品")
@Controller
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation("获取商品列表")
    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public Result list(@Valid PageRequest pageRequest) {
        PageInfo<Product> pageInfo = productService.listByBuyer(pageRequest);
        List<Product> productList = pageInfo.getList();
        List<ProductVo> productVoList = productList.stream()
                .map(product -> ConvertUtils.convert(product, ProductVo.class))
                .collect(Collectors.toList());
        return PageResult.success(productVoList, pageInfo.getTotal());
    }
}