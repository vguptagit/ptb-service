package com.pearson.ptb.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pearson.ptb.bean.HealthApis;
import com.pearson.ptb.service.HealthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author shridhar.mg Controller for health check
 */
@Controller
@Tag(name = "Health", description = "Health APIs")
public class HealthController extends BaseController {

	@Autowired()
	@Qualifier("healthService")
	private HealthService healthService;

	/**
	 * @param request
	 *            Request object
	 * @return health check details
	 */
    @Operation(summary = "Returns health check details", description = "Returns health check details")
	@RequestMapping(value = "/health", method = RequestMethod.GET)
	@ResponseBody
	public HealthApis getHealth(HttpServletRequest request) {
		return healthService.getHealthInfo(request);
	}

}
