<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Drug Packages" otherwise="/login.htm"
	redirect="/module/inventorypoc/addNewDrugPackage.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<h2>
	<spring:message code="inventorypoc.manage.drug.name" />
</h2>

<form method="post">

<table>
	<fieldset>
		<table>
			<tr>
				<td align="right"><spring:message code="inventorypoc.error.drug.package.drug" /></td>
				<td><spring:bind path="drugPackage.drug">
						<openmrs_tag:drugField formFieldName="drugPackage.drug"
							initialValue="${status.value}" />
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>
			<tr>
				<td align="right"><spring:message
						code="inventorypoc.manage.drug.package.barcocde" /></td>
				<td><spring:bind path="drugPackage.barcode">
						<input type="text" name="barcode" value="${status.value}"
							size="35" />
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>
			<tr>
				<td align="right"><spring:message
						code="inventorypoc.manage.drug.package.total.units" /></td>
				<td><spring:bind path="drugPackage.totalQuantity">
						<input type="text" " name="totalQuantity" value="${status.value}"
							size="35" />
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>
		</table>
		<input type="submit" value="<spring:message code="inventorypoc.error.drug.package.save"/>" name="save" />
	</fieldset>
</table>
</form>

<br />

<%@ include file="/WEB-INF/template/footer.jsp"%>