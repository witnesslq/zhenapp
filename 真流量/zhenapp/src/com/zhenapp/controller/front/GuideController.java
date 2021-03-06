package com.zhenapp.controller.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zhenapp.po.PageInfo;
import com.zhenapp.po.Custom.TGuideInfoCustom;
import com.zhenapp.po.Custom.TelectricityCustom;
import com.zhenapp.service.ElectrityService;
import com.zhenapp.service.GuideService;

@Controller
@RequestMapping(value="/guide")
public class GuideController {
	
	@Autowired
	private ElectrityService electrityService;
	@Autowired
	private GuideService guideService;
	
	
	/*
	 * 查询前10条电商信息  用于侧边栏展示 按时间倒序排列
	 */
	@RequestMapping(value="/findGuide_10")
	public @ResponseBody ModelAndView findGuide_10(Integer pagenum,Integer pagesize) throws Exception{
		ModelAndView mv = new ModelAndView();
		List<TelectricityCustom> TelectricityCustomlist = electrityService
				.findElectrity_10();
		List<TGuideInfoCustom> TGuideInfoCustomlist = guideService
				.findGuide_10();
		PageInfo pageinfo = new PageInfo();

		if (pagenum == null || pagesize == null) {
			pagenum = 1;
			pagesize = 15;
			pageinfo.setPagenum(pagenum);
			pageinfo.setPagesize(pagesize);
		} else {
			pageinfo.setPagenum(pagenum);
			pageinfo.setPagesize(pagesize);
		}
		pageinfo.setPageagin((pagenum - 1) * pagesize);
		List<TGuideInfoCustom> tGuideInfoCustomcountlist=guideService.findAllGuide();
		
		List<TGuideInfoCustom> tGuideInfoCustomAlllist = guideService
				.findGuideBypage(pageinfo);
		mv.addObject("total", tGuideInfoCustomcountlist.size());
		mv.addObject("pagenum", pagenum);
		mv.addObject("TelectricityCustomlist", TelectricityCustomlist);
		mv.addObject("TGuideInfoCustomlist", TGuideInfoCustomlist);
		mv.addObject("tGuideInfoCustomAlllist", tGuideInfoCustomAlllist);
		mv.setViewName("/bootstrap/guide.jsp");
		return mv;
	}
	
	/*
	 * 查询前10条电商信息  用于侧边栏展示 按时间倒序排列
	 */
	@RequestMapping(value = "/findGuide_10/{pagenum}/{pagesize}")
	public @ResponseBody
	ModelAndView findGuide_10_2(
			@PathVariable(value = "pagenum") Integer pagenum,
			@PathVariable(value = "pagesize") Integer pagesize)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		List<TelectricityCustom> TelectricityCustomlist = electrityService
				.findElectrity_10();
		List<TGuideInfoCustom> TGuideInfoCustomlist = guideService
				.findGuide_10();
		PageInfo pageinfo = new PageInfo();

		if (pagenum == null || pagesize == null) {
			pagenum = 1;
			pagesize = 15;
			pageinfo.setPagenum(pagenum);
			pageinfo.setPagesize(pagesize);
		} else {
			pageinfo.setPagenum(pagenum);
			pageinfo.setPagesize(pagesize);
		}
		pageinfo.setPageagin((pagenum - 1) * pagesize);
		List<TGuideInfoCustom> tGuideInfoCustomcountlist=guideService.findAllGuide();
		
		List<TGuideInfoCustom> tGuideInfoCustomAlllist = guideService
				.findGuideBypage(pageinfo);
		mv.addObject("total", tGuideInfoCustomcountlist.size());
		mv.addObject("pagenum", pagenum);
		mv.addObject("TelectricityCustomlist", TelectricityCustomlist);
		mv.addObject("TGuideInfoCustomlist", TGuideInfoCustomlist);
		mv.addObject("tGuideInfoCustomAlllist", tGuideInfoCustomAlllist);
		mv.setViewName("/bootstrap/guide.jsp");
		return mv;
	}
	
	/*
	 * 根据电商信息主键查询电商信息
	 */
	@RequestMapping(value="/details/{guidepk}")
	public ModelAndView details(@PathVariable(value="guidepk")String guidepk) throws Exception{
		ModelAndView mv= new ModelAndView();
		List<TelectricityCustom> TelectricityCustomlist=electrityService.findElectrity_10();
		List<TGuideInfoCustom> TGuideInfoCustomlist=guideService.findGuide_10();
		TGuideInfoCustom tGuideInfoCustom=guideService.findElectrityBypk(guidepk);
		
		mv.addObject("TelectricityCustomlist",TelectricityCustomlist);
		mv.addObject("TGuideInfoCustomlist",TGuideInfoCustomlist);
		mv.addObject("tGuideInfoCustom",tGuideInfoCustom);
		mv.setViewName("/bootstrap/guidedetails.jsp");
		return mv;
	}
	
}
