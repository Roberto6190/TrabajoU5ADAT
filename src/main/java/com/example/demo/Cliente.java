package com.example.demo;

import java.io.Serializable;

//POJO que representa un cliente registrado.
//Los atributos coinciden columna a columna con la tabla clientes.

public class Cliente implements Serializable {

	private long idCliente;
	private String nombre, apellido, email, telefono, DNI;

	public Cliente() {

	}

	public Cliente(long idCliente, String nombre, String apellido, String email, String telefono, String dNI) {
		this.idCliente = idCliente;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.telefono = telefono;
		DNI = dNI;
	}

	public long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDNI() {
		return DNI;
	}

	public void setDNI(String dNI) {
		DNI = dNI;
	}

	@Override
	public String toString() {
		return String.format("%-10s%-22s%-22s%-30s%-22s%-22s", idCliente, nombre, apellido, email, telefono, DNI);
	}

}
