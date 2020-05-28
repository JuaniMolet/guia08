package frsf.isi.died.guia08.problema01;


import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import frsf.isi.died.guia08.problema01.ExcepcionPersonalizada.ExcepcionPersonalizada;
import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;

public class AppRRHH {

	private List<Empleado> empleados;
	
	
	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) {
		Empleado a = new Empleado(cuil, nombre, Tipo.CONTRATADO, costoHora);
		empleados.add(a);
	}
	
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) {
		Empleado a = new Empleado(cuil, nombre, Tipo.EFECTIVO, costoHora);
		empleados.add(a);
	}
	
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada){
		Optional<Empleado> b = this.buscarEmpleado(e -> e.getCuil().equals(cuil));
		if(b.isPresent()) {
			Tarea t = new Tarea(idTarea, descripcion, duracionEstimada);
			try {
				t.asignarEmpleado(b.get());
			} catch (ExcepcionPersonalizada e1) {
				System.out.println(e1.getMessage());
			}
		}
		else {
			System.out.println("No existe dicho empleado");
		}	
	}
	
	public void empezarTarea(Integer cuil,Integer idTarea) {
		Optional<Empleado> b = this.buscarEmpleado(e -> e.getCuil().equals(cuil));
		if(b.isPresent()) {
			try {
				b.get().comenzar(idTarea);
			} catch (ExcepcionPersonalizada e1) {
				System.out.println(e1.getMessage());
			}
		}
		else {
			System.out.println("No existe dicho empleado");
		}
	}
	
	public void terminarTarea(Integer cuil,Integer idTarea) {		
		Optional<Empleado> b = this.buscarEmpleado(e -> e.getCuil().equals(cuil));
		if(b.isPresent()) {
			try {
				b.get().finalizar(idTarea);
			} catch (ExcepcionPersonalizada e1) {
				System.out.println(e1.getMessage());
			}
		}
		else {
			System.out.println("No existe dicho empleado");
		}
	}

	public void cargarEmpleadosContratadosCSV(String nombreArchivo) {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado		
	}

	public void cargarTareasCSV(String nombreArchivo) {
		// leer datos del archivo
		// cada fila del archivo tendrá:
		// cuil del empleado asignado, numero de la taera, descripcion y duración estimada en horas.
	}
	
	private void guardarTareasTerminadasCSV() {
		// guarda una lista con los datos de la tarea que fueron terminadas
		// y todavía no fueron facturadas
		// y el nombre y cuil del empleado que la finalizó en formato CSV 
	}
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}
}
