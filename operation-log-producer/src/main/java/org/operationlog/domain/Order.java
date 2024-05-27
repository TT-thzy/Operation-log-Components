package org.operationlog.domain;

import lombok.Data;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/24
 **/
@Data
public class Order {

    private String id;

    private String orderNo;

    private Long productId;

    private Integer qty;

}
