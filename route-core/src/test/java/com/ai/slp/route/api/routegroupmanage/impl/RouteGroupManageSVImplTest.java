package com.ai.slp.route.api.routegroupmanage.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.slp.route.api.routegroupmanage.interfaces.IRouteGroupManageSV;
import com.ai.slp.route.api.routegroupmanage.param.RouteGroupPageSearchRequest;
import com.ai.slp.route.api.routegroupmanage.param.RouteGroupPageSearchResponse;
import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/context/core-context.xml" })
public class RouteGroupManageSVImplTest {
	private static final Logger log = LogManager.getLogger(RouteGroupManageSVImplTest.class);
	
	@Autowired
	private IRouteGroupManageSV routeGroupManageSV;
	@Test
	public void queryPageSearch(){
		RouteGroupPageSearchRequest request = new RouteGroupPageSearchRequest();
		//
		request.setTenantId("SLP");
		request.setPageNo(1);
		request.setPageSize(1000);
		//
		log.info("request:"+JSON.toJSONString(request));
		RouteGroupPageSearchResponse response = this.routeGroupManageSV.queryPageSearch(request);
		//
		log.info("response:"+JSON.toJSONString(response));
	}

}