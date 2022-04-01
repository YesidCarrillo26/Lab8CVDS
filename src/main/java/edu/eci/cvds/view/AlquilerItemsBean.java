package edu.eci.cvds.view;

import com.google.inject.Inject;

import edu.eci.cvds.samples.entities.*;
import edu.eci.cvds.samples.services.ServiciosAlquiler;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;

import java.util.List;
import java.util.ArrayList;

import java.sql.Date;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext; 
import javax.faces.application.FacesMessage; 

@ManagedBean(name = "AlquilerBean")
@SessionScoped
public class AlquilerItemsBean extends BasePageBean {

    @Inject
    private ServiciosAlquiler serviciosAlquiler;
    private Cliente cliente;
    private List<Cliente> added;
    private long costoAlquiler;

    /**
     * Retorna una lista con la informacion de los clientes almacenados en la base de datos
     * @return todos los clientes de la base de datos
     * @throws ExcepcionServiciosAlquiler
     */
    public AlquilerItemsBean(){
	added = new ArrayList<Cliente>();
	costoAlquiler=0;
    }

    public List<Cliente> consultarClientes() throws ExcepcionServiciosAlquiler{
        try {
	    List<Cliente> clientes = new ArrayList<Cliente>();
	    clientes.addAll(added);
	    clientes.addAll(serviciosAlquiler.consultarClientes());
            return clientes;   
        } catch (Exception e) {
            throw new ExcepcionServiciosAlquiler("Error al consultar la tabla Clientes");
        }
    }

    /**
      * Registra un cliente nuevo en la base de datos
      * @param Nombre nombre del nuevo cliente
      * @param Documeto documento de identidad del nuevo cliente
      * @param Telefono telefono del nuevo cliente
      * @param Direccion direccion del nuevo cliente
      * @param Email email del nuevo cliente
      * 
      */
    public void registrar(String nombre, long documento, String telefono, String direccion, String email) throws ExcepcionServiciosAlquiler{
	try{	
		Cliente nuevo = new Cliente(nombre,documento,telefono,direccion,email);
		serviciosAlquiler.registrarCliente(nuevo);
		added.add(nuevo);
	}catch(Exception e){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error ", "Ya se encuentra registrado un cliente con documento '" + documento + "'"));
	}
    }

    public void setCliente(Cliente cliente){
	this.cliente = cliente;
    }
    
    public Cliente getCliente(){
	return this.cliente;
    }

    /**
     * Retorna una lista con la informacion de los items rentados por un cliente dado
     * @param documento documento del cliente solicitado
     * @return lista con la informacion de los items rentados por un cliente dado
     */
    public List<ItemRentado> consultarItemsCliente(Cliente cliente) throws ExcepcionServiciosAlquiler{
        return serviciosAlquiler.consultarItemsCliente(cliente.getDocumento());
    }

    /**
    * Registra un nuevo alquiler con el item y numero de dias especificados
    * @param idItem identificador unico del item deseado
    * @param numDays numero de dias por los cuales se solicita el item
    */
    public void registrarAlquiler(int idItem , int numDays) throws ExcepcionServiciosAlquiler {
	try{
        	Item item = serviciosAlquiler.consultarItem(idItem);
        	serviciosAlquiler.registrarAlquilerCliente(new Date(System.currentTimeMillis()),cliente.getDocumento(),item,numDays);
	}catch(Exception e){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error ",e.getMessage()));
	}
    }

    public void setCostoAlquiler(long costoAlquiler){
	this.costoAlquiler = costoAlquiler;
    }
    
    public long getCostoAlquiler(){
	return costoAlquiler;
    }

    /**
    * Consulta los costos de rentar un item con base en el numero de dias dado
    * @param idItem id del item a rentar
    * @param nDays numero de dias a rentar
    */
    public void consultarCosto(String iditem , String nDays) throws ExcepcionServiciosAlquiler {
    	costoAlquiler = serviciosAlquiler.consultarCostoAlquiler(Integer.parseInt(iditem), Integer.parseInt(nDays));
    }

    /**
    * Consulta los costos de rentar un item con base en el numero de dias dado
    * @param idItem id del item a rentar
    * @param nDays numero de dias a rentar
    */
    public void consultarCosto(int iditem , int nDays) throws ExcepcionServiciosAlquiler {
	costoAlquiler=0;
	try{
    		costoAlquiler = serviciosAlquiler.consultarCostoAlquiler(iditem, nDays);
		System.out.println(costoAlquiler);
	}catch(Exception e){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error ",e.getMessage()));
	}
    }

    /**
    * Consulta la multa asociada a un item dado
    * @param idItem id del item a consultar
    */
    public long consultarMulta(int iditem) throws ExcepcionServiciosAlquiler {
	return serviciosAlquiler.consultarMultaAlquiler(iditem, new Date(System.currentTimeMillis()));
    }
}