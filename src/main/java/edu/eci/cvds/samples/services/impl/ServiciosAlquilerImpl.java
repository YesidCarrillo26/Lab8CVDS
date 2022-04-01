package edu.eci.cvds.samples.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.eci.cvds.sampleprj.dao.*;

import edu.eci.cvds.sampleprj.dao.ItemRentadoDAO;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.entities.TipoItem;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquiler;
import org.mybatis.guice.transactional.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Singleton
public class ServiciosAlquilerImpl implements ServiciosAlquiler {

    @Inject
    private ItemDAO itemDAO;

    @Inject
    private ClienteDAO clienteDAO;

    @Inject
    private ItemRentadoDAO itemRentadoDAO;

    @Inject
    private TipoItemDAO tipoItemDAO;


    public static final int MULTA_DIARIA=5000;

    @Override
    public int valorMultaRetrasoxDia(int itemId) throws ExcepcionServiciosAlquiler {
        try {
            return (int) itemDAO.load(itemId).getTarifaxDia()+MULTA_DIARIA;
        }catch (PersistenceException ex){
            throw new ExcepcionServiciosAlquiler("Error al consultar el valor de la multa", ex);
        }
    }

    @Override
    public Cliente consultarCliente(long docu) throws ExcepcionServiciosAlquiler {
        try {
            Cliente cliente=  clienteDAO.load(docu);
            if(cliente==null) throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.NO_CLIENT_REGISTRED);
            return cliente;
        }catch (PersistenceException ex){
            throw new ExcepcionServiciosAlquiler("Error al consultar el cliente", ex);
        }
    }

    @Override
    public List<ItemRentado> consultarItemsCliente(long idCliente) throws ExcepcionServiciosAlquiler {
        try {
            consultarCliente(idCliente);
            return itemRentadoDAO.load(idCliente);
        } catch (PersistenceException ex){
            throw new ExcepcionServiciosAlquiler("Error al consultar los items del cliente", ex);
        }
    }

    @Override
    public List<Cliente> consultarClientes() throws ExcepcionServiciosAlquiler {
        try{
            return clienteDAO.loadAll();
        }catch (PersistenceException ex){
            throw new ExcepcionServiciosAlquiler("Error al consultar cliente",ex);
        }
    }

    @Override
    public Item consultarItem(int id) throws ExcepcionServiciosAlquiler {
        try {
            return itemDAO.load(id);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el item "+id,ex);
        }
    }

    @Override
    public List<Item> consultarItemsDisponibles() throws ExcepcionServiciosAlquiler {
        try {
            return itemDAO.load();
        }catch (PersistenceException ex){
            throw new ExcepcionServiciosAlquiler("Error al consultar los items disponibles",ex);
        }
    }

    @Override
    public long consultarMultaAlquiler(int iditem, Date fechaDevolucion) throws ExcepcionServiciosAlquiler {
        try{
            consultarItem(iditem);
            ItemRentado itemRentado = itemRentadoDAO.loadByItem(iditem);
            if (itemRentado == null) throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.NO_ITEM_RENTED);
            LocalDate fechaFinal = itemRentado.getFechafinrenta().toLocalDate();
            long dias = ChronoUnit.DAYS.between(fechaFinal, fechaDevolucion.toLocalDate());
            System.out.println("Test: " + dias);
            if (dias < 0) dias = 0;
            return  dias * valorMultaRetrasoxDia(iditem);
        } catch (PersistenceException ex){
            throw new ExcepcionServiciosAlquiler("Error al consultar la multa del alquiler.", ex);
        }
    }

    @Override
    public TipoItem consultarTipoItem(int id) throws ExcepcionServiciosAlquiler {
        try{
            TipoItem tipo = tipoItemDAO.load(id);
            if (tipo == null) throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.NO_ITEM_TYPE);
            return tipo;
        } catch (PersistenceException ex){
            throw new ExcepcionServiciosAlquiler("Error al consultar el tipo de item", ex);
        }
    }

    @Override
    public List<TipoItem> consultarTiposItem() throws ExcepcionServiciosAlquiler {
        try {
            return tipoItemDAO.loadTiposItems();
        }catch (PersistenceException ex){
            throw new ExcepcionServiciosAlquiler("Error al consultar tipo item");
        }
    }

    @Transactional
    @Override
    public void registrarAlquilerCliente(Date date, long docu, Item item, int numdias) throws ExcepcionServiciosAlquiler {
        try {
            Cliente cliente= consultarCliente(docu);
            consultarItem(item.getId());
            if(numdias<0){throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.INVALID_DAYS);}
            clienteDAO.saveRenting(docu,item,date,Date.valueOf(date.toLocalDate().plusDays(numdias)));
        }catch (PersistenceException ex){
            throw new ExcepcionServiciosAlquiler("Error al registrar el alquiler");
        }
    }

    @Transactional
    @Override
    public void registrarCliente(Cliente c) throws ExcepcionServiciosAlquiler {
        try {
            clienteDAO.save(c);
        }catch (PersistenceException ex){
            throw new ExcepcionServiciosAlquiler("Error al registrar el cliente",ex);
        }
    }

    @Override
    public long consultarCostoAlquiler(int iditem, int numdias) throws ExcepcionServiciosAlquiler {
        try {
            if (itemDAO.load(iditem) == null ){
                throw new ExcepcionServiciosAlquiler("El Item no existe");
            }
            long tarifa = itemDAO.load(iditem).getTarifaxDia();
            return tarifa * numdias;
        } catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("Error al consultar alquiler"+iditem, e);
        }
    }

    @Transactional
    @Override
    public void actualizarTarifaItem(int id, long tarifa) throws ExcepcionServiciosAlquiler {
        try {
            consultarItem(id);
            if(tarifa<0){throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.INVALID_RATE);}
            itemDAO.actualizarTarifa(id,tarifa);
        }catch (PersistenceException ex){
            throw new ExcepcionServiciosAlquiler("error al actualizar la tarifa del item",ex);
        }
    }


    @Override
    public void registrarItem(Item i) throws ExcepcionServiciosAlquiler {
        try {
            itemDAO.save(i);
        }catch (PersistenceException ex){
            throw new ExcepcionServiciosAlquiler("Error al registrar el item",ex);
        }
    }

    @Transactional
    @Override
    public void vetarCliente(long docu, boolean estado) throws ExcepcionServiciosAlquiler {
        try{
            consultarCliente( docu );
            clienteDAO.updateState(docu,estado);
        }
        catch(PersistenceException persistenceException){
            throw new ExcepcionServiciosAlquiler("Error al vetar al cliente con id: " + docu ,persistenceException);
        }
    }



}
