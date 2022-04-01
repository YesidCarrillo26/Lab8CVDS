package edu.eci.cvds.sampleprj.dao.mybatis;

import com.google.inject.Inject;
import edu.eci.cvds.sampleprj.dao.ClienteDAO;
import edu.eci.cvds.sampleprj.dao.PersistenceException;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;

import java.sql.Date;
import java.util.List;

public class MyBATISClienteDAO implements ClienteDAO {

    @Inject
    private ClienteMapper clienteMapper;


    @Override
    public void save(Cliente cl) throws PersistenceException {
        try {
            clienteMapper.insertarCliente(cl);
        }catch (org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al agregar cliente",e);
        }

    }

    @Override
    public Cliente load(long id) throws PersistenceException {
        try{
            return clienteMapper.consultarCliente(id);
        } catch (org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al consultar el cliente" + id, e);
        }
    }

    @Override
    public List<Cliente> loadAll() throws PersistenceException {
        try{
            return clienteMapper.consultarClientes();
        } catch (org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al consultar los clientes", e);
        }
    }

    @Override
    public void saveRenting(long docu, Item item, Date initialDate, Date finalDate) throws PersistenceException {
        try{
            clienteMapper.insertarItemRentado(docu, item.getId(), initialDate, finalDate);
        } catch (org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al insertar el alquile del item", e);
        }
    }

    @Override
    public void updateState(long docu, boolean state) throws PersistenceException {
        try {
            clienteMapper.actualizarEstado(docu,state);
        }catch (org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al actualizar estado",e);
        }
    }


}
