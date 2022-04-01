package edu.eci.cvds.samples.services;

import com.google.inject.Injector;
import edu.eci.cvds.sampleprj.dao.ClienteDAO;
import edu.eci.cvds.sampleprj.dao.ItemDAO;
import edu.eci.cvds.sampleprj.dao.ItemRentadoDAO;
import edu.eci.cvds.sampleprj.dao.TipoItemDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.MyBATISClienteDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.MyBATISItemDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.MyBATISItemRentadoDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.MyBATISTipoItemDAO;
import edu.eci.cvds.samples.services.impl.ServiciosAlquilerImpl;
import org.mybatis.guice.XMLMyBatisModule;

import java.util.Optional;

import static com.google.inject.Guice.createInjector;

public class ServiciosAlquilerFactory {

    private static ServiciosAlquilerFactory instance = new ServiciosAlquilerFactory();

    private static Optional<Injector> optInjector;

    private Injector myBatisInjector(String env, String pathResource) {
        return createInjector(new XMLMyBatisModule() {
            @Override
            protected void initialize() {
                setEnvironmentId(env);
                setClassPathResource(pathResource);
                bind(ItemDAO.class).to(MyBATISItemDAO.class);
                bind(TipoItemDAO.class).to(MyBATISTipoItemDAO.class);
                bind(ClienteDAO.class).to(MyBATISClienteDAO.class);
                bind(ItemRentadoDAO.class).to(MyBATISItemRentadoDAO.class);
                bind(ServiciosAlquiler.class).to(ServiciosAlquilerImpl.class);
            }
        });
    }

    private ServiciosAlquilerFactory(){
        optInjector = Optional.empty();
    }

    public edu.eci.cvds.samples.services.ServiciosAlquiler getServiciosAlquiler(){
        if (!optInjector.isPresent()) {
            optInjector = Optional.of(myBatisInjector("development","mybatis-config.xml"));
        }

        return optInjector.get().getInstance(edu.eci.cvds.samples.services.ServiciosAlquiler.class);
    }


    public edu.eci.cvds.samples.services.ServiciosAlquiler getServiciosAlquilerTesting(){
        if (!optInjector.isPresent()) {
            optInjector = Optional.of(myBatisInjector("test","mybatis-config-h2.xml"));
        }

        return optInjector.get().getInstance(edu.eci.cvds.samples.services.ServiciosAlquiler.class);
    }


    public static ServiciosAlquilerFactory getInstance(){
        return instance;
    }

    public static void main(String[] args) throws ExcepcionServiciosAlquiler{
        System.out.println("-------------------------------- CONSULAR CLIENTES --------------------------------");
        System.out.println(instance.getServiciosAlquiler().consultarClientes().toString());
        long documento = 10999900099L;
        /*
        System.out.println("---------------------------------- CONSULAR CLIENTE " + documento + " ------------------------------------");
        System.out.println(instance.getServiciosAlquiler().consultarCliente(documento).toString());
        System.out.println("----------------------------- CONSULAR ITEMS DEL CLIENTE " + documento +" -----------------------------");
        System.out.println(instance.getServiciosAlquiler().consultarItemsCliente(10999900099L).toString());*/
    }
}