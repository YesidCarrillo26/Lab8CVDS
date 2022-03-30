package edu.eci.cvds.sampleprj.dao;

import java.sql.Date;
import java.util.List;

import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;

public interface ClienteDAO {
    public void save(Cliente cl) throws PersistenceException;

    public Cliente load(long id) throws PersistenceException;

    public List<Cliente> loadAll() throws PersistenceException;

    public void saveRenting(long docu, Item item, Date initialDate, Date finalDate) throws PersistenceException;

    public void updateState(long docu, boolean state) throws PersistenceException;
}
