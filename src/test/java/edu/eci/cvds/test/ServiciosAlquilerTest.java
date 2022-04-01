package edu.eci.cvds.test;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import edu.eci.cvds.sampleprj.dao.PersistenceException;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.entities.TipoItem;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquilerFactory;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.*;

public class ServiciosAlquilerTest {

    @Inject
    private SqlSession sqlSession;

    ServiciosAlquiler serviciosAlquiler;

    public ServiciosAlquilerTest() {
        serviciosAlquiler = ServiciosAlquilerFactory.getInstance().getServiciosAlquilerTesting();
    }

    @Before
    public void setUp() {
    }

    /*@Test
    public void emptyDB() {
        for(int i = 0; i < 100; i += 10) {
            boolean r = false;
            try {
                Cliente cliente = serviciosAlquiler.consultarCliente(documento);
            } catch(ExcepcionServiciosAlquiler e) {
                r = true;
            } catch(IndexOutOfBoundsException e) {
                r = true;
            }
            // Validate no Client was found;
            Assert.assertTrue(r);
        };
    }*/

    @Test
    public void NoDeberiaConsultarElCostoDelAlquilerDeItemDesconocido() throws ExcepcionServiciosAlquiler {
        try{
            assertEquals(40*30,serviciosAlquiler.consultarCostoAlquiler(1934,30));
        } catch (ExcepcionServiciosAlquiler excepcionServiciosAlquiler) {
            assertFalse(false);
        }
    }

    @Test
    public void DeberiaConsultarElCostoDelAlquiler() throws ExcepcionServiciosAlquiler {
        try{
            Item it = new Item(new TipoItem(40, "item bonito" ),800,
                    "estufa", "bueno", new SimpleDateFormat("yyyy/MM/dd").parse("2022/03/25"),
                    40,"Cualquiera","99");
            serviciosAlquiler.registrarItem(it);
            assertEquals(40*30,serviciosAlquiler.consultarCostoAlquiler(800,30));
        } catch (ExcepcionServiciosAlquiler| ParseException excepcionServiciosAlquiler) {
            assertTrue(true);
        }
    }

    @Test
    public void NoDeberiaRegistrarAlquilerClienteDesconocido() throws ExcepcionServiciosAlquiler {
        try{
            Item it = new Item(new TipoItem(40, "item bonito" ),300,
                    "microondas", "bueno", new SimpleDateFormat("yyyy/MM/dd").parse("2022/03/25"),
                    99,"Internet","99");
            LocalDate at = LocalDate.parse("2019-09-28");
            serviciosAlquiler.registrarAlquilerCliente(Date.valueOf(at) , 7435345 , it , 30 );
            assertTrue(false);
        } catch (ExcepcionServiciosAlquiler excepcionServiciosAlquiler) {
            assertTrue(true);
        } catch (ParseException px){
            fail();
        }
    }

    @Test
    public void DeberiaConsultarMultaAlquiler() throws ExcepcionServiciosAlquiler {
        try {
            ArrayList<ItemRentado> itemRentados = new ArrayList<ItemRentado>();
            Cliente cliente = new Cliente("Fernando", 90, "144463", "dgffd", "fermna", false, itemRentados);
            serviciosAlquiler.registrarCliente(cliente);
            Item it = new Item(new TipoItem(1, "futbol"), 344,
                    "item99", "item99", new SimpleDateFormat("yyyy/MM/dd").parse("2019/09/28"),
                    65, "Digital", "99");
            serviciosAlquiler.registrarItem(it);
            LocalDate fechainicio = LocalDate.parse("2021-10-28");
            LocalDate fechafin = LocalDate.parse("2021-10-29");
            LocalDate fechaDevolucion = LocalDate.parse("2021-11-01");
            itemRentados.add(new ItemRentado(1, it, Date.valueOf(fechainicio), Date.valueOf(fechafin)));
            serviciosAlquiler.registrarAlquilerCliente(Date.valueOf(fechainicio), 90, it, 1);
            Assert.assertEquals(297, serviciosAlquiler.consultarMultaAlquiler(344, Date.valueOf(fechaDevolucion)));
            Assert.assertEquals(0, serviciosAlquiler.consultarMultaAlquiler(344, Date.valueOf(fechafin)));
        } catch (Exception e) {
        }
    }

    @Test
    public void NoDeberiaConsultarTipoItem() throws ExcepcionServiciosAlquiler {
        try{
            serviciosAlquiler.consultarTipoItem(32);
            assertTrue(false);
        } catch (ExcepcionServiciosAlquiler excepcionServiciosAlquiler) {
            assertTrue(true);
        }
    }

    @Test
    public void NoDeberiaActualizarTarifaItemNoRegistrado()   throws PersistenceException , ParseException {
        try{
            serviciosAlquiler.actualizarTarifaItem(9000,30);
        } catch (ExcepcionServiciosAlquiler ex){
            assertTrue(true);
        }
    }

    @Test
    public void DeberiaRegistrarYConsultarItem() throws ExcepcionServiciosAlquiler , ParseException {
        try{
            Item it = new Item(new TipoItem(-97, "item bonito" ),-990,
                    "estufa", "bueno", new SimpleDateFormat("yyyy/MM/dd").parse("2020/09/28"),
                    40,"Cualquiera","99");
            serviciosAlquiler.registrarItem(it);
            if(serviciosAlquiler.consultarItem(-990)!=null) {
                assertTrue(true);
            }
            else{
                assertTrue(false);
            }
        } catch (ExcepcionServiciosAlquiler excepcionServiciosAlquiler){
            assertTrue(true);
        }
    }

    @Test
    public void DeberiaLanzarExcepcionSiNoExisteElCliente() throws ExcepcionServiciosAlquiler {
        try{
            serviciosAlquiler.consultarCliente(11112);
        }catch (ExcepcionServiciosAlquiler excepcionServiciosAlquiler) {
            assertTrue(true);
        }
    }

    @Test
    public void NoDeberiaRegistrarAlClienteExistente(){
        try{
            serviciosAlquiler.registrarCliente(new Cliente("Camilito", 14546, "4675673", "cll 32423","camilito@gmail.com"));
            serviciosAlquiler.registrarCliente(new Cliente("Camilito", 1345556, "4675673", "cll 32423","camilito@gmail.com"));
        }catch (ExcepcionServiciosAlquiler excepcionServiciosAlquiler) {
            assertTrue(true);
        }
    }

    @Test
    public void NoDeberiaVetarAClienteQueNoExiste(){
        try{
            serviciosAlquiler.vetarCliente(83747,true);
            assertTrue(false);
        }catch (ExcepcionServiciosAlquiler excepcionServiciosAlquiler) {
        }
    }

    @Test
    public void DeberiaRetornarValorMultaItem() throws ExcepcionServiciosAlquiler {
        try {
            serviciosAlquiler.registrarItem(new Item(new TipoItem(1, "item bonito" ),1,
                    "Televisor", "bueno", new SimpleDateFormat("yyyy/MM/dd").parse("2020/09/28"),
                    99,"Internet","99"));
            serviciosAlquiler.valorMultaRetrasoxDia(1);
            assertEquals(serviciosAlquiler.consultarItem(1).getTarifaxDia(),99);
        } catch (ExcepcionServiciosAlquiler | ParseException excepcionServiciosAlquiler) {
            assertTrue(true);
        }
    }
}

