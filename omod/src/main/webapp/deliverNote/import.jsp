<%@ taglib prefix="springform"
	uri="http://www.springframework.org/tags/form"%>
<%@ include file="../template/localInclude.jsp"%>
<%@ include file="../template/localHeader.jsp"%>
<openmrs:require privilege="Share Metadata" otherwise="/login.htm"
	redirect="/module/inventorypoc/managePackages.form" />
<%@ include file="../template/jqueryPage.jsp"%>
<openmrs:htmlInclude
	file="${pageContext.request.contextPath}/moduleResources/inventorypoc/css/inventorypoc.css" />

<h3>
	<spring:message code="inventorypoc.importDeliverNote" />
</h3>
<style type="text/css">
#reviewPackageTable {
	width: 100%;
}
</style>

<c:if test="${not empty openmrs_msg}">
	<br />
	<div id="openmrs_msg">
		<span> <spring:message code="${openmrs_msg}"
				text="${openmrs_msg}" />
		</span>
	</div>
	<br />
</c:if>
<springform:form commandName="deliverNote" method="post">
	<fieldset>
		<legend>
			3.
			<spring:message code="inventorypoc.importXlsFile" />
		</legend>
		<table id="reviewPackageTable">
			<thead>
				<tr>
					<th colspan="1"><spring:message
							code="inventorypoc.healthFacility" /></th>
					<td colspan="7">${deliverNote.healthFacilityName}<springform:errors
							path="healthFacilityName" cssClass="error" />
					</td>
				</tr>
				<tr>
					<th colspan="1"><spring:message
							code="inventorypoc.deliveryDate" /></th>
					<td colspan="7">${deliverNote.deliveryDate}<springform:errors
							path="deliveryDate" cssClass="error" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="inventorypoc.simamCode" /></th>
					<td colspan="7">${deliverNote.simamNumber}<springform:errors
							path="simamNumber" cssClass="error" />
					</td>
				</tr>
				<tr>
					<th colspan="8">
				</tr>
				<tr>
					<th><spring:message code="inventorypoc.fnm" /></th>
					<th><spring:message code="inventorypoc.designation" /></th>
					<th><spring:message code="inventorypoc.drug.name.designation" /></th>
					<th><spring:message code="inventorypoc.unitsPerPack" /></th>
					<th><spring:message code="inventorypoc.requestedQuantity" /></th>
					<th><spring:message code="inventorypoc.authorizedQuantity" /></th>
					<th><spring:message code="inventorypoc.dispensedQuantity" /></th>
					<th><spring:message code="inventorypoc.unitPrice" /></th>
					<th><spring:message code="inventorypoc.lot.number" /></th>
					<th><spring:message code="inventorypoc.validity" /></th>
					<th><spring:message code="inventorypoc.token.number" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${deliverNote.items}" var="item">
					<tr class="oddAssessed">
						<td>${item.fnmCode}</td>
						<td class="info-file-designation">${item.designation}</td>
						<td class="info-system-designation">${item.drugNameSytemDesignation}</td>
						<td align="right">${item.totalPackageUnits}</td>
						<td align="right">${item.requestedQuantity}</td>
						<td align="right">${item.authorizedQuantity}</td>
						<td align="right">${item.deliveredQuantity}</td>
						<td align="right">${item.unitPrice}</td>
						<td>${item.lotNumber}</td>
						<td>${item.expireDate}</td>
						<td>${item.tokenNumber}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</fieldset>
	<p>
		<c:if test="${empty openmrs_msg}">
			<input type="button" id="cancel"
				value="<spring:message code="inventorypoc.back" />"
				onclick="window.location = 'validate.form';" />
			<input type="submit" id="next"
				value="<spring:message code="inventorypoc.import" />" />
		</c:if>
		<br />
	</p>
</springform:form>

<script type="text/javascript">
	var noteItemTrs = document.querySelectorAll(".oddAssessed");

	noteItemTrs
			.forEach(function(noteItem) {

				var tdFileDesignation = noteItem
						.querySelector(".info-file-designation");
				var tdSystemDesignation = noteItem
						.querySelector(".info-system-designation");

				var fileDesignation = tdFileDesignation.textContent.replace(
						/[`~!@#$%^&*()_|+\-=?;:'",.<>\{\}\[\]\\\/]/gi, '');
				var systemDesignation = tdSystemDesignation.textContent
						.replace(/[`~!@#$%^&*()_|+\-=?;:'",.<>\{\}\[\]\\\/]/gi,
								'');

				if (!(fileDesignation.toUpperCase() === systemDesignation
						.toUpperCase())) {

					tdFileDesignation.classList.add("warning");
					tdSystemDesignation.classList.add("warning");
				}
			});
</script>


<%@ include file="/WEB-INF/template/footer.jsp"%>