package com.ai.slp.route.service.business.interfaces;

import com.ai.slp.route.api.supplyproduct.param.SupplyProduct;
import com.ai.slp.route.api.supplyproduct.param.SupplyProductQueryVo;

/**
 * Created by xin on 16-5-30.
 */
public interface ISupplyProductQueryBusiSV {
    SupplyProduct updateSaleCount(SupplyProductQueryVo supplyProductQueryVo);
}
