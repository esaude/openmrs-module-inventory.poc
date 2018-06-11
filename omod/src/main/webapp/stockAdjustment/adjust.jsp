<%@ taglib prefix="h"
	uri="/WEB-INF/view/module/inventorypoc/taglib/openmrsObjectHandler.tld"%>
<%@ taglib prefix="springform"
	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/localHeader.jsp"%>
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
	<spring:message code="inventorypoc.stockAdjustment" />
</h3>
<br>
<c:choose>
	<c:when test="${not empty openmrs_msg}">
		<br />
		<div id="openmrs_msg">
			<span> <spring:message code="${openmrs_msg}"
					text="${openmrs_msg}" />
			</span>
		</div>
		<br />
	</c:when>
	<c:otherwise>
		<br />
		<div id="openmrs_info_msg">
			<span> <spring:message code="inventorypoc.confirm.before.save" />
			</span>
		</div>
		<br />
	</c:otherwise>
</c:choose>
<springform:form modelAttribute="inventory" method="post">
	<fieldset>
		<table id="tableAvailableDrugStock">
			<thead>
				<tr>
					<th><spring:message code="inventorypoc.designation" /></th>
					<th><spring:message code="inventorypoc.unitsPerPack" /></th>
					<th><spring:message code="inventorypoc.lot.number" /></th>
					<th><spring:message code="inventorypoc.token.number" /></th>
					<th><spring:message code="inventorypoc.creationdate" /></th>
					<th><spring:message code="inventorypoc.expirationDate" /></th>
					<th><spring:message code="inventorypoc.initial.quantity" /></th>
					<th><spring:message
							code="inventorypoc.initial.current.quantity" /></th>
					<th><spring:message
							code="inventorypoc.stockAdjustment.newQuantity" /></th>
					<th><spring:message
							code="inventorypoc.stockAdjustment.adjustReason" /></th>
				</tr>
			</thead>

			<tbody>
				<c:forEach items="${inventory.items}" var="item"
					varStatus="itemsRow">
					<c:if test="${item.selected}">
						<tr class="oddAssessed">
							<td>${item.batch.drugPackage.drug.name}</td>
							<td align="right" width="20px">${item.batch.drugPackage.totalQuantity}</td>
							<td align="right" width="20px">${item.noteItem.lotNumber}</td>
							<td align="right" width="20px">${item.noteItem.tokenNumber}</td>
							<td align="right"> <fmt:formatDate value="${item.noteItem.dateCreated}" pattern="dd/MM/yyyy" /></td>
							<td align="right"> <fmt:formatDate value="${item.batch.expireDate}" pattern="dd/MM/yyyy" /></td>
							<td align="right" width="20px">${item.batch.packageQuantityUnits}</td>
							<td align="right" width="20px">${item.batch.remainPackageQuantityUnits}</td>
							<td align="right">${item.newQuantity}</td>
							<td align="right">${item.reason}</td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
	</fieldset>
	<c:if test="${empty openmrs_msg}">
		<p>
			<input type="button"
				value="<spring:message code="inventorypoc.back" />"
				onclick="window.location='initForm.form'" /> <input type="submit"
				value="<spring:message code="inventorypoc.save" />"
				onclick="$j('#inventory').submit(); return false;" /> <br />
		</p>
	</c:if>
</springform:form>

<%@ include file="/WEB-INF/template/footer.jsp"%>