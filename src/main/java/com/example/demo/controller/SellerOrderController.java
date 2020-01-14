package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderTimeline;
import com.example.demo.service.SellerOrderService;
import com.example.demo.vo.OrderDetailVo;
import com.example.demo.vo.OrderVo;
import com.example.demo.vo.OrderTimelineVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "商家订单")
@Controller
@RequestMapping("/seller/order")
public class SellerOrderController {

    @Autowired
    private SellerOrderService sellerOrderService;

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PageResult<List<OrderVo>> list(@Valid OrderPageRequest pageRequest) {
        // page
        PageInfo<Order> pageInfo = sellerOrderService.list(pageRequest);
        // convert
        List<OrderVo> orderVoList = pageInfo.getList()
                .stream()
                .map(order -> Convert.convert(OrderVo.class, order))
                .collect(Collectors.toList());
        return PageResult.success(orderVoList, pageInfo.getTotal());
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/all/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<Map<String, Object>> all(@PathVariable("id") Long id) {
        List<OrderDetail> orderDetailList = sellerOrderService.getDetail(id);
        List<OrderDetailVo> orderDetailVoList = orderDetailList
                .stream()
                .map(orderDetail -> Convert.convert(OrderDetailVo.class, orderDetail))
                .collect(Collectors.toList());

        List<OrderTimeline> orderTimelineList = sellerOrderService.getTimeline(id);
        List<OrderTimelineVo> orderTimelineVoList = orderTimelineList
                .stream()
                .map(orderTimeline -> Convert.convert(OrderTimelineVo.class, orderTimeline))
                .collect(Collectors.toList());

        Map<String, Object> map = new HashMap<>();
        map.put("detail", orderDetailVoList);
        map.put("timeline", orderTimelineVoList);
        return Result.success(map);
    }
}
