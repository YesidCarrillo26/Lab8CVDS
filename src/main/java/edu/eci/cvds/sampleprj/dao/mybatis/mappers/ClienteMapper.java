package edu.eci.cvds.sampleprj.dao.mybatis.mappers;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import edu.eci.cvds.samples.entities.Cliente;

/**
 *
 * @author 2106913
 */
public interface ClienteMapper {

    public Cliente consultarCliente(@Param("idcli") long id);

    /**
     * Insertar un cliente
     * @param cli, Objeto cliente a insertar
     */
    public void insertarCliente(@Param("cli") Cliente cli);

    /**
     * Registrar un nuevo item rentado asociado al cliente identificado
     * con 'idc' y relacionado con el item identificado con 'idi'
     * @param id
     * @param idit
     * @param fechainicio
     * @param fechafin
     */
    public void agregarItemRentadoACliente(@Param("idcli") int id,
                                           @Param("iditr") int idit,
                                           @Param("fini") Date fechainicio,
                                           @Param("ffin") Date fechafin);


    public void insertarItemRentado(@Param("cli") long docu,
                                    @Param("idIt") int item,
                                    @Param("iniD") Date initialDate,
                                    @Param("finD") Date finalDate);

    /**
     * Consultar todos los clientes
     * @return
     */
    public List<Cliente> consultarClientes();


    public void actualizarEstado(@Param("cli") long docu,
                                 @Param("vetado") boolean estado);


}
