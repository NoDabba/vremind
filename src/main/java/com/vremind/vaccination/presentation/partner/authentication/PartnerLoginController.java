/**
 * 
 */
package com.vremind.vaccination.presentation.partner.authentication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.vremind.vaccination.common.util.WebUtil;
import com.vremind.vaccination.domain.PartnerDetails;
import com.vremind.vaccination.domain.User;
import com.vremind.vaccination.domain.UserType;
import com.vremind.vaccination.services.partner.PartnerException;
import com.vremind.vaccination.services.partner.PartnerService;

/**
 * @author sdoddi
 *
 */

@Controller
public class PartnerLoginController {
	
	private final static Logger logger = LoggerFactory.getLogger(PartnerLoginController.class);

	
	private PartnerService partnerService;
	
	/**
	 * @param partnerService the partnerService to set
	 */
	@Autowired
	public void setPartnerService(PartnerService partnerService) {
		this.partnerService = partnerService;
	}


	/**
	 * shows the admin login form.
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/partner/{partnerId}/signon.do")
	public ModelAndView showSignOnForm(@PathVariable String partnerId, Model model) {
		
		model.addAttribute("command", new PartnerLoginCommand());
		model.addAttribute("partnerId", partnerId);
		
		return new ModelAndView("/partner/partner_login", "command", new PartnerLoginCommand());
	}
	
	
	/**
	 * validates the userid and password. if success, this will set the details to session
	 * @param command
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/partner/{partnerId}/submitsignon.do", method = RequestMethod.POST)
	public ModelAndView addStudent(@ModelAttribute PartnerLoginCommand command, @PathVariable String partnerId, HttpServletRequest request, Model model) {
		
		List<String> errors = new ArrayList<String>();
		if (WebUtil.isEmpty(partnerId)) {
			errors.add("Partner ID cannot be null or empty");
		}
		else {
			errors= PartnerLoginValidator.validate(command);
		}
		
		if (errors.size() <= 0) {
			try {
				PartnerDetails partnerDetails = partnerService.getPartnerUser(partnerId, command.getUserId(), command.getPassword());
				WebUtil.setUserToSession(request, new User(command.getUserId(), new Date(), UserType.PARTNER, partnerDetails));
			}
			catch (PartnerException ex) {
				errors.add(ex.getMessage());
				logger.error("error during partner authentication", ex);
			}
		}
		
		ModelAndView modelView;
		if (errors.size() > 0) {
			model.addAttribute("command", new PartnerLoginCommand());
			model.addAttribute("partnerId", partnerId);
			model.addAttribute("errors", errors);
			modelView = new ModelAndView("/partner/partner_login");
		}
		else
		{
			modelView = new ModelAndView("forward:/partner/register.do");
		}
		return modelView;
	}
	
	/**
	 * shows the admin login form.
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/partner/{partnerId}/signoff.do")
	public ModelAndView signOffAndShowSignOnPage(@PathVariable String partnerId, HttpServletRequest request) {
		request.getSession().invalidate();
		return new ModelAndView("forward:/partner/"+partnerId+"/signon.do");
	}
}
