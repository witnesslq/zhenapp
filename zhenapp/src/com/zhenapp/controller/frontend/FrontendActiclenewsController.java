package com.zhenapp.controller.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/frontend")
public class FrontendActiclenewsController {
	@RequestMapping(value="/articlenews")
	public @ResponseBody ModelAndView articlenews() throws Exception{
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("/frontend/articlenews.jsp");
		return mv;
	}
}
