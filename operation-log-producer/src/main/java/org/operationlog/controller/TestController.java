package org.operationlog.controller;

import org.operationlog.OperationLog;
import org.operationlog.OperationLogModule;
import org.operationlog.OperationLogTag;
import org.operationlog.domain.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/24
 **/
@RestController
@RequestMapping("/api/orders")
@OperationLogModule("Orders")
public class TestController {


    @OperationLog(
            success = "save order id : {#params['orderId']}",
            operator = OperationLog.Operation.CREATE
    )
    @PostMapping("/v1")
    public String saveOrder(String orderId) {
        return "save order success";
    }

    @OperationLog(
            fail = "save order id : {#params['orderId']} fail, error reason: {#errorMsg}",
            operator = OperationLog.Operation.CREATE
    )
    @PostMapping("/v2")
    public String saveOrderV2(String orderId) {
        int i = 0 / 0;
        return "save order fail";
    }

    @OperationLog(
            success = "save order id : {#params['order'].getId()}, orderNo: {#params['order'].getOrderNo()}, result: {#result}",
            operator = OperationLog.Operation.CREATE,
            tags = {
                    @OperationLogTag(key = "productId", value = "{#params['order'].getProductId()}")
            }
    )
    @PostMapping("/v3")
    public String saveOrderV3(@RequestBody Order order) {
        return "save order success";
    }

    @OperationLog(
            fail = "save order id : {#params['order'].getId()}, orderNo: {#params['order'].getOrderNo()}, fail reason: {#errorMsg}",
            operator = OperationLog.Operation.CREATE,
            tags = {
                    @OperationLogTag(key = "productId", value = "{#params['order'].getProductId()}")
            }
    )
    @PostMapping("/v4")
    public String saveOrderV4(@RequestBody Order order) {
        return "save order fail";
    }

    @OperationLog(
            success = "save order id : {#params['order'].getId()}, orderNo: {#params['order'].getOrderNo()}, result: {#result.getId()}",
            operator = OperationLog.Operation.CREATE,
            tags = {
                    @OperationLogTag(key = "productId", value = "{#params['order'].getProductId()}")
            }
    )
    @PostMapping("/v5")
    public Order saveOrderV5(@RequestBody Order order) {
        Order result = new Order();
        result.setId("888888");
        return result;
    }
}
