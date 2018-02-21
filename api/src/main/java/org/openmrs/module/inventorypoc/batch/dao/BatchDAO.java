package org.openmrs.module.inventorypoc.batch.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.module.inventorypoc.batch.model.Batch;

public interface BatchDAO {
	
	public void setSessionFactory(SessionFactory sessionFactory);
	
	public Batch save(Batch batch);
	
	public void updateBatch(Batch batch);
	
	public void decreaseRemainPackageQuantityUnits(Batch batch, Double quantity);
	
	public void updateBatches(List<Batch> batchs);
	
	public List<Batch> findByDrugAndLocationAndNotExpiredDate(Drug drug, Location location, Date date, boolean retired);
	
	public List<Batch> findByLocationAndAvailableQuantity(Location location, Date currentDate, boolean retired);
	
}
