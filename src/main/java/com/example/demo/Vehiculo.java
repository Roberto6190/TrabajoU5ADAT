package com.example.demo;

import java.io.Serializable;

//POJO que representa un vehículo del catálogo.
//Debe tener constructor vacío para que Spring pueda
//deserializar el JSON que llega en los POST.

public class Vehiculo implements Serializable{

	private long idVehiculo;
	private String marca, modelo, matricula, tipoVehiculo;
	private double precio_dia;
	private boolean disponible;
	
	public Vehiculo() {
		
	}
	
	public Vehiculo(long idVehiculo, String marca, String modelo, String matricula, String tipoVehiculo,
			double precio_dia, boolean disponible) {
		this.idVehiculo = idVehiculo;
		this.marca = marca;
		this.modelo = modelo;
		this.matricula = matricula;
		this.tipoVehiculo = tipoVehiculo;
		this.precio_dia = precio_dia;
		this.disponible = disponible;
	}

	public long getIdVehiculo() {
		return idVehiculo;
	}
	
	public void setIdVehiculo(long idVehiculo) {
		this.idVehiculo = idVehiculo;
	}
	
	public String getMarca() {
		return marca;
	}
	
	public void setMarca(String marca) {
		this.marca = marca;
	}
	
	public String getModelo() {
		return modelo;
	}
	
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	public String getMatricula() {
		return matricula;
	}
	
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	public String getTipoVehiculo() {
		return tipoVehiculo;
	}
	
	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}
	
	public double getPrecio_dia() {
		return precio_dia;
	}
	
	public void setPrecio_dia(double precio_dia) {
		this.precio_dia = precio_dia;
	}
	
	public boolean isDisponible() {
		return disponible;
	}
	
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	
}
