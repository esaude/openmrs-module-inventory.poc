<%@ taglib prefix="h"
	uri="/WEB-INF/view/module/inventorypoc/taglib/openmrsObjectHandler.tld"%>
<%@ taglib prefix="springform"
	uri="http://www.springframework.org/tags/form"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp" %>
<openmrs:require privilege="Share Metadata" otherwise="/login.htm"
	redirect="/module/metadatasharing/import/list.form" />
<openmrs:htmlInclude file="/scripts/jquery/jquery-1.3.2.min.js" />
<openmrs:htmlInclude
	file="/scripts/jquery/dataTables/css/dataTables.css" />
<openmrs:htmlInclude
	file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js" />
<openmrs:htmlInclude
	file="/scripts/jquery-ui/js/jquery-ui-1.7.2.custom.min.js" />
<openmrs:htmlInclude
	file="/scripts/jquery-ui/css/redmond/jquery-ui-1.7.2.custom.css" />
<openmrs:htmlInclude
	file="${pageContext.request.contextPath}/moduleResources/inventorypoc/css/inventorypoc.css" />
<style>
td.upload {
	padding: 0px 5px;
}

table.upload {
	background-color: whitesmoke;
	border: 1px solid lightgrey;
	padding: 5px;
	margin: 10px;
}

#file {
	width: 575px;
	background: white;
}

.titleColumn {
	width: 30px;
}
</style>
<h3>
	<spring:message code="inventorypoc.importDeliverNote" />
</h3>

<springform:form commandName="uploadFile" enctype="multipart/form-data"
	id="uploadFile">
	<fieldset>
		<legend>
			1.
			<spring:message code="inventorypoc.chooseFileToImport" />
		</legend>
		<table class="upload">
			<tr>
				<td class="titleColumn"><spring:message
						code="inventorypoc.file" /></td>
				<td><spring:message code="inventorypoc.file.xls" /></td>
			</tr>
			<tr>
				<td><img
					src="${pageContext.request.contextPath}/moduleResources/inventorypoc/images/XLS.png" />
				</td>
				<td><input type="file" name="file" id="file" size="60" /><br />
					<springform:errors path="file" cssClass="error" /></td>
			</tr>
		</table>
	</fieldset>
	<p>
		<input type="button"
			value="<spring:message code="inventorypoc.cancel" />"
			onclick="window.location='/openmrs/admin/index.htm'" /> <input
			type="submit" value="<spring:message code="inventorypoc.next" />"
			onclick="$j('#uploadFile').submit(); $j(this).attr('disabled', true);" />

		<br />
	</p>
</springform:form>

<%@ include file="/WEB-INF/template/footer.jsp"%>