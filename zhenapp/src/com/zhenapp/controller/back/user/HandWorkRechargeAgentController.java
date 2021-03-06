package com.zhenapp.controller.back.user;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhenapp.po.Custom.TPointsInfoCustom;
import com.zhenapp.po.Custom.TUserInfoCustom;
import com.zhenapp.service.PointsInfoService;
import com.zhenapp.service.PriceInfoService;
import com.zhenapp.service.UserInfoService;
@Transactional
@Controller
@RequestMapping(value="/user")
public class HandWorkRechargeAgentController {
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private PointsInfoService pointsInfoService;
	@Autowired
	private PriceInfoService priceInfoService;
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
	private static Logger logger = Logger.getLogger(HandWorkRechargeAgentController.class);

	/*
	 * 对用户积分手工充值扣款-----代理 
	 */
	@RequestMapping(value="/handworkrecharge")
	public @ResponseBody ModelMap handworkrecharge(HttpSession session,String userpk,String updatepoints,String recharge,String memo) throws Exception{
		ModelMap map = new ModelMap();
		TUserInfoCustom tUserInfoCustomsession=(TUserInfoCustom) session.getAttribute("tUserInfoCustom");
		//TUserInfoCustom tUserInfoCustomagent = userInfoService.findUserByuserpk(tUserInfoCustomsession.getUserpk()+"");
		TUserInfoCustom tUserInfoCustom = userInfoService.findUserByuserpk(userpk);
		Integer newpoints = 0;
		//Integer newpointsagent = 0;
		String Pointstype = "";
		//String Pointstypeagent = "";
		if(recharge.equals("recharge")){
			newpoints=tUserInfoCustom.getPoints() + Integer.parseInt(updatepoints);
			//newpointsagent = tUserInfoCustomsession.getPoints() - Integer.parseInt(updatepoints);
			//if(newpointsagent<0){
			//	map.put("msg", "充值积分超出代理最大积分数");
			//	logger.error("充值积分超出代理最大积分数,代理：" + tUserInfoCustomsession.getUserid() + " 用户："+ tUserInfoCustom.getUserid());
			//	return map;
			//}
			Pointstype = "31";//充值
			//Pointstypeagent = "32";//扣款
		}else{
			newpoints=tUserInfoCustom.getPoints() - Integer.parseInt(updatepoints);
			if(newpoints<0){
				map.put("msg", "扣除积分超出用户最大积分数");
				logger.error("扣除积分超出用户最大积分数,代理：" + tUserInfoCustomsession.getUserid() + " 用户："+ tUserInfoCustom.getUserid());
				return map;
			}
			//newpointsagent = tUserInfoCustomsession.getPoints() + Integer.parseInt(updatepoints);
			Pointstype = "32";//扣款
			//Pointstypeagent = "31";//充值
		}
		//修改代理积分
		//tUserInfoCustomagent.setPoints(newpointsagent);
		//tUserInfoCustomagent.setUpdatetime(sdf.format(new Date()));
		//tUserInfoCustomagent.setUpdateuser(tUserInfoCustomsession.getUserid());
		//userInfoService.updateUserinfoPointByUserid(tUserInfoCustomagent);
		//插入账户明细
		/*TPointsInfoCustom tPointsInfoCustomagent =new TPointsInfoCustom();
		tPointsInfoCustomagent.setCreateuser(tUserInfoCustomsession.getUserid());
		tPointsInfoCustomagent.setCreatetime(sdf.format(new Date()));
		tPointsInfoCustomagent.setUpdatetime(sdf.format(new Date()));
		tPointsInfoCustomagent.setUpdateuser("sys");
		tPointsInfoCustomagent.setPointreason(memo);
		tPointsInfoCustomagent.setPointsid(UUID.randomUUID().toString().replace("-", ""));
		tPointsInfoCustomagent.setPoints(newpointsagent);
		tPointsInfoCustomagent.setPointstype(Pointstypeagent);
		tPointsInfoCustomagent.setPointsupdate(Integer.parseInt(updatepoints));
		tPointsInfoCustomagent.setTaskpk(0);
		tPointsInfoCustomagent.setUserid(tUserInfoCustomsession.getUserid());
		pointsInfoService.savePoints(tPointsInfoCustomagent);*/
		//修改用户积分
		tUserInfoCustom.setPoints(newpoints);
		tUserInfoCustom.setUpdatetime(sdf.format(new Date()));
		tUserInfoCustom.setUpdateuser(tUserInfoCustomsession.getUserid());
		userInfoService.updateUserinfoPointByUserid(tUserInfoCustom);
		//插入账户明细
		TPointsInfoCustom tPointsInfoCustom =new TPointsInfoCustom();
		tPointsInfoCustom.setCreateuser(tUserInfoCustom.getUserid());
		tPointsInfoCustom.setCreatetime(sdf.format(new Date()));
		tPointsInfoCustom.setUpdatetime(sdf.format(new Date()));
		tPointsInfoCustom.setUpdateuser("sys");
		tPointsInfoCustom.setPointreason(memo);
		tPointsInfoCustom.setPointsid(UUID.randomUUID().toString().replace("-", ""));
		tPointsInfoCustom.setPoints(newpoints);
		tPointsInfoCustom.setPointstype(Pointstype);
		tPointsInfoCustom.setPointsupdate(Integer.parseInt(updatepoints));
		tPointsInfoCustom.setTaskpk(0);
		tPointsInfoCustom.setUserid(tUserInfoCustomsession.getUserid());
		pointsInfoService.savePoints(tPointsInfoCustom);
		map.put("ec", "0");
		return map;
	}
}
