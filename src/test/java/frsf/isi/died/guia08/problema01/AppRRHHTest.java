package frsf.isi.died.guia08.problema01;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.ExcepcionPersonalizada.ExcepcionPersonalizada;
import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class AppRRHHTest {
	private final static DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	Empleado e1, e2, e3, e4;
	Tarea t1, t2;
	
	@Before
	public void init() throws Exception {
		
		LocalDate dia1 = LocalDate.of(2020, 06, 02);
		
		AppRRHH app = new AppRRHH();
		
		e1 = new Empleado(1,"Juan",	Tipo.CONTRATADO, 10.0);
		e2 = new Empleado(2,"Pedro",Tipo.EFECTIVO, 10.0);
		e3 = new Empleado(3,"Maria",Tipo.EFECTIVO, 10.0);
		e4 = new Empleado(4,"Pepe",Tipo.CONTRATADO, 10.0);
		
		//Tarea 1
		t1 = new Tarea(1, "Tarea1", 4);
		t1.setFechaInicio(LocalDateTime.now());
		t1.setFechaFin(LocalDateTime.of(dia1, LocalTime.now()));
		
		//Tarea 2
		t2 = new Tarea(2, "Tarea2", 4);
		e1.asignarTarea(t1);
		
		List<Empleado> lista = new ArrayList<Empleado>();
		List<Tarea> tareas = new ArrayList<Tarea>();
		
		tareas.add(t2); //Tarea sin fechas.
		e3.setTareasAsignadas(tareas);
	}
	
	@Test
	//Testea el metodo agregarEmpleadoContratado() cuando el empleado que quiero agregar ya existe en la lista de empleados.
	public void testAgregarEmpleadoContratadoExcepcion() throws ExcepcionPersonalizada {
		AppRRHH app = new AppRRHH();
		List<Empleado> lista = new ArrayList<Empleado>();
		lista.add(e1);
		lista.add(e2);
		app.setEmpleados(lista);
		ExcepcionPersonalizada e = assertThrows(ExcepcionPersonalizada.class, ()->app.agregarEmpleadoContratado(1, "Juan", 10.0));
		assertEquals(e.getMessage(), "El empleado ya existe en la lista de empleados.");
	}
	
	@Test
	//Testea el metodo agregarEmpleadoContratado() cuando funciona correctamente.
	public void testAgregarEmpleadoContratado() throws ExcepcionPersonalizada {
		AppRRHH app = new AppRRHH();
		List<Empleado> lista = new ArrayList<Empleado>(); //Lista vacia.
		app.agregarEmpleadoContratado(2, "Pedro", 10.0);
		boolean esperado = app.getEmpleados().isEmpty();
		assertFalse(esperado);
	}
	
	@Test
	//Testea el metodo agregarEmpleadoEfectivo() cuando el empleado que quiero agregar ya existe en la lista de empleados.
	public void testAgregarEfectivoExcepcion() throws ExcepcionPersonalizada {
		AppRRHH app = new AppRRHH();
		List<Empleado> lista = new ArrayList<Empleado>();
		lista.add(e1);
		lista.add(e2);
		app.setEmpleados(lista);
		ExcepcionPersonalizada e = assertThrows(ExcepcionPersonalizada.class, ()->app.agregarEmpleadoEfectivo(2, "Pedro", 10.0));
		assertEquals(e.getMessage(), "El empleado ya existe en la lista de empleados.");
	}
	
	@Test
	//Testea el metodo agregarEmpleadoEfecto() cuando funciona correctamente.
	public void testAgregarEmpleadoEfectivo() throws ExcepcionPersonalizada {
		AppRRHH app = new AppRRHH();
		List<Empleado> lista = new ArrayList<Empleado>(); //Lista vacia.
		app.agregarEmpleadoContratado(1, "Juan", 10.0);
		boolean esperado = app.getEmpleados().isEmpty();
		assertFalse(esperado);
	}
	
	@Test
	//Testea el metodo asignarTarea() cuando la tarea ya existe asignada previamente.
	public void testAsignarTareaYaAsignada() throws ExcepcionPersonalizada{
		AppRRHH app = new AppRRHH();
		List<Empleado> lista = new ArrayList<Empleado>();
		lista.add(e1);
		lista.add(e2);
		app.setEmpleados(lista);
		ExcepcionPersonalizada e = assertThrows(ExcepcionPersonalizada.class, ()->app.asignarTarea(1, 1, "Tarea1", 5));
		assertEquals(e.getMessage(), "Ya existe una tarea con ese id");
	}
	
	@Test
	//Testea el metodo asignarTarea() cuando funciona correctamente.
	public void testAsignarTarea() throws Exception{
		AppRRHH app = new AppRRHH();
		List<Empleado> lista = new ArrayList<Empleado>();
		lista.add(e2);
		app.setEmpleados(lista);
		app.asignarTarea(2, 3, "Tarea3", 5);
		Optional<Tarea> tarea = app.getEmpleados().stream()
				.map(e -> e.getTareasAsignadas())
				.flatMap(t -> t.stream())
				.filter(t -> t.getId().equals(3))
				.findFirst();
		boolean esperado = tarea.isPresent();
		assertTrue(esperado);
	}
	
	
	@Test
	//Testea el metodo comenzarTarea() cuando funciona correctamente.
	public void testComenzarTarea() {
		AppRRHH app = new AppRRHH();
		List<Empleado> lista = new ArrayList<Empleado>();
		lista.add(e3);
		app.setEmpleados(lista);
		app.empezarTarea(3, 2);
		boolean esperado = app.getEmpleados().stream()
				.map(e -> e.getTareasAsignadas())
				.flatMap(t -> t.stream())
				.filter(t -> t.getId().equals(2))
				.findFirst().get().tareaEmpezada(); 
		assertTrue(esperado);
		
	}
	
	@Test
	//Testea el metodo FinalizarTarea() cuando funciona correctamente.
	public void testFinalizarTarea() {
		AppRRHH app = new AppRRHH();
		List<Empleado> lista = new ArrayList<Empleado>();
		lista.add(e3);
		app.setEmpleados(lista);
		app.terminarTarea(3, 2);
		boolean esperado = app.getEmpleados().stream()
				.map(e -> e.getTareasAsignadas())
				.flatMap(t -> t.stream())
				.filter(t -> t.getId().equals(2))
				.findFirst().get().tareaPendiente();
		assertTrue(esperado);
	}
	

}
