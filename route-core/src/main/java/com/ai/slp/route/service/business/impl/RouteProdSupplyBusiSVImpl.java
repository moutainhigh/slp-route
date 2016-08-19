package com.ai.slp.route.service.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.opt.base.vo.PageInfo;
import com.ai.opt.base.vo.ResponseHeader;
import com.ai.opt.sdk.util.DateUtil;
import com.ai.slp.route.api.routeprodsupplymanage.param.RouteProdSupplyPageSearchRequest;
import com.ai.slp.route.api.routeprodsupplymanage.param.RouteProdSupplyPageSearchResponse;
import com.ai.slp.route.api.routeprodsupplymanage.param.RouteProdSupplyPageSearchVo;
import com.ai.slp.route.api.routeprodsupplymanage.param.RouteProdSupplyUpdateUsableNumRequest;
import com.ai.slp.route.api.routeprodsupplymanage.param.RouteProdSupplyUpdateUsableNumResponse;
import com.ai.slp.route.dao.mapper.bo.RouteProdSupply;
import com.ai.slp.route.dao.mapper.bo.RouteSupplyAddsLog;
import com.ai.slp.route.service.atom.interfaces.IRouteProdSupplyAtomSV;
import com.ai.slp.route.service.atom.interfaces.IRouteSupplyAddsLogAtomSV;
import com.ai.slp.route.service.business.interfaces.IRouteProdSupplyBusiSV;
import com.ai.slp.route.util.SequenceUtil;
@Service
public class RouteProdSupplyBusiSVImpl implements IRouteProdSupplyBusiSV {

	@Autowired
	private IRouteProdSupplyAtomSV routeProdSupplyAtomSV;
	@Autowired
	private IRouteSupplyAddsLogAtomSV routeSupplyAddsLogAtomSV;

	@Override
	public RouteProdSupplyPageSearchResponse queryPageSearch(RouteProdSupplyPageSearchRequest request) {
		//
		RouteProdSupplyPageSearchResponse response = new RouteProdSupplyPageSearchResponse();
		//
		Integer pageNo = request.getPageNo();
		Integer pageSize = request.getPageSize();
		String routeId = request.getRouteId();
		String standedProdId = request.getStandedProdId();
		String supplyId = request.getSupplyId();
		String supplyName = request.getSupplyName();
		String productCatId = request.getProductCatId();
		String tenantId = request.getTenantId();
		//
		RouteProdSupply routeProdSupply = new RouteProdSupply();
		//
		routeProdSupply.setTenantId(tenantId);
		routeProdSupply.setRouteId(routeId);
		routeProdSupply.setStandedProdId(standedProdId);
		routeProdSupply.setSupplyId(supplyId);
		routeProdSupply.setSupplyName(supplyName);
		routeProdSupply.setProductCatId(productCatId);
		//
		PageInfo<RouteProdSupply> pageInfo = this.routeProdSupplyAtomSV.queryRouteProdSupplyPageInfo(routeProdSupply, pageNo, pageSize);
		PageInfo<RouteProdSupplyPageSearchVo> voPageInfo = new PageInfo<RouteProdSupplyPageSearchVo>();
		voPageInfo.setCount(pageInfo.getCount());
		voPageInfo.setPageCount(pageInfo.getPageCount());
		voPageInfo.setPageNo(pageNo);
		voPageInfo.setPageSize(pageSize);
		//
		List<RouteProdSupplyPageSearchVo> voList = new ArrayList<RouteProdSupplyPageSearchVo>();
		RouteProdSupplyPageSearchVo vo = null;
		//
		int index = 0;
		for(RouteProdSupply routeProdSupplyVo : pageInfo.getResult()){
			index++;
			
			vo = new RouteProdSupplyPageSearchVo();
			//
			vo.setIndex((pageNo - 1) * pageSize + index);
			vo.setRouteId(routeProdSupplyVo.getRouteId());
			vo.setTenantId(routeProdSupplyVo.getTenantId());
			vo.setSupplyId(routeProdSupplyVo.getSupplyId());
			vo.setSupplyName(routeProdSupplyVo.getSupplyName());
			vo.setTotalNum(routeProdSupplyVo.getTotalNum());
			vo.setUsableNum(routeProdSupplyVo.getUsableNum());
			vo.setUsedNum(routeProdSupplyVo.getUsedNum());
			//
			voList.add(vo);
		}
		voPageInfo.setResult(voList);
		//
		response.setPageInfo(voPageInfo);
		//
		return response;
	}
	@Override
	@Transactional
	public RouteProdSupplyUpdateUsableNumResponse updateUsableNum(RouteProdSupplyUpdateUsableNumRequest request){
		//
		RouteProdSupplyUpdateUsableNumResponse response = new RouteProdSupplyUpdateUsableNumResponse();
		
		//
		RouteProdSupply vo = this.routeProdSupplyAtomSV.getRouteProdSupplyByPrimaryKey(request.getSupplyId());
		Long unsableNum = null == vo.getUsableNum()?0:vo.getUsableNum();
		Long totalNum = null == vo.getTotalNum()?0:vo.getTotalNum();
		//
		RouteProdSupply routeProdSupply = new RouteProdSupply();
		//
		routeProdSupply.setSupplyId(request.getSupplyId());
		routeProdSupply.setUsableNum(request.getUsableNum() + unsableNum);
		routeProdSupply.setTotalNum(request.getUsableNum() + totalNum);
		//
		this.routeProdSupplyAtomSV.updateByPrimaryKeySelective(routeProdSupply);
		
		//添加仓库量
		RouteSupplyAddsLog routeSupplyAddsLog = new RouteSupplyAddsLog();
		routeSupplyAddsLog.setSupplyAddsLogId(SequenceUtil.createSupplyAddsLogId());
		routeSupplyAddsLog.setOperId(Long.valueOf(1));
		routeSupplyAddsLog.setOperTime(DateUtil.getSysDate());
		routeSupplyAddsLog.setSource("");
		routeSupplyAddsLog.setSupplyId(request.getSupplyId());
		routeSupplyAddsLog.setSupplyName(request.getSupplyName());
		routeSupplyAddsLog.setSupplyNum(request.getUsableNum());
		routeSupplyAddsLog.setBeforeUsableNum(unsableNum);
		//
		this.routeSupplyAddsLogAtomSV.insert(routeSupplyAddsLog);
		//
		return response;
	}
}
