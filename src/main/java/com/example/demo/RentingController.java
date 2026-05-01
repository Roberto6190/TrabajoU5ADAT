package com.example.demo;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

		// Tabla de vehiculos.
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS vehiculos (" + "idVehiculo INT AUTO_INCREMENT PRIMARY KEY,"
				+ "marca VARCHAR(255) NOT NULL," + "modelo VARCHAR(255) NOT NULL," + "matricula VARCHAR(7),"
				+ "tipoVehiculo ENUM('Pequeño', 'Mediano', 'Grande', 'Todo-terreno', 'Lujo', 'Mono-volumen', 'Furgoneta'),"
				+ "precioDia DECIMAL(7,2) NOT NULL," + "disponible BOOLEAN DEFAULT TRUE)");

		// Tabla clientes.
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS clientes (" + "idCliente INT AUTO_INCREMENT PRIMARY KEY,"
				+ "nombre VARCHAR(50)," + "apellido VARCHAR(50)," + "email VARCHAR(50) UNIQUE NOT NULL,"
				+ "telefono VARCHAR(9) UNIQUE NOT NULL," + "DNI VARCHAR(9) UNIQUE NOT NULL)");

		// Tabla alquileres.
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS alquileres (" + "idAlquiler INT AUTO_INCREMENT PRIMARY KEY,"
				+ "idCliente INT NOT NULL," + "idVehiculo INT NOT NULL," + "fechaInicio VARCHAR(10) NOT NULL,"
				+ "fechaDevolucion VARCHAR(10) NOT NULL," + "estado BOOLEAN DEFAULT TRUE," + "costeTotal DOUBLE,"
				+ "FOREIGN KEY (idCliente) REFERENCES clientes(idCliente),"
				+ "FOREIGN KEY (idVehiculo) REFERENCES vehiculos(idVehiculo))");

		return "TABLAS CREADAS";
	}

	// Listar todos los Vehiculos.
	@GetMapping("/vehiculos")
	public List<Vehiculo> listarVehiculos() {
		return jdbcTemplate.query("SELECT * FROM vehiculos", new RowMapperVehiculo());
	}

	// Listar vehiculos disponibles.
	@GetMapping("/vehiculos/disponibles")
	public List<Vehiculo> listarVehiculosDisponibles() {
		return jdbcTemplate.query("SELECT * FROM vehiculos WHERE disponible = TRUE", new RowMapperVehiculo());
	}

	// Buscar vehiculos por ID.
	@GetMapping("/vehiculos/{id}")
	public Vehiculo buscarVehiculo(@PathVariable long id) {
		List<Vehiculo> lista = jdbcTemplate.query("SELECT * FROM vehiculos WHERE idVehiculo = ?",
				new RowMapperVehiculo());
		return lista.isEmpty() ? null : lista.get(0);// Si esta vacia, cogemos el primero.
	}

	//Añadir vehiculo.
	@GetMapping("/vehiculos/aniadir")
	public String aniadirVehiculo(@RequestParam String marca, @RequestParam String modelo,
			@RequestParam String matricula, @RequestParam String tipo, @RequestParam double precio) {
		int filas = jdbcTemplate.update(
				"INSERT INTO vehiculos (marca, modelo, matricula, tipoVehiculo, precioDia, disponible) VALUES (?,?,?,?,?, TRUE)",
				marca, modelo, matricula, tipo, precio);

		return filas > 0 ? "Vehiculo añadido" : "Error al añadir el vehiculo";
	}

	//Borrar vehiculo.
	@GetMapping("/vehiculos/delete/{id}")
	public String deleteVehiculo(@PathVariable long id) {
		List<AlquilerVehiculo> activos = jdbcTemplate
				.query("SELECT * FROM alquileres WHERE idVehiculo = ? AND estado = TRUE", new RowMapperRentig(), id);
		if (!activos.isEmpty()) {
			return "No se puede eliminar: el vehículo tiene un alquiler activo";
		}
		int filas = jdbcTemplate.update("DELETE FROM vehiculos WHERE idVehiculo = ?", id);
		return filas > 0 ? "Vehículo eliminado" : "Vehículo no encontrado";
	}
}
