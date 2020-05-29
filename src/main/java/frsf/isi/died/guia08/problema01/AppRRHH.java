package frsf.isi.died.guia08.problema01;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import frsf.isi.died.guia08.problema01.ExcepcionPersonalizada.ExcepcionPersonalizada;
import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;

public class AppRRHH {

	private List<Empleado> empleados;
	
	
	//CONSTRUCTORES
	public AppRRHH() {
		empleados = new ArrayList<Empleado>();
	}
	
	public AppRRHH(List<Empleado> lista) {
		empleados = lista;
	}
	
	
	
	//METODOS.
	
	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) throws ExcepcionPersonalizada {
		Empleado a = new Empleado(cuil, nombre, Tipo.CONTRATADO, costoHora);
		
		//Debo verificar si el empleado existe previamente.
		if(this.buscarEmpleado(emp -> emp.getCuil().equals(cuil)).isPresent()) {
			throw new ExcepcionPersonalizada("El empleado ya existe en la lista de empleados.");
		}
		else {
			empleados.add(a);
		}
	}
	
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) throws ExcepcionPersonalizada {
		Empleado a = new Empleado(cuil, nombre, Tipo.EFECTIVO, costoHora);
		
		//Debo verificar si el empleado existe previamente.
		if(this.buscarEmpleado(emp -> emp.getCuil().equals(cuil)).isPresent()) {
			throw new ExcepcionPersonalizada("El empleado ya existe en la lista de empleados.");
		}
		else {
			
			empleados.add(a);
		}
	}
	
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada) throws Exception{
		//Primero debo buscar si la tarea que se quiere asignar existe previamente.
		Optional<Tarea> tareaExistente = this.buscarTarea(t -> t.getId().equals(idTarea));
		if(tareaExistente.isPresent()) {
			throw new ExcepcionPersonalizada("Ya existe una tarea con ese id");
		}
		else {
			//Si no existe una tarea registrada previamente con ese id, busco el empleado.
			Optional<Empleado> b = this.buscarEmpleado(e -> e.getCuil().equals(cuil));
			if(b.isPresent()) {
				Tarea t = new Tarea(idTarea, descripcion, duracionEstimada);
				t.setFechaInicio(LocalDateTime.now());
				t.setFechaFin(LocalDateTime.of(2020, 06, 04, 8, 00));
				try {
					(b.get()).asignarTarea(t);
					t.asignarEmpleado(b.get());
				} catch (ExcepcionPersonalizada e1) {
					e1.getMessage();
				}
			}
			else {
				System.out.println("No existe dicho empleado");
			}	
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

	public void cargarEmpleadosContratadosCSV(String nombreArchivo) throws FileNotFoundException, IOException, ExcepcionPersonalizada{
		try(Reader lector = new FileReader(nombreArchivo)){
			try(BufferedReader entrada = new BufferedReader(lector)){
				String linea = null;
				while((linea = entrada.readLine())!=null) {
					String[] fila = linea.split(";");
					try{
						this.agregarEmpleadoContratado(Integer.valueOf(fila[0]), fila[1], Double.valueOf(fila[2]));
					}
					catch(ExcepcionPersonalizada e) {
						throw new ExcepcionPersonalizada("Error al leer el archivo");
					}
				}
			}
		}
	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) throws FileNotFoundException, IOException, ExcepcionPersonalizada {
		try(Reader lector = new FileReader(nombreArchivo)){
			try(BufferedReader entrada = new BufferedReader(lector)){
				String linea = null;
				while((linea = entrada.readLine())!=null) {
					String[] fila = linea.split(";");
					try{
						this.agregarEmpleadoEfectivo(Integer.valueOf(fila[0]), fila[1], Double.valueOf(fila[2]));
					}
					catch(ExcepcionPersonalizada e) {
						throw new ExcepcionPersonalizada("Error al leer el archivo");
					}
				}
			}
		}
	}

	public void cargarTareasCSV(String nombreArchivo) throws NumberFormatException, Exception{
		try(Reader lector = new FileReader(nombreArchivo)){
			try(BufferedReader entrada = new BufferedReader(lector)){
				String linea = null;
				while((linea = entrada.readLine())!=null) {
					String[] fila = linea.split(";");
					try{
						this.asignarTarea(Integer.valueOf(fila[0]), Integer.valueOf(fila[1]), fila[2], Integer.valueOf(fila[3]));
					}
					catch(ExcepcionPersonalizada e) {
						throw new ExcepcionPersonalizada("Error al leer el archivo");
					}
				}
			}
		}
	}
	
	private void guardarTareasTerminadasCSV() throws IOException {	
		//Lista de tareas terminadas y no facturadas.
		List<Tarea> tareas = this.empleados.stream()
				.map(e -> e.getTareasAsignadas())
				.flatMap(tareasAsignadas -> tareasAsignadas.stream())
				.filter(t -> t.tareaTerminada() && t.getFacturada() == false)
				.collect(Collectors.toList());		
		//Escribir sobre el archivo.
		try(Writer fileWriter= new FileWriter("tareasFinalizadas.csv",true)) {
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				for(Tarea unaTarea: tareas) {
					out.write(unaTarea.asCsv()+ System.getProperty("line.separator"));
				}
			}
		}
	}
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}
	
	private Optional<Tarea> buscarTarea(Predicate<Tarea> p){
		return this.empleados.stream()
				.map(e -> e.getTareasAsignadas())
				.flatMap(tareasAsignadas -> tareasAsignadas.stream())
				.filter(p)
				.findFirst();
	}

	public Double facturar() throws IOException {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}
	
	public void setEmpleados(List<Empleado> lista) {
		this.empleados = lista;
	}
	
	public List<Empleado> getEmpleados() {
		return empleados;
	}
}
