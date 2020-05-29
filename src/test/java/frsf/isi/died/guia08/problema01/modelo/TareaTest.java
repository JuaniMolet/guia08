package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.ExcepcionPersonalizada.ExcepcionPersonalizada;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;




public class TareaTest {
	
	Empleado e1, e2, e3;
	Tarea t1, t2, t3;
	
	LocalDate dia1 = LocalDate.of(2020, 05, 30); //Poner el dia siguiente al dia de la fecha(Es decir, setear con la fecha del dia de mañana).
	
	@Before
	public void init() throws Exception {
	
		e1 = new Empleado(1,"Juan",	Tipo.EFECTIVO, 10.0);
		e2 = new Empleado(1,"Pedro",Tipo.CONTRATADO, 10.0);
		
		//Tarea 1
		t1 = new Tarea(1, "Tarea1", 4);
		t1.setFechaInicio(LocalDateTime.now());
		t1.setFechaFin(LocalDateTime.of(dia1, LocalTime.now()));
		t1.asignarEmpleado(e1);
		
		//Tarea 2
		t2 = new Tarea(2, "Tarea2", 4);
		t2.setFechaInicio(LocalDateTime.now());
		
		//Tarea 3
		t3 = new Tarea(3, "Tarea3", 4);
		t3.setFechaInicio(LocalDateTime.now());
		t3.setFechaFin(LocalDateTime.of(dia1, LocalTime.now()));
		
	}
	
	@Test
	//Testea el metodo asignarEmpleado() cuando la tarea ya tiene otro empleado asignado.
	public void testAsignarEmpleadoConOtroEmpleadoAsignadoPreviamente() {
		ExcepcionPersonalizada e = assertThrows(ExcepcionPersonalizada.class, ()->t1.asignarEmpleado(e2));
		assertEquals(e.getMessage(), "Esta tarea ya tiene un empleado asignado");
	}
	
	@Test
	//Testea el metodo asignarEmpleado() cuando la tarea ya ha finalizado.
	public void testAsignarEmpleadoConTareaFinalizada() {
		ExcepcionPersonalizada e = assertThrows(ExcepcionPersonalizada.class, ()->t2.asignarEmpleado(e1));
		assertEquals(e.getMessage(), "Esta tarea ya ha finalizado");
	}
	
	@Test
	//Testea el metodo asignarEmpleado() cuando funciona correctamente.
	public void testAsignarEmpleado() throws ExcepcionPersonalizada {
		t3.asignarEmpleado(e1);
		assertEquals(e1, t3.getEmpleadoAsignado());
	}

}
