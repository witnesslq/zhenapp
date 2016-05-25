package com.zhenapp.controller.back;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.zhenapp.po.Custom.TAgentInfoCustom;
import com.zhenapp.po.Custom.TComboInfoCustom;
import com.zhenapp.po.Custom.TPointsInfoCustom;
import com.zhenapp.po.Custom.TRechargeInfoCustom;
import com.zhenapp.po.Custom.TUserInfoCustom;
import com.zhenapp.service.AgentInfoService;
import com.zhenapp.service.ComboInfoService;
import com.zhenapp.service.PointsInfoService;
import com.zhenapp.service.RechargeInfoService;
import com.zhenapp.service.UserInfoService;

@Controller
@RequestMapping(value="/points")
public class PointsInfoController {
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat yyyyMMdd=new SimpleDateFormat("yyyyMMdd");
	
	@Autowired
	private PointsInfoService pointsInfoService;
	@Autowired
	private ComboInfoService comboInfoService;
	@Autowired
	private RechargeInfoService rechargeInfoService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private AgentInfoService agentInfoService;
	
	
	/*
	 * 跳转到购买积分界面
	 */
	@RequestMapping(value="/responsebuypoints")
	public ModelAndView responsebuypoints(HttpSession session) throws Exception{
		ModelAndView mv = new ModelAndView();
		TUserInfoCustom tUserInfoCustom=(TUserInfoCustom) session.getAttribute("tUserInfoCustom");
		HashMap<String, Object> hashmap=new HashMap<String, Object>();
		hashmap.put("agentid", tUserInfoCustom.getAgentid());
		hashmap.put("page", 0);
		hashmap.put("rows", 1000);
		List<TComboInfoCustom> tComboInfoCustomlist = comboInfoService.findComboByAgentid(hashmap);
		mv.addObject("tComboInfoCustomlist", tComboInfoCustomlist);
		mv.setViewName("/backstage/points/buypoints.jsp");
		return mv;
	}

	/*
	 * 插入充值记录
	 */
	@RequestMapping(value="/buypoints")
	public ModelAndView buypoints(String id,HttpServletRequest request) throws Exception{
		ModelAndView mv=new ModelAndView();
		String verificationcode = UUID.randomUUID().toString().replace("-", "");
		HttpSession session=request.getSession();
		TComboInfoCustom tComboInfoCustom= comboInfoService.findComboByid(id);
		TRechargeInfoCustom tRechargeInfoCustom=new TRechargeInfoCustom();
		tRechargeInfoCustom.setRechargeid(UUID.randomUUID().toString().replace("-", ""));
		tRechargeInfoCustom.setRechargemoney(tComboInfoCustom.getCombomoney());
		tRechargeInfoCustom.setRechargepoints(tComboInfoCustom.getCombointegral());
		tRechargeInfoCustom.setRechargegivemoney(tComboInfoCustom.getCombogivemoney());
		tRechargeInfoCustom.setRechargegivepoints(tComboInfoCustom.getCombogiveintegral());
		tRechargeInfoCustom.setRechargestate("24");//待确认状态
		tRechargeInfoCustom.setRechargeverification(verificationcode);
		tRechargeInfoCustom.setUpdatetime(sdf.format(new Date()));
		TUserInfoCustom tUserInfoCustom=(TUserInfoCustom) session.getAttribute("tUserInfoCustom");
		tRechargeInfoCustom.setUpdateuser(tUserInfoCustom.getUserid());
		tRechargeInfoCustom.setCreateuser(tUserInfoCustom.getUserid());
		tRechargeInfoCustom.setCreatetime(sdf.format(new Date()));
		int i = rechargeInfoService.insertRechargeinfo(tRechargeInfoCustom);
		if(i>0){
			mv.addObject("tComboInfoCustom", tComboInfoCustom);
			mv.addObject("Verificationcode", verificationcode);
			mv.setViewName("/backstage/points/buyingpoints.jsp");
		}
		return mv;
	}
	//===========================================================
	/*
	 * 跳转到充值记录界面--代理
	 */
	@RequestMapping(value="/responseconsumeagent")
	public ModelAndView responseconsumeagent(HttpSession session,Integer page,Integer rows,String datefrom,String dateto,String usernick,String rechargeid) throws Exception{
		ModelAndView mv=new ModelAndView();
		TUserInfoCustom tUserInfoCustom=(TUserInfoCustom) session.getAttribute("tUserInfoCustom");
		String points= userInfoService.findpointsByUsernickAndPwd(tUserInfoCustom);
		TAgentInfoCustom tAgentInfoCustom = agentInfoService.findAgentByuserid(tUserInfoCustom.getUserid());
		HashMap<String, Object> pagemap= new HashMap<String, Object>();
		if (page == null || page==0) {
			page = 1;
		} 
		rows = 10;
		pagemap.put("page", (page - 1) * rows);
		pagemap.put("rows", rows);
		if(datefrom != null && !datefrom.equals("")){
			pagemap.put("datefrom", datefrom.replace("-", "")+"000000");
		}
		if(dateto != null && !dateto.equals("")){
			pagemap.put("dateto", dateto.replace("-", "")+"235959");
		}
		pagemap.put("usernick", usernick);
		pagemap.put("rechargeid", rechargeid);
		/*
		* 代理用户
		*/
		pagemap.put("userid", tUserInfoCustom.getUserid());
		int total = rechargeInfoService.findTotalRechargeinfoByUserAndpage(pagemap);
		List<TRechargeInfoCustom> tRechargeInfoCustomlist = rechargeInfoService.findRechargeinfoByUserAndpage(pagemap);
		mv.addObject("total",total);
		mv.addObject("pagenum", page);
		mv.addObject("datefrom", datefrom);
		mv.addObject("dateto", dateto);
		mv.addObject("usernick", usernick);
		mv.addObject("rechargeid", rechargeid);
		mv.addObject("points", points);
		mv.addObject("tAgentInfoCustom", tAgentInfoCustom);
		mv.addObject("tRechargeInfoCustomlist",tRechargeInfoCustomlist);
		mv.setViewName("/backstage/agent/listrecharge.jsp");
		return mv;
	}
	/*
	 * 跳转到积分记录界面--代理
	 */
	@RequestMapping(value="/responserecordspointsagent")
	public ModelAndView responserecordspointsagent(HttpSession session,Integer page,Integer rows,String datefrom ,String dateto,String usernick,String taskpk) throws Exception{
		ModelAndView mv=new ModelAndView();
		TUserInfoCustom tUserInfoCustomsession=(TUserInfoCustom) session.getAttribute("tUserInfoCustom");
		String points= userInfoService.findpointsByUsernickAndPwd(tUserInfoCustomsession);
		TAgentInfoCustom tAgentInfoCustom = agentInfoService.findAgentByuserid(tUserInfoCustomsession.getUserid());
		HashMap<String, Object> pagemap= new HashMap<String, Object>();
		if (page == null || page==0) {
			page = 1;
		} 
		rows = 10;
		pagemap.put("page", (page - 1) * rows);
		pagemap.put("rows", rows);
		if(datefrom != null && !datefrom.equals("")){
			pagemap.put("datefrom", datefrom.replace("-", ""));
		}
		if(dateto != null && !dateto.equals("")){
			pagemap.put("dateto", dateto.replace("-", ""));
		}
		pagemap.put("usernick", usernick);
		pagemap.put("taskpk", taskpk);
		/*
		* 代理用户
		*/
		pagemap.put("userid", tUserInfoCustomsession.getUserid());
		List<TPointsInfoCustom> tPointsInfoCustomlist = pointsInfoService.findPointsInfoByPageandRole(pagemap);
		int total = pointsInfoService.findPointsCountsByPageandRole(pagemap);
		mv.addObject("total",total);
		mv.addObject("pagenum", page);
		mv.addObject("datefrom",datefrom);
		mv.addObject("dateto",dateto);
		mv.addObject("usernick",usernick);
		mv.addObject("points", points);
		mv.addObject("taskpk", taskpk);
		mv.addObject("tAgentInfoCustom", tAgentInfoCustom);
		mv.addObject("tPointsInfoCustomlist",tPointsInfoCustomlist);
		mv.setViewName("/backstage/agent/listcoin.jsp");
		return mv;
	}
	
	
	/*
	 * 跳转到积分明细界面--用户
	 */
	@RequestMapping(value="/responserecordspoints")
	public ModelAndView responserecordspoints(HttpSession session,Integer page,Integer rows,String taskpk) throws Exception{
		ModelAndView mv=new ModelAndView();
		TUserInfoCustom tUserInfoCustom=(TUserInfoCustom) session.getAttribute("tUserInfoCustom");
		HashMap<String, Object> pagemap= new HashMap<String, Object>();
		if (page == null || page==0) {
			page = 1;
		} 
		rows = 10;
		pagemap.put("page", (page - 1) * rows);
		pagemap.put("rows", rows);
		pagemap.put("taskpk", taskpk);
		/*
		 * 普通用户
		 */
		pagemap.put("createuser", tUserInfoCustom.getUserid());
		List<TPointsInfoCustom> tPointsInfoCustomlist = pointsInfoService.findPointsInfoByPage(pagemap);
		int total = pointsInfoService.findTotalPointsInfoByPage(pagemap);
		mv.addObject("total",total);
		mv.addObject("pagenum", page);
		mv.addObject("taskpk", taskpk);
		mv.addObject("tPointsInfoCustomlist",tPointsInfoCustomlist);
		mv.setViewName("/backstage/points/recordspoints.jsp");
		return mv;
	}
	
	/*
	 * 跳转到购买记录界面
	 */
	@RequestMapping(value="/responseconsume")
	public ModelAndView responseconsume(HttpSession session,Integer page,Integer rows, String datefrom,String dateto) throws Exception{
		ModelAndView mv=new ModelAndView();
		TUserInfoCustom tUserInfoCustom=(TUserInfoCustom) session.getAttribute("tUserInfoCustom");
		HashMap<String,Object> pagemap=new HashMap<String,Object>();
		datefrom=datefrom!=null?datefrom.replace("-", ""):"";
		dateto=dateto!=null?dateto.replace("-", ""):"";
		pagemap.put("datefrom", datefrom);
		pagemap.put("dateto", dateto);
		List<TRechargeInfoCustom> tRechargeInfoCustomlist = new ArrayList<TRechargeInfoCustom>();
		
		if (page == null || page==0) {
			page = 1;
		} 
		rows = 10;
		pagemap.put("page", (page - 1) * rows);
		pagemap.put("rows", rows);
		int total = 0;
		/*
		 * 普通用户
		 */
		pagemap.put("createuser", tUserInfoCustom.getUserid());
		total = rechargeInfoService.findTotalRechargeinfoByUserAndpage(pagemap);
		tRechargeInfoCustomlist = rechargeInfoService.findRechargeinfoByUserAndpage(pagemap);

		mv.addObject("total",total);
		mv.addObject("pagenum", page);
		mv.addObject("tRechargeInfoCustomlist", tRechargeInfoCustomlist);
		mv.setViewName("/backstage/points/consumepoints.jsp");
		return mv;
	}
	
	
	
	
	/*
	 * 跳转到充值记录界面-----系统管理员
	 */
	@RequestMapping(value="/responseconsumeadmin")
	public ModelAndView responseconsumeadmin(HttpSession session,Integer page,Integer rows,String datefrom,String dateto,String usernick,String rechargeid) throws Exception{
		ModelAndView mv=new ModelAndView();
		//TUserInfoCustom tUserInfoCustom=(TUserInfoCustom) session.getAttribute("tUserInfoCustom");
		HashMap<String, Object> pagemap= new HashMap<String, Object>();
		if (page == null || page==0) {
			page = 1;
		} 
		rows = 10;
		pagemap.put("page", (page - 1) * rows);
		pagemap.put("rows", rows);
		if(datefrom !=null){
			pagemap.put("datefrom", datefrom.replace("-", ""));
		}
		if(dateto !=null){
			pagemap.put("dateto", dateto.replace("-", ""));
		}
		pagemap.put("usernick", usernick);
		pagemap.put("rechargeid", rechargeid);
		/*
		* 系统管理员
		*/
		int total = rechargeInfoService.findTotalRechargeinfoByUserAndpage(pagemap);
		List<TRechargeInfoCustom> tRechargeInfoCustomlist = rechargeInfoService.findRechargeinfoByUserAndpage(pagemap);
		mv.addObject("total",total);
		mv.addObject("pagenum", page);
		mv.addObject("tRechargeInfoCustomlist",tRechargeInfoCustomlist);
		mv.setViewName("/backstage/admin/listrecharge.jsp");
		return mv;
	}
	
	
	/*
	 * 跳转到积分记录界面--系统管理员
	 */
	@RequestMapping(value="/responserecordspointsadmin")
	public ModelAndView responserecordspointsadmin(HttpSession session,Integer page,Integer rows,String datefrom ,String dateto,String usernick,String taskpk) throws Exception{
		ModelAndView mv=new ModelAndView();
		//TUserInfoCustom tUserInfoCustomsession=(TUserInfoCustom) session.getAttribute("tUserInfoCustom");
		HashMap<String, Object> pagemap= new HashMap<String, Object>();
		if (page == null || page==0) {
			page = 1;
		} 
		rows = 10;
		pagemap.put("page", (page - 1) * rows);
		pagemap.put("rows", rows);
		if(datefrom != null && !datefrom.equals("")){
			pagemap.put("datefrom", datefrom.replace("-", "") + "000000");
		}
		if(dateto != null && !dateto.equals("")){
			pagemap.put("dateto", dateto.replace("-", "") + "235959");
		}
		pagemap.put("usernick", usernick);
		pagemap.put("taskpk", taskpk);
		/*
		* 系统管理员
		*/
		//pagemap.put("userid", tUserInfoCustomsession.getUserid());
		List<TPointsInfoCustom> tPointsInfoCustomlist = pointsInfoService.findPointsInfoByPage(pagemap);
		int total = pointsInfoService.findTotalPointsInfoByPage(pagemap);
		mv.addObject("total",total);
		mv.addObject("pagenum", page);
		mv.addObject("datefrom", datefrom);
		mv.addObject("dateto", dateto);
		mv.addObject("usernick", usernick);
		mv.addObject("taskpk", taskpk);
		mv.addObject("tPointsInfoCustomlist",tPointsInfoCustomlist);
		mv.setViewName("/backstage/admin/listcoin.jsp");
		return mv;
	}
	
//============================================================================================以上为最新
	/*@RequestMapping(value="/findPointsInfoByPage")
	public @ResponseBody ModelMap findPointsInfoByPage(Integer page,Integer rows,HttpServletRequest request) throws Exception{
		ModelMap map=new ModelMap();
		HttpSession session = request.getSession();
		TUserInfoCustom tUserInfoCustom=(TUserInfoCustom) session.getAttribute("tUserInfoCustom");
		HashMap<String, Object> pagemap= new HashMap<String, Object>();
		if (page == null || page == null) {
			pagemap.put("page", 0);
			pagemap.put("rows", 10);
		} else {
			pagemap.put("page", page-1);
			pagemap.put("rows", rows);
		}
		
		List<TPointsInfoCustom> tPointsInfoCustomlist = new ArrayList<TPointsInfoCustom>();
		int counts = 0;
		if(tUserInfoCustom.getUserroleid()==1){
			/*
			 * 系统管理员
			 *
			tPointsInfoCustomlist = pointsInfoService.findPointsInfoByPage(pagemap);
			counts = pointsInfoService.findTotalPointsInfoByPage(pagemap);
		}else if(tUserInfoCustom.getUserroleid()==2){
			/*
			 * 代理用户
			 *
			pagemap.put("userid", tUserInfoCustom.getUserid());
			tPointsInfoCustomlist = pointsInfoService.findPointsInfoByPageandRole(pagemap);
			counts = pointsInfoService.findPointsCountsByPageandRole(pagemap);
		}else{
			/*
			 * 普通用户
			 *
			pagemap.put("createuser", tUserInfoCustom.getUserid());
			tPointsInfoCustomlist = pointsInfoService.findPointsInfoByPage(pagemap);
			counts = pointsInfoService.findTotalPointsInfoByPage(pagemap);
		}
		map.put("total", counts);
		map.put("rows", tPointsInfoCustomlist);
		
		return map;
	}*/
}
