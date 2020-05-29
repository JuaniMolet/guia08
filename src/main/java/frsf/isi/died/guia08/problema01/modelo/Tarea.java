package frsf.isi.died.guia08.problema01.modelo;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Optional;

import frsf.isi.died.guia08.problema01.ExcepcionPersonalizada.ExcepcionPersonalizada;

public class Tarea {

	private Integer id;
	private String descripcion;
	private Integer duracionEstimada;
	private Empleado empleadoAsignado;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private Boolean facturada;
	
	
	//CONSTRUCTORES
	
	public Tarea() {
		super();
		this.fechaInicio = null;
		this.fechaFin = null;
		this.facturada = false;
	}
	
	
	public Tarea(Integer id, String descripcion, Integer duracionEstimada) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.duracionEstimada = duracionEstimada;
		this.empleadoAsignado = null;
		this.fechaInicio = null;
		this.fechaFin = null;
		this.facturada = false;
	}


	//METODOS.
	
	//RETORNA LA CANTIAD DE DIAS EN LOS QUE SE REALIZO UNA TAREA.
	public int diasReales() {
		return (int)  Duration.between(this.fechaInicio,this.fechaFin).toDays();
	}
	
	//RETORNA LA CANTIDAD DE DIAS EN LOS QUE SE REALIZO UNA TAREA.
	public int diasEstimados() {
		int dias = duracionEstimada / 4;
		if(duracionEstimada % 4 == 0.0) {
			return dias;
		}
		else {
			return dias+1;
		}
	}
	
	//RETORNA TRUE SI LA TAREA TERMINO ANTES DE LO ESTIMADO.
		public boolean terminoAntes() {
			int dias = this.diasEstimados() - this.diasReales();
			if(dias > 0) {
				return true;
			}
			else{
				return false;
			}
		}
	
	//RETORNA TRUE SI LA TAREA TERMINO MAS DE DOS DIAS TARDE.
		public boolean terminoMasDeDosDiasTarde() {
			int dias = this.diasEstimados()-this.diasReales();
			if(dias < -2) {
				return true;
			}
			else{
				return false;
			}
		}	
	
	//RETORNA TRUE SI LA TAREA AUN ESTA PENDIENTE.
	public boolean tareaPendiente() {
		if(this.fechaFin != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//RETORNA TRUE SI LA TAREA TERMINO.
	public boolean tareaTerminada() {
		if(this.tareaPendiente() == false) {
			return true;
		}
		return false;
	}
	
	public void asignarEmpleado(Empleado e) throws ExcepcionPersonalizada{
		if(this.empleadoAsignado == null) {
			if(this.fechaFin != null) {
				this.empleadoAsignado = e;
			}
			else {
				throw new ExcepcionPersonalizada("Esta tarea ya ha finalizado");
			}
		}
		else {
			throw new ExcepcionPersonalizada("Esta tarea ya tiene un empleado asignado");
		}
	}
	
	
	//GETTERS Y SETTERS.
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDuracionEstimada() {
		return duracionEstimada;
	}

	public void setDuracionEstimada(Integer duracionEstimada) {
		this.duracionEstimada = duracionEstimada;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}
	
	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	public boolean tareaEmpezada() {
		if(this.getFechaInicio() != null) {
			return true;
		}
		else return false;
	}
	

	
	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Boolean getFacturada() {
		return facturada;
	}
	
	public Tarea facturar() {
		this.facturada = true;
		return this;
	}
	
	public Empleado getEmpleadoAsignado() {
		return empleadoAsignado;
	}
	
	public void imprimirEmpleadoAsignado() {
		System.out.println(this.empleadoAsignado.getNombre());
	}
	
	@Override
	public String toString() {
		return this.id+";"+this.descripcion+";"+this.duracionEstimada;
	}


	public String asCsv() {
		//ID; DESCRIPCION; DURACIONESTIMADA; CUILEMPLEADOASIGNAR 
		return this.id + ";\"" + this.descripcion + ";\"" + this.duracionEstimada + ";\"" + this.empleadoAsignado.getCuil();
	}
	
}
