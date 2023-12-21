package com.pearson.ptb.service;

import com.pearson.ptb.bean.Health;
import com.pearson.ptb.bean.HealthApis;
import com.pearson.ptb.framework.ConfigurationManager;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

@Service("healthService")
public class HealthService {

	/**
	 * This method will returns API Governance Health rule details.
	 * 
	 * @return a string which contains Application Name, Owner details and
	 *         domain details.
	 */
	public HealthApis getHealthInfo(HttpServletRequest request) {
		Health health = new Health();
		health.setApplicationName(
				ConfigurationManager.getInstance().getHealthApplicationName());
		health.setApplicationOwner(ConfigurationManager.getInstance()
				.getHealthApplicationOwnerName());
		health.setApplicationDomain(request.getServerName());

		List<Health> healthData = new ArrayList<Health>();
		healthData.add(health);

		HealthApis api = new HealthApis();

		api.setHealthApi(healthData);

		return api;
	}

}
