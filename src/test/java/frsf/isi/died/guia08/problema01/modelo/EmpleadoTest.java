package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.ExcepcionPersonalizada.ExcepcionPersonalizada;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;


public class EmpleadoTest {
	private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	
	Empleado e1, e2, e3, e4, e5, e6, e7;
	Tarea t1, t2, t3, t4, t5, t6, t7;
	LocalDate dia1 = LocalDate.of(2020, 05, 30); //Poner el dia siguiente al dia de la fecha(Es decir, setear con la fecha del dia de mañana).
	LocalDate dia2 = LocalDate.of(2020, 06, 02); //Mas de 2 dias de la fecha actual.
	
	@Before
	public void init() throws Exception {
		
		//Empleados.
		e1 = new Empleado(1,"Juan",	Tipo.EFECTIVO, 10.0);
		e2 = new Empleado(1,"Pedro",Tipo.CONTRATADO, 10.0);
		e3 = new Empleado(1,"Maria",Tipo.EFECTIVO, 10.0);
		e4 = new Empleado(4,"Pepe",Tipo.CONTRATADO, 10.0);
		e5 = new Empleado(5,"Paola",Tipo.CONTRATADO, 10.0);
		e6 = new Empleado(6,"Terry",Tipo.CONTRATADO, 10.0);
		e7 = new Empleado(7,"Hugo",Tipo.CONTRATADO, 10.0);
		
		//Tarea 1
		t1 = new Tarea(1, "Tarea1", 4);
		t1.setFechaInicio(LocalDateTime.now());
		t1.setFechaFin(LocalDateTime.of(dia1, LocalTime.now()));	
		
			
		//Tarea 2
		t2 = new Tarea(2, "Tarea2", 4);
		t2.setFechaInicio(LocalDateTime.now());
		t2.setFechaFin(LocalDateTime.of(dia1, LocalTime.now()));
			
		//Tarea 3
		t3 = new Tarea(3, "Tarea3", 5);
		t3.setFechaInicio(LocalDateTime.now());
		t3.setFechaFin(LocalDateTime.of(dia1, LocalTime.now()));
			
		//Tarea 4
		t4 = new Tarea(3, "Tarea3", 5);
		t4.setFechaInicio(LocalDateTime.now());
		t4.setFechaFin(LocalDateTime.of(dia1, LocalTime.now()));
		
		//Tarea 5
		t5 = new Tarea(5, "Tarea5", 4);
		t5.setFechaInicio(LocalDateTime.now());
		t5.setFechaFin(LocalDateTime.of(dia2, LocalTime.now()));
		
		//Tarea 6 --> SIN FECHA DE INICIO NI FIN.
		t6 = new Tarea(6, "Tarea6", 5);
		
		//Tarea 7
		t7 = new Tarea(7, "Tarea7", 5);
		t7.setFechaFin(LocalDateTime.of(dia1, LocalTime.now()));
		
	}
	
	@Test
	//Testea el metodo salario de un empleado del tipo EFECTIVO sin extra.
	public void testSalarioBasicoEfectivo() {
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t1);
		e1.setTareasAsignadas(tareas);
		Double esperado = e1.salario();
		assertEquals(esperado,(Double)40.0);
	};
	
	@Test
	//Testea el metodo salario de un empleado tipo CONTRATADO sin extra.
	public void testSalarioContratadoBasico() {	
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t2);
		e2.setTareasAsignadas(tareas);
		Double esperado = e2.salario();
		assertEquals(esperado,(Double)40.0);
	};
	
	@Test
	//Testea el metodo salario() de un empleado tipo EFECTIVO con extra.
	public void testSalarioEfectivoExtra() {
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t3);
		e3.setTareasAsignadas(tareas);
		Double esperado = e3.salario();
		assertEquals(esperado,(Double)60.0);
	}
	
	@Test
	//Testea el metodo salario() de un empleado tipo CONTRATADO con extra.
	public void testSalarioContratadoExtra() {
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t4);
		e4.setTareasAsignadas(tareas);
		Double esperado = e4.salario();
		assertEquals(esperado,(Double)65.0);
	}
	
	@Test
	//Testea el metodo salario() de un empleado tipo CONTRATADO con descuento.
	public void testSalarioContratadoConDescuento() {
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t5);
		e5.setTareasAsignadas(tareas);
		Double esperado = e5.salario();
		assertEquals(esperado,(Double)30.0);
	}
	
	@Test
	//Testea el metodo costoTarea() cuando la fecha de fin de la tarea es null.
	public void testCostoTareaConFechaFinNull() {
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t6);
		e6.setTareasAsignadas(tareas);
		Double esperado = e6.costoTarea(t6);
		assertEquals(esperado,(Double)50.0);
	}
	
	@Test
	//Testea el metodo costoTarea() cuando la fecha de fin es distinta de null.
	public void testCostoTareaSinFechaFinNull() {
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t4);
		Double esperado = e4.costoTarea(t4);
		assertEquals(esperado,(Double)65.0);
	}
	
	@Test
	//Testea el metodo asignarTarea en un empleado de tipo CONTRATADO con mas de 5 tareasPendientes.
	public void testAsignarTareaEmpleadoContratadoConMasDe5TareasPendientes() throws Exception {
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t1);
		tareas.add(t2);
		tareas.add(t3);
		tareas.add(t4);
		tareas.add(t5);
		tareas.add(t7);
		e7.setTareasAsignadas(tareas);
		boolean esperado = e7.asignarTarea(t1);
		assertFalse(esperado);
		
	}
	
	@Test
	//Testea el metodo asignarTarea() en un empleado de tipo EFECTIVO con mas de 15 horas pendientes.
	public void testAsignarTareaEmpleadoEfectivoConMasDe15HorasPendientes() throws Exception {
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t1);
		tareas.add(t2);
		tareas.add(t3);
		tareas.add(t4);
		e7.setTareasAsignadas(tareas);
		boolean esperado = e7.asignarTarea(t1);
		assertFalse(esperado);
	}

	@Test
	//Testea el metodo asignarTarea() con un empleado donde el predicado puedeAsignarTarea es falso.
	public void testAsignarTareaConPredicadoFalso() throws ExcepcionPersonalizada {
		ExcepcionPersonalizada e = assertThrows(ExcepcionPersonalizada.class, ()->e3.asignarTarea(t6));
		assertEquals(e.getMessage(), "La asignacion es incorrecta. Seleccione otra tarea.");
	}
	
	@Test
	//Testea el metodo comenzar() con una tarea que no existe en la lista del empleado.
	public void testComenzarIntegerExcepcion() throws ExcepcionPersonalizada{
		ExcepcionPersonalizada e = assertThrows(ExcepcionPersonalizada.class, ()->e7.comenzar(15));
		assertEquals(e.getMessage(), "No existe una tarea para comenzar con ese id");
	}

	@Test
	//Testea el metodo comenzar() cuando funciona correctamente.
	public void testComenzarInteger() throws ExcepcionPersonalizada {
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t6); //Tarea sin fecha de inicio.
		e7.setTareasAsignadas(tareas);
		e7.comenzar(6);
		assertEquals(t6.getFechaInicio(), LocalDateTime.now());
	}
	
	@Test
	//Testea el metodo finalizar() cuando no existe una tarea con ese id en la lista de tareas del empleado.
	public void testFinalizarIntegerExcepcion() throws ExcepcionPersonalizada {
		ExcepcionPersonalizada e = assertThrows(ExcepcionPersonalizada.class, ()->e7.finalizar(15));
		assertEquals(e.getMessage(), "No existe una tarea para finalizar con ese id");
	}
	
	@Test
	//Testea el metodo finalizar() cuando funciona correctamente.
	public void testFinalizarInteger() throws ExcepcionPersonalizada {
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t6); //Tarea sin fecha de fin.
		e7.setTareasAsignadas(tareas);
		e7.finalizar(6);
		assertEquals(t6.getFechaFin(), LocalDateTime.now());
	}
	
	@Test
	//Testea el metodo comenzar() con una tarea que no existe en la lista del empleado.
	public void testComenzarIntegerStringExcepcion() throws ExcepcionPersonalizada{
		ExcepcionPersonalizada e = assertThrows(ExcepcionPersonalizada.class, ()->e7.comenzar(15, "30-05-2020 08:00"));
		assertEquals(e.getMessage(), "No existe una tarea para comenzar con ese id");
	}
	
	@Test
	//Testea el metodo comenzar() cuando funciona correctamente.
	public void testComenzarIntegerSting() throws ExcepcionPersonalizada {
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t6); //Tarea sin fecha de inicio.
		e7.setTareasAsignadas(tareas);
		e7.comenzar(6, "30-05-2020 08:00");
		assertEquals(t6.getFechaInicio().format(formato), "30-05-2020 08:00");
	}
	
	
	@Test
	//Testea el metodo finalizar() cuando no existe una tarea con ese id en la lista de tareas del empleado.
	public void testFinalizarIntegerStringExcepcion() throws ExcepcionPersonalizada {
		ExcepcionPersonalizada e = assertThrows(ExcepcionPersonalizada.class, ()->e7.finalizar(15,"30-05-2020 08:00"));
		assertEquals(e.getMessage(), "No existe una tarea para finalizar con ese id");
	}
	
	@Test
	//Testea el metodo finalizar() cuando funciona correctamente.
	public void testFinalizarIntegerSting() throws ExcepcionPersonalizada {
		List<Tarea> tareas = new ArrayList<Tarea>();
		tareas.add(t6); //Tarea sin fecha de inicio.
		e7.setTareasAsignadas(tareas);
		e7.finalizar(6, "30-05-2020 08:00");
		assertEquals(t6.getFechaFin().format(formato), "30-05-2020 08:00");
	}

}
