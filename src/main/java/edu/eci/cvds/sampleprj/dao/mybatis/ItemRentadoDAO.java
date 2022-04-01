package edu.eci.cvds.sampleprj.dao;

import java.sql.Date;
import java.util.List;

import edu.eci.cvds.samples.entities.ItemRentado;

public interface ItemRentadoDAO {

    public List<ItemRentado> load(long idClient) throws PersistenceException;

    public ItemRentado loadByItem(int idItem) throws PersistenceException;

}