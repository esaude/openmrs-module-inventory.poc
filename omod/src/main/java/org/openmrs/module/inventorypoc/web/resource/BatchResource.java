package org.openmrs.module.inventorypoc.web.resource;

import java.util.Date;
import java.util.List;

import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.batch.service.BatchService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.MetadataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1
+ "/batch", order = 2, supportedClass = Batch.class, supportedOpenmrsVersions = { "1.8.*", "1.9.*", "1.10.*",
		"1.11.*", "1.12.*" })
public class BatchResource extends MetadataDelegatingCrudResource<Batch> {

	@Override
	public DelegatingResourceDescription getRepresentationDescription(final Representation rep) {

		if ((rep instanceof DefaultRepresentation) || (rep instanceof RefRepresentation)
				|| (rep instanceof FullRepresentation)) {
			final DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("packageQuantityUnits");
			description.addProperty("remainPackageQuantityUnits");
			description.addProperty("unBalancedUnitsQuantity");
			description.addProperty("reciptDate");
			description.addProperty("expireDate");

			return description;
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected PageableResult doSearch(final RequestContext context) {

		final Drug drug = Context.getConceptService().getDrugByUuid(context.getParameter("drug"));
		final Location location = Context.getLocationService().getLocationByUuid(context.getParameter("location"));
		final List<Batch> batches = Context.getService(BatchService.class)
				.findBatchesByDrugAndLocationAndNotExpiredDate(drug, location, new Date());
		return new NeedsPaging(batches, context);
	}

	@Override
	public Batch newDelegate() {
		throw new ResourceDoesNotSupportOperationException();
	}

	@Override
	public Batch save(final Batch delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}

	@Override
	public Batch getByUniqueId(final String uniqueId) {
		throw new ResourceDoesNotSupportOperationException();
	}

	@Override
	public void purge(final Batch delegate, final RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
}