package edu.eci.cvds.sampleprj.dao;

import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.sampleprj.dao.PersistenceException;


public interface ItemDAO {

    public void save(Item it) throws PersistenceException;

    public Item load(int id) throws PersistenceException;

}
