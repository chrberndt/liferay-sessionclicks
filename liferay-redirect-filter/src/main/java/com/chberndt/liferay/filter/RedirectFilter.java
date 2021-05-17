package com.chberndt.liferay.filter;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.SessionClicks;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(
	enabled = true, immediate = true,
	property = {
		"after-filter=Aggregate Filter", "dispatcher=FORWARD",
		"dispatcher=REQUEST", "servlet-context-name=",
		"servlet-filter-name=Redirect Filter",
		"url-pattern=/web/guest/session-clicks"
	},
	service = Filter.class
)
public class RedirectFilter extends BaseFilter {

	@Override
	protected Log getLog() {
		return _log;
	}

	@Override
	protected void processFilter(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info("processFilter()");
		}

		User user = _portal.getUser(httpServletRequest);

		if (user != null) {

			// User must be signed in

			PortalPreferences portalPreferences =
				PortletPreferencesFactoryUtil.getPortalPreferences(
					httpServletRequest.getSession(), user.getUserId(), true);

			String myKey = portalPreferences.getValue(
				_DEFAULT_NAMESPACE, "my-key");

			if (_log.isInfoEnabled()) {
				_log.info("myKey = " + myKey);
			}

			// TODO: Send redirect based on myKey value
			// httpServletResponse.sendRedirect("https://www.chberndt.com");

			// Hand over to next filter (Does not apply if you send a redirect above.)

			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
		else {

			// Hand over to next filter

			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
	}

	private static final String _DEFAULT_NAMESPACE =
		SessionClicks.class.getName();

	private static final Log _log = LogFactoryUtil.getLog(RedirectFilter.class);

	@Reference
	private Portal _portal;

}