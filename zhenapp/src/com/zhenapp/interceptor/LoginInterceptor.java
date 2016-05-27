package com.zhenapp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zhenapp.po.Custom.TUserInfoCustom;

/**
 * 
 * <p>
 * Title: HandlerInterceptor1
 * </p>
 * <p>
 * Description:登陆认证拦截器
 * </p>
 * <p>
 * Company: www.itcast.com
 * </p>
 * 
 * @author 传智.燕青
 * @date 2015-4-14下午4:45:47
 * @version 1.0
 */
public class LoginInterceptor implements HandlerInterceptor {

	// 进入 Handler方法之前执行
	// 用于身份认证、身份授权
	// 比如身份认证，如果认证通过表示当前用户没有登陆，需要此方法拦截不再向下执行
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		// 获取请求的url
		String url = request.getRequestURI();
		// 判断session
		HttpSession session = request.getSession();
		// 从session中取出用户身份信息
		TUserInfoCustom tUserInfoCustom = (TUserInfoCustom)session.getAttribute("tUserInfoCustom");
		if (tUserInfoCustom != null) {
			// 身份存在，放行
			return true;
		}	
		/*
		 * 静态资源全部放行
		 */
		if (url.indexOf(".js") >= 0 || url.indexOf(".css") >= 0
				|| url.indexOf(".icon") >= 0 || url.indexOf(".jpg") >= 0
				|| url.indexOf(".png") >= 0 || url.indexOf(".jsp") >= 0
				|| url.indexOf(".gif") >= 0 || url.indexOf(".woff2") >= 0 || url.indexOf(".ttf") >= 0) {
			// 如果进行登陆提交，放行
			return true;
		}
		/*
		 * 调用api的请求不用登录
		 */
		 if(url.indexOf("/api/")>=0 ){ 
			 return true; 
		 }
		 /*
		  * 调用前端请求不用登录
		  */
		 if(url.indexOf("/frontend/")>=0 ){ 
				 return true; 
		 }
		// 判断url是否是公开 地址（实际使用时将公开 地址配置配置文件中）
		// 这里公开地址是登陆提交的地址
		if (url.indexOf("user/Loginrest") >= 0 || url.indexOf("user/register") >= 0 || url.indexOf("user/findUserByNick") >=0 ) {
				// 如果进行登陆提交，放行
				return true;
			}
		if (url.indexOf("/user/authlogout") >= 0) {
				// 如果进行登陆提交，放行
				return true;
			}
		// 执行这里表示用户身份需要认证，跳转登陆页面
		request.getRequestDispatcher("/user/authlogout").forward(request,
				response);

		// return false表示拦截，不向下执行
		// return true表示放行
		return false;
	}

	// 进入Handler方法之后，返回modelAndView之前执行
	// 应用场景从modelAndView出发：将公用的模型数据(比如菜单导航)在这里传到视图，也可以在这里统一指定视图
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		//System.out.println("LoginInterceptor...postHandle");

	}

	// 执行Handler完成执行此方法
	// 应用场景：统一异常处理，统一日志处理
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (ex != null) {
			System.out.println("存在异常信息,转发到状态页面！");
			request.getRequestDispatcher("/info.jsp").forward(
					request, response);
		}
		//System.out.println("LoginInterceptor...afterCompletion");
	}

}
