package com.zhenapp.controller.frontend.register;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhenapp.service.UserInfoService;

@Controller
@RequestMapping(value="/frontend")
public class CheckEmailController {
	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
	@Autowired
	private UserInfoService userInfoService;
	
	@RequestMapping(value="/ajax/checkEmailUnique")
	public @ResponseBody ModelMap checkEmailUnique(String param) throws Exception{
		int i= userInfoService.checkEmailUnique(param);
		ModelMap map= new ModelMap();
		if(i > 0){
			map.put("status", "n");
			map.put("info", "此邮箱账号已被占用");
		}else{
			map.put("status", "y");
		}
		return map;
	}
	
}
