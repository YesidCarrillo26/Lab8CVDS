package edu.eci.cvds.samples.services;

import com.google.inject.Injector;
import edu.eci.cvds.sampleprj.dao.ClienteDAO;
import edu.eci.cvds.sampleprj.dao.ItemDAO;
//import edu.eci.cvds.sampleprj.dao.TipoItemDAO;
//import edu.eci.cvds.sampleprj.dao.mybatis.MyBATISClienteDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.MyBATISItemDAO;
//import edu.eci.cvds.sampleprj.dao.mybatis.MyBATISTipoItemDAO;
//import edu.eci.cvds.samples.services.impl.ServiciosAlquilerItemsImpl;
import edu.eci.cvds.samples.services.impl.ServiciosAlquilerItemsStub;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
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
                bind(ServiciosAlquiler.class).to(ServiciosAlquilerItemsStub.class);
            }
        });
    }

    private ServiciosAlquilerFactory(){
        optInjector = Optional.empty();
    }

    public ServiciosAlquiler getServiciosAlquiler(){
        if (!optInjector.isPresent()) {
            optInjector = Optional.of(myBatisInjector("development","mybatis-config.xml"));
        }

        return optInjector.get().getInstance(ServiciosAlquiler.class);
    }


    public ServiciosAlquiler getServiciosAlquilerTesting(){
        if (!optInjector.isPresent()) {
            optInjector = Optional.of(myBatisInjector("test","mybatis-config-h2.xml"));
        }

        return optInjector.get().getInstance(ServiciosAlquiler.class);
    }


    public static ServiciosAlquilerFactory getInstance(){
        return instance;
    }

}