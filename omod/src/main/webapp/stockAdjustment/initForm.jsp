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
<div id="error_msg" hidden="hidden">
	<span> <spring:message
			code="inventorypoc.selectAtLeastOneRowToProceed" /></span>
</div>
<br>
<springform:form modelAttribute="inventory" method="post"
	onsubmit="return validateBeforeNextPage();">
	<fieldset>
		<table id="tableAvailableDrugStock">
			<thead>
				<tr>
					<th></th>
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
					<tr class="oddAssessed">
						<spring:bind path="items[${itemsRow.index}].selected">
							<td><input type="hidden"
								name="_<c:out value="${status.expression}"/>"> <input
								type="checkbox" class="info-checkBox" name="<c:out value="${status.expression}"/>"
								value="true" <c:if test="${status.value}">checked</c:if> /></td>
						</spring:bind>
						<td>${item.batch.drugPackage.drug.name}</td>
						<td align="right" width="20px">${item.batch.drugPackage.totalQuantity}</td>
						<td align="right" width="20px">${item.noteItem.lotNumber}</td>
						<td align="right" width="20px">${item.noteItem.tokenNumber}</td>
						<td align="right"> <fmt:formatDate value="${item.noteItem.dateCreated}" pattern="dd/MM/yyyy" /></td>
						<td align="right"> <fmt:formatDate value="${item.batch.expireDate}" pattern="dd/MM/yyyy" /></td>
						<td align="right" width="20px">${item.batch.packageQuantityUnits}</td>
						<td align="right" width="20px">${item.batch.remainPackageQuantityUnits}</td>
						<td align="right"><spring:bind
								path="items[${itemsRow.index}].newQuantity">
								<input type="number" min="0" id="newQtd${item.batch.id}"
									style="text-align:right;"
									class="info-new-quantity" size="20"
									 disabled="disabled"
									value="${item.newQuantity}"
									name="items[${itemsRow.index}].newQuantity">
								<div class="info-div-new-quantity">
									<springform:errors path="items[${itemsRow.index}].newQuantity"
										cssClass="error" />
								</div>
							</spring:bind></td>
						<td><spring:bind path="items[${itemsRow.index}].reason">
								<textarea id="reason${item.batch.id}" class="info-new-reason"
									name="items[${itemsRow.index}].reason" rows="2" cols="50"
									disabled="disabled"></textarea>
								<div class="info-div-reason">
									<springform:errors path="items[${itemsRow.index}].reason"
										cssClass="error" />
								</div>
							</spring:bind></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</fieldset>
	<p>
		<input type="button"
			value="<spring:message code="inventorypoc.cancel" />"
			onclick="window.location='/openmrs/admin/index.htm'" /> <input type="submit"
			value="<spring:message code="inventorypoc.next" />"
			onclick="$j('#inventory').submit(); return false;" /> <br />
	</p>
</springform:form>
<script type="text/javascript">
	var itemTrs = document.querySelectorAll(".oddAssessed");

	itemTrs.forEach(function(item) {

		var inputCheckBox = item.querySelector(".info-checkBox");
		var inputNewQuantity = item.querySelector(".info-new-quantity");
		var inputReason = item.querySelector(".info-new-reason");

		if (inputCheckBox.checked) {

			inputNewQuantity.disabled = false;
			inputReason.disabled = false;
		}
		else{
			
			var parentNewQuantity = inputNewQuantity.parentNode;
			var parentReason = inputReason.parentNode;
			
			var divNewQuantity = parentNewQuantity.querySelector(".info-div-new-quantity");
			var divReason = parentReason.querySelector(".info-div-reason");
			
			while (divNewQuantity.lastChild) {
				divNewQuantity.removeChild(divNewQuantity.lastChild);
			}
			
			while (divReason.lastChild) {
				divReason.removeChild(divReason.lastChild);
			}
		}
	});
	
	itemTrs.forEach(function(item) {
		item.addEventListener("click",
				function(event) {
					if (event.target.type === "checkbox") {

						var inputNewQuantity = item
								.querySelector(".info-new-quantity");
						var inputReason = item
								.querySelector(".info-new-reason");

						if (event.target.checked) {
							inputNewQuantity.disabled = false;
							inputReason.disabled = false;
						} else {
							inputNewQuantity.disabled = true;
							inputReason.disabled = true;
						}
					}
				});
	});

	function validateBeforeNextPage() {

		var itemTrs = document.querySelectorAll(".oddAssessed");
		var atLeastOneCkecked = false;
		var validTable = true;

		itemTrs.forEach(function(item) {

			var inputCheckBox = item.querySelector(".info-checkBox");

			if (inputCheckBox.checked) {
				atLeastOneCkecked = true;
			}
		});

		var divErrorMsg = document.querySelector("#error_msg");

		if (!atLeastOneCkecked) {
			divErrorMsg.hidden = "";
		} else {
			divErrorMsg.hidden = "hidden";
		}
		return atLeastOneCkecked;
	}
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>