package com.example.demo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//Controller principal — recibe todas las peticiones HTTP y las gestiona.
//@RestController indica a Spring que esta clase maneja endpoints REST.
//@CrossOrigin permite peticiones desde el cliente web aunque esté en otro puerto.
//JdbcTemplate es inyectado por Spring automáticamente desde application.properties.

@RestController
@CrossOrigin
public class RentingController {
	private final JdbcTemplate jdbcTemplate;

	public RentingController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@GetMapping("/crear")
	public String crearTablas() {
		
		//Tabla de vehiculos.
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS vehiculos ("
				+ "idVehiculo INT AUTO_INCREMENT PRIMARY KEY,"
				+ "marca VARCHAR(255) NOT NULL,"
				+ "modelo VARCHAR(255) NOT NULL,"
				+ "matricula VARCHAR(7),"
				+ "tipoVehiculo ENUM('Pequeño', 'Mediano', 'Grande', 'Todo-terreno', 'Lujo', 'Mono-volumen', 'Furgoneta'),"
				+ "precioDia DECIMAL(7,2) NOT NULL,"
				+ "disponible BOOLEAN DEFAULT TRUE)");
		
		//Tabla clientes.
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS clientes ("
				+ "idCliente INT AUTO_INCREMENT PRIMARY KEY,"
				+ "nombre VARCHAR(50),"
				+ "apellido VARCHAR(50),"
				+ "email VARCHAR(50) UNIQUE NOT NULL,"
				+ "telefono VARCHAR(9) UNIQUE NOT NULL,"
				+ "DNI VARCHAR(9) UNIQUE NOT NULL)");
		
		//Tabla alquileres.
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS alquileres ("
				+ "idAlquiler INT AUTO_INCREMENT PRIMARY KEY,"
				+ "idCliente INT NOT NULL,"
				+ "idVehiculo INT NOT NULL,"
				+ "fechaInicio VARCHAR(8) NOT NULL,"
				+ "fechaDevolucion VARCHAR(8) NOT NULL,"
				+ "estado BOOLEAN DEFAULT TRUE,"
				+ "costeTotal DOUBLE,"
				+ "FOREIGN KEY (idCliente) REFERENCES clientes(idCliente),"
				+ "FOREIGN KEY (idVehiculo) REFERENCES vehiculos(idVehiculo))");
		
		return "TABLAS CREADAS";
	}
	
}
