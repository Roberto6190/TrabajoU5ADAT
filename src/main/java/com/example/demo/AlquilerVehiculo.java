package com.example.demo;

import java.io.Serializable;

//POJO que representa un alquiler activo o cerrado.
//estado = true significa activo, false significa cerrado/devuelto.
//costeTotal se calcula al devolver, hasta entonces vale 0.

public class AlquilerVehiculo implements Serializable{

	private long idAlquiler, idVehiculo, idCliente;
	private String fechaInicio, fechaDevolucion;
	private boolean estado; //true = activo, false = cerrado
	private double costeTotal;
	
	public AlquilerVehiculo() {
		
	}
	
	public AlquilerVehiculo(long idAlquiler, long idVehiculo, long idCliente, String fechaInicio,
			String fechaDevolucion, boolean estado, double costeTotal) {
		this.idAlquiler = idAlquiler;
		this.idVehiculo = idVehiculo;
		this.idCliente = idCliente;
		this.fechaInicio = fechaInicio;
		this.fechaDevolucion = fechaDevolucion;
		this.estado = estado;
		this.costeTotal = costeTotal;
	}

	public long getIdAlquiler() {
		return idAlquiler;
	}
	
	public void setIdAlquiler(long idAlquiler) {
		this.idAlquiler = idAlquiler;
	}
	
	public long getIdVehiculo() {
		return idVehiculo;
	}
	
	public void setIdVehiculo(long idVehiculo) {
		this.idVehiculo = idVehiculo;
	}
	
	public long getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}
	
	public String getFechaInicio() {
		return fechaInicio;
	}
	
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	public String getFechaDevolucion() {
		return fechaDevolucion;
	}
	
	public void setFechaDevolucion(String fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}
	
	public boolean isEstado() {
		return estado;
	}
	
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public double getCosteTotal() {
		return costeTotal;
	}
	
	public void setCosteTotal(double costeTotal) {
		this.costeTotal = costeTotal;
	}
	
}
