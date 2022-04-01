package edu.eci.cvds.sampleprj.dao.mybatis.mappers;

import edu.eci.cvds.samples.entities.ItemRentado;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemRentadoMapper{


    public List<ItemRentado> consultarItemsRentados();


    public List<ItemRentado> consultarItemsRentadosCliente(@Param("idIt") long id);

    public ItemRentado consultarPorItemRentado(@Param("idIt") int id);
}
