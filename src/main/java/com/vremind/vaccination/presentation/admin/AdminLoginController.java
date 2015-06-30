/**
 * 
 */
package com.vremind.vaccination.presentation.admin;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.vremind.vaccination.common.util.WebUtil;
import com.vremind.vaccination.domain.User;
import com.vremind.vaccination.domain.UserType;

/**
 * @author sdoddi
 *
 */
@Controller
public class AdminLoginController {
	
	/**
	 * shows the admin login form.
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/admin/signon.do")
	public ModelAndView showSignOnForm(HttpServletRequest request) {
		return new ModelAndView("/admin/login", "command", new AdminLoginCommand());
	}
	
	/**
	 * shows the admin login form.
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/admin/signoff.do")
	public ModelAndView signOffAndShowSignOnPage(HttpServletRequest request) {
		request.getSession().invalidate();
		return new ModelAndView("forward:/admin/signon.do");
	}
	
	/**
	 * validates the userid and password. if success, this will set the details to session
	 * @param command
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/admin/submitsignon.do", method = RequestMethod.POST)
	public ModelAndView addStudent(@ModelAttribute AdminLoginCommand command, HttpServletRequest request) {
		
		List<String> errors = AdminLoginValidator.validate(command);
		ModelAndView modelView;
		if (errors.size() > 0) {
			modelView = new ModelAndView("/admin/login", "command", new AdminLoginCommand());
		}
		else
		{
			WebUtil.setUserToSession(request, new User(command.getUserId(), new Date(), UserType.ADMIN, null));
			modelView = new ModelAndView("forward:/admin/register.do");
		}
		return modelView;
	}
}
