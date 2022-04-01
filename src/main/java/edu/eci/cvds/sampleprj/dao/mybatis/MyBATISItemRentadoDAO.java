package edu.eci.cvds.sampleprj.dao.mybatis;

import com.google.inject.Inject;
import edu.eci.cvds.sampleprj.dao.ItemRentadoDAO;
import edu.eci.cvds.sampleprj.dao.PersistenceException;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ItemRentadoMapper;
import edu.eci.cvds.samples.entities.ItemRentado;

import java.util.List;

public class MyBATISItemRentadoDAO implements ItemRentadoDAO {

    @Inject
    private ItemRentadoMapper itemRentadoMapper;


    @Override
    public List<ItemRentado> load(long idClient) throws PersistenceException {
        try{
            return itemRentadoMapper.consultarItemsRentadosCliente(idClient);
        } catch (org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al consultar los items rentados por el cliente ", e);
        }
    }

    @Override
    public ItemRentado loadByItem(int idItem) throws PersistenceException {
        try{
            return itemRentadoMapper.consultarPorItemRentado(idItem);
        } catch(org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al consultar item rentado por el id del item" , e);
        }
    }


}
