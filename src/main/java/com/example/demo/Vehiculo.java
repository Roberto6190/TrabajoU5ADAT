package com.example.demo;

import java.io.Serializable;

// Representa un vehiculo del catalogo de renting.
// Cada atributo de esta clase es una columna de la tabla "vehiculos" en la BD.
// Implementa Serializable para que Java pueda convertir el objeto a bytes.

public class Vehiculo implements Serializable {

	// Atributos de las columnas de la tabla vehiculos.
	private long idVehiculo;
	private String marca, modelo, matricula, tipoVehiculo;
	private double precio_dia;
	private boolean disponible;

	// Constructor vacio, Spring lo necesita para poder crear objetos Vehiculo
	// vacios y rellenarlos el solo al convertir el JSON que llega del servidor.
	public Vehiculo() {

	}

	// Constructor completo, lo usa el RowMapperVehiculo para crear un Vehiculo
	// con todos los datos de una fila de la BD.
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

	// Getters y setters, permiten leer y modificar los atributos privados
	// desde otras clases como el RentingController o el ClienteConsola.

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
	
	@Override
	public String toString() {
		return String.format("%-10s%-22s%-22s%-22s%-22s%-22s%-22s",
				idVehiculo, marca, modelo, matricula, tipoVehiculo,
				precio_dia, (disponible ? "Si" : "No"));
	}

}