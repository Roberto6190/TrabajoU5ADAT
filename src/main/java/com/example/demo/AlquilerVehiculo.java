package com.example.demo;

import java.io.Serializable;

// POJO que representa un alquiler, tanto si sigue activo como si ya se cerro.
// Cada atributo coincide con una columna de la tabla "alquileres" en la BD.
// No guarda objetos Cliente o Vehiculo enteros, solo sus IDs como claves foraneas.
// Implementa Serializable para que Java pueda convertir el objeto a bytes.

public class AlquilerVehiculo implements Serializable {

	// Atributos — uno por columna de la tabla alquileres.
	// estado: true = alquiler activo, false = alquiler cerrado (vehiculo devuelto).
	// costeTotal: vale 0 mientras el alquiler esta activo, se rellena al devolver.
	private long idAlquiler, idVehiculo, idCliente;
	private String fechaInicio, fechaDevolucion;
	private boolean estado;
	private double costeTotal;

	public AlquilerVehiculo() {

	}

	// Constructor completo — lo usa el RowMapperRentig para crear un AlquilerVehiculo.
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

	@Override
	public String toString() {
		return String.format("%-15s%-15s%-15s%-15s%-15s%-15s%-15s",
				idAlquiler, idVehiculo, idCliente,
				fechaInicio, fechaDevolucion,
				(estado ? "Activo" : "Cerrado"),
				(costeTotal > 0 ? costeTotal + "€" : "Pendiente"));
	}

}