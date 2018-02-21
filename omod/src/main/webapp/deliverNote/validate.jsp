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


<springform:form commandName="deliverNote" method="post">
	<fieldset>
		<legend>
			2.
			<spring:message code="inventorypoc.import.reviewXlsFile" />
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
					<td colspan="7">${deliverNote.deliveredDate}<springform:errors
							path="deliveredDate" cssClass="error" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="inventorypoc.originDocument" /></th>
					<td colspan="7">${deliverNote.originDocument}<springform:errors
							path="originDocument" cssClass="error" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="inventorypoc.simamCode" /></th>
					<td colspan="7">${deliverNote.simamDocument}<springform:errors
							path="simamDocument" cssClass="error" />
					</td>
				</tr>
				<tr>
					<th colspan="8">
				</tr>
				<tr>
					<th><spring:message code="inventorypoc.fnm" /></th>
					<th><spring:message code="inventorypoc.designation" /></th>
					<th><spring:message code="inventorypoc.unitsPerPack" /></th>
					<th><spring:message code="inventorypoc.requestedQuantity" /></th>
					<th><spring:message code="inventorypoc.authorizedQuantity" /></th>
					<th><spring:message code="inventorypoc.dispensedQuantity" /></th>
					<th><spring:message code="inventorypoc.unitPrice" /></th>
					<th><spring:message code="inventorypoc.validity" /></th>
				</tr>
			</thead>

			<tbody>
				<c:forEach items="${deliverNote.items}" var="item" varStatus="index">
					<tr class="oddAssessed">
						<td class="info-fnmCode">${item.fnmCode}
							<div>
								<springform:errors path="items[${index.count - 1}].fnmCode"
									cssClass="error" />
							</div>
						</td>
						<td class="info-designation">${item.designation}
							<div>
								<springform:errors path="items[${index.count - 1}].designation"
									cssClass="error" />
							</div>
						</td>
						<td class="info-totalPackageUnits" align="right">${item.totalPackageUnits}
							<div>
								<springform:errors
									path="items[${index.count - 1}].totalPackageUnits"
									cssClass="error" />
							</div>
						</td>
						<td class="info-requestedQuantity" align="right">${item.requestedQuantity}
							<div>
								<springform:errors
									path="items[${index.count - 1}].requestedQuantity"
									cssClass="error" />
							</div>
						</td>
						<td class="info-authorizedQuantity" align="right">${item.authorizedQuantity}
							<div>
								<springform:errors
									path="items[${index.count - 1}].authorizedQuantity"
									cssClass="error" />
							</div>
						</td>
						<td class="info-deliveredQuantity" align="right">${item.deliveredQuantity}
							<div>
								<springform:errors
									path="items[${index.count - 1}].deliveredQuantity"
									cssClass="error" />
							</div>
						</td>
						<td class="info-unitPrice" align="right">${item.unitPrice}
							<div>
								<springform:errors path="items[${index.count - 1}].unitPrice"
									cssClass="error" />
							</div>
						</td>
						<td class="info-expirationDate">${item.expirationDate}
							<div>
								<springform:errors
									path="items[${index.count - 1}].expirationDate"
									cssClass="error" />
							</div>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</fieldset>
	<p>
		<input type="button" id="cancel"
			value="<spring:message code="inventorypoc.back" />"
			onclick="window.location = 'upload.form';" />
		<c:if test="${!hasErrors}">
			<input type="submit" id="next" 
				value="<spring:message code="inventorypoc.next" />" />
		</c:if>
		<br />
	</p>
</springform:form>

<script type="text/javascript">
	var noteItemTrs = document.querySelectorAll(".oddAssessed");

	var rowDuplicate = [];
	var auxrowDuplicate = [];
	
	var lastFnm = "";
	var lastTotalQtyUnit = "" ;
	
	function checkDuplications(collect, fnm, totalQtyUnit){
		
		for (let item of collect)
		{
			if(item.fnm === fnm && item.totalQtyUnit === totalQtyUnit){
				return true;
			}
		}
		return false;
	}
	
	noteItemTrs
			.forEach(function(noteItem) {

				var tdFnmCode = noteItem
						.querySelector(".info-fnmCode");
				var tdTotalPackageUnits = noteItem
						.querySelector(".info-totalPackageUnits");
				
				var fnm = tdFnmCode.textContent.trim();
				var totalQtyUnit = tdTotalPackageUnits.textContent.trim();
				
				
				auxrowDuplicate.push({fnm: lastFnm, totalQtyUnit: lastTotalQtyUnit });
				
				if(checkDuplications(auxrowDuplicate, fnm, totalQtyUnit)){
					
					rowDuplicate.push({fnm: fnm, totalQtyUnit: totalQtyUnit });
				}
				else{
					
				}
				lastFnm = fnm;
				lastTotalQtyUnit = totalQtyUnit;
	
	});
	
	noteItemTrs
	.forEach(function(noteItem) {

		var tdFnmCode = noteItem
				.querySelector(".info-fnmCode");
		var tdTotalPackageUnits = noteItem
				.querySelector(".info-totalPackageUnits");
		
		var fnm = tdFnmCode.textContent;
		var totalQtyUnit = tdTotalPackageUnits.textContent;
		
		if(checkDuplications(rowDuplicate, fnm, totalQtyUnit)){

			noteItem.classList.add("warning");
		}
	});
	
	if(rowDuplicate.length > 0){
		var nextBtn = document.querySelector("#next");
		
	}
	
</script>


<%@ include file="/WEB-INF/template/footer.jsp"%>