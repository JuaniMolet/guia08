package frsf.isi.died.guia08.problema01.modelo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.function.Predicate;

import frsf.isi.died.guia08.problema01.ExcepcionPersonalizada.ExcepcionPersonalizada;

public class Empleado {

	private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	
	public enum Tipo {CONTRATADO,EFECTIVO}; 
	
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	private Function<Tarea, Double> calculoPagoPorTarea;		
	private Predicate<Tarea> puedeAsignarTarea;

	
	
	//CONSTRUCTORES.
	
	public Empleado() {
		this.tareasAsignadas = new ArrayList<Tarea>();
		this.puedeAsignarTarea = (Tarea t) -> t.getEmpleadoAsignado() == null && t.tareaPendiente() == true;
	}
	
	
	public Empleado(Integer cuil, String nombre, Tipo tipo, Double costoHora) {
		
		super();
		this.tareasAsignadas = new ArrayList<Tarea>();
		this.cuil = cuil;
		this.nombre = nombre;
		this.tipo = tipo;
		this.costoHora = costoHora;	
		this.puedeAsignarTarea = t -> t.getEmpleadoAsignado() == null && t.tareaPendiente() == true;
		
		if(this.tipo == Tipo.EFECTIVO) {
			this.calculoPagoPorTarea = t -> {	
				if(t.terminoAntes()) {
					return this.costoBase(t)*1.2;
				}
				else {
					return this.costoBase(t);
				}
			};
		}
		else if(this.tipo == Tipo.CONTRATADO) {
			this.calculoPagoPorTarea = t -> {
				if(t.terminoAntes() == true) {
					return this.costoBase(t)*1.3;
				}
				else if(t.terminoMasDeDosDiasTarde() == true) {
					return this.costoBase(t)*0.75;
				}
				return costoBase(t);
			};
		}
	}

	//METODOS 
	
	//CONTAR TAREAS PENDIENTES.
	public int cantidadTareasPendientes() {
		int contador = 0;
		for(Tarea unaTarea : tareasAsignadas) {
			if(unaTarea.tareaPendiente() == true) {
				contador++;
			}
		}
		return contador;
	}
	
	//CALCULAR EL TOTAL DE HORAS ESTIMADAS DE UN EMPLEADO.
	public int sumaDuracionEstimada() {
		int contador = 0;
		for(Tarea unaTarea : tareasAsignadas) {
			contador += unaTarea.getDuracionEstimada();
		}
		return contador;
	}
	
	public Double salario() {
		// cargar todas las tareas no facturadas
		// calcular el costo
		// marcarlas como facturadas.
		Optional<Double> salario = tareasAsignadas.stream()
				.filter(t -> t.getFacturada() == false)
				.map(t -> t.facturar())
				.map(t -> this.costoTarea(t))
				.reduce((acum, ant) -> {return acum+ant;});

		return salario.orElse(0.0);
	}
	
	/**
	 * Si la tarea ya fue terminada nos indica cuaal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 */
	public Double costoTarea(Tarea t) {
		if(t.getFechaFin() != null) {
			return calculoPagoPorTarea.apply(t);
		}
		else {
			return this.costoBase(t);
		}
	}
	
	//DEVUELVE EL COSTO BASE QUE GANA EL EMPLEADO.
	public Double costoBase(Tarea t) {
		return t.getDuracionEstimada()*this.costoHora;
	}
		
	//ASIGNA UNA TAREA A UN EMPLEADO.
	public Boolean asignarTarea(Tarea t) throws ExcepcionPersonalizada {
		switch(this.getTipo()) {
			case CONTRATADO:
				if(this.cantidadTareasPendientes() > 5) {
					return false;
				}
			case EFECTIVO:
				if(this.sumaDuracionEstimada() > 15) {
					return false;
				}
		}
		if(puedeAsignarTarea.test(t)) {
					this.tareasAsignadas.add(t);
					t.asignarEmpleado(this);
					return true;
		}
		else {
				throw new ExcepcionPersonalizada("La asignacion es incorrecta. Seleccione otra tarea.");
		}
	}
	
	public void comenzar(Integer idTarea) throws ExcepcionPersonalizada{
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de inicio la fecha y hora actual
		boolean bandera = false;
		for(Tarea unaTarea : this.tareasAsignadas) {
			if(unaTarea.getId() == idTarea) {
				unaTarea.setFechaInicio(LocalDateTime.now());
				bandera = true;
			}
		}
		if(bandera = false) {
			throw new ExcepcionPersonalizada("No existe una tarea para comenzar con ese id");
		}
		
	}
	
	public void finalizar(Integer idTarea) throws ExcepcionPersonalizada {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		boolean bandera = false;
		for(Tarea unaTarea : this.tareasAsignadas) {
			if(unaTarea.getId() == idTarea) {
				unaTarea.setFechaFin(LocalDateTime.now());
				bandera=true;
			}
		}
		if(bandera == false) {
			throw new ExcepcionPersonalizada("No existe una tarea para finalizar con ese id");
		}
	}

	public void comenzar(Integer idTarea,String fecha) throws ExcepcionPersonalizada {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		boolean bandera = false;
		for(Tarea unaTarea : this.tareasAsignadas) {
			if(unaTarea.getId() == idTarea) {
				unaTarea.setFechaInicio(LocalDateTime.parse(fecha, this.formato));
				bandera = true;
			}
		}
		if(bandera = false) {
			throw new ExcepcionPersonalizada("No existe una tarea para comenzar con ese id");
		}
	}
	
	public void finalizar(Integer idTarea,String fecha) throws ExcepcionPersonalizada {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		boolean bandera = false;
		for(Tarea unaTarea : this.tareasAsignadas) {
			if(unaTarea.getId() == idTarea) {
				unaTarea.setFechaFin(LocalDateTime.parse(fecha,this.formato));
				bandera = true;
			}
		}
		if(bandera == false) {
			throw new ExcepcionPersonalizada("No existe una tarea para finalizar con ese id");
		}
	}
		
	//GETTERS Y SETTERS.

	public Integer getCuil() {
		return cuil;
	}

	public void setCuil(Integer cuil) {
		this.cuil = cuil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Double getCostoHora() {
		return costoHora;
	}

	public void setCostoHora(Double costoHora) {
		this.costoHora = costoHora;
	}

	public Function<Tarea, Double> getCalculoPagoPorTarea() {
		return calculoPagoPorTarea;
	}

	public void setCalculoPagoPorTarea(Function<Tarea, Double> calculoPagoPorTarea) {
		this.calculoPagoPorTarea = calculoPagoPorTarea;
	}

	public List<Tarea> getTareasAsignadas() {
		return tareasAsignadas;
	}

	public void setTareasAsignadas(List<Tarea> tareasAsignadas) {
		this.tareasAsignadas = tareasAsignadas;
	}

	public Predicate<Tarea> getPuedeAsignarTarea() {
		return puedeAsignarTarea;
	}

	public void setPuedeAsignarTarea(Predicate<Tarea> puedeAsignarTarea) {
		this.puedeAsignarTarea = puedeAsignarTarea;
	}
	
}
