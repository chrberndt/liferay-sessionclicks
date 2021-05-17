<%@ include file="/init.jsp" %>

<p>
	<b><liferay-ui:message key="sessionclicks.caption"/></b>
</p>

<% 
	String myKey = SessionClicks.get(request, "my-key", null);
	String namespace = renderResponse.getNamespace();
%>

myKey = <%= myKey %>

<aui:form>
	<aui:button name="setKey" primary="true" value="Set Key"></aui:button>
</aui:form>

<aui:script use="aui-core">
	var setKey = A.one('#<%= namespace %>setKey');

	setKey.on('click', function (event) {		
		Liferay.Util.Session.set(
			'my-key',
			'my-value'
		);
	});
</aui:script>