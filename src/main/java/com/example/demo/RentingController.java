package com.example.demo;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Clase principal del servidor que recibe y gestiona todas las peticiones HTTP.
// @RestController le dice a Spring que esta clase maneja peticiones y que
// los objetos que devuelven sus metodos se convierten automaticamente a JSON.
// @CrossOrigin permite que el navegador pueda hacer peticiones desde otro puerto,
// por ejemplo desde el index.html al servidor.
// Sin esto el navegador bloquearia las llamadas por politica de seguridad CORS.

@RestController
@CrossOrigin
public class RentingController {

	// JdbcTemplate es la clase de Spring para ejecutar SQL contra la BD.
	// Spring lo crea automaticamente con la configuracion de application.properties
	// y lo inyecta aqui por el constructor.

	private final JdbcTemplate jdbcTemplate;

	public RentingController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// Crea las 3 tablas si no existen ya.

	@GetMapping("/crear")
	public String crearTablas() {

		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS vehiculos (" + "idVehiculo INT AUTO_INCREMENT PRIMARY KEY,"
				+ "marca VARCHAR(255) NOT NULL," + "modelo VARCHAR(255) NOT NULL," + "matricula VARCHAR(7),"
				+ "tipoVehiculo ENUM('Pequeño', 'Mediano', 'Grande', 'Todo-terreno', 'Lujo', 'Mono-volumen', 'Furgoneta'),"
				+ "precioDia DECIMAL(7,2) NOT NULL," + "disponible BOOLEAN DEFAULT TRUE)");

		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS clientes (" + "idCliente INT AUTO_INCREMENT PRIMARY KEY,"
				+ "nombre VARCHAR(50)," + "apellido VARCHAR(50)," + "email VARCHAR(50) UNIQUE NOT NULL,"
				+ "telefono VARCHAR(9) UNIQUE NOT NULL," + "DNI VARCHAR(9) UNIQUE NOT NULL)");

		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS alquileres (" + "idAlquiler INT AUTO_INCREMENT PRIMARY KEY,"
				+ "idCliente INT NOT NULL," + "idVehiculo INT NOT NULL," + "fechaInicio VARCHAR(10) NOT NULL,"
				+ "fechaDevolucion VARCHAR(10) NOT NULL," + "estado BOOLEAN DEFAULT TRUE," + "costeTotal DOUBLE,"
				+ "FOREIGN KEY (idCliente) REFERENCES clientes(idCliente),"
				+ "FOREIGN KEY (idVehiculo) REFERENCES vehiculos(idVehiculo))");

		return "TABLAS CREADAS";
	}

	// Devuelve todos los vehiculos de la tabla.

	@GetMapping("/vehiculos")
	public List<Vehiculo> listarVehiculos() {
		return jdbcTemplate.query("SELECT * FROM vehiculos", new RowMapperVehiculo());
	}

	// Solo devuelve los que tienen disponible = TRUE.

	@GetMapping("/vehiculos/disponibles")
	public List<Vehiculo> listarVehiculosDisponibles() {
		return jdbcTemplate.query("SELECT * FROM vehiculos WHERE disponible = TRUE", new RowMapperVehiculo());
	}

	// Busca un vehiculo concreto por su ID.
	// @PathVariable coge el valor que va dentro de la URL, en /vehiculos/{id}.

	@GetMapping("/vehiculos/{id}")
	public Vehiculo buscarVehiculo(@PathVariable long id) {
		List<Vehiculo> lista = jdbcTemplate.query("SELECT * FROM vehiculos WHERE idVehiculo = ?",
				new RowMapperVehiculo(), id);
		return lista.isEmpty() ? null : lista.get(0);
	}

	// Inserta un vehiculo nuevo en la tabla.

	@GetMapping("/vehiculos/aniadir")
	public String aniadirVehiculo(@RequestParam String marca, @RequestParam String modelo,
			@RequestParam String matricula, @RequestParam String tipo, @RequestParam double precio) {
		int filas = jdbcTemplate.update(
				"INSERT INTO vehiculos (marca, modelo, matricula, tipoVehiculo, precioDia, disponible) VALUES (?,?,?,?,?,TRUE)",
				marca, modelo, matricula, tipo, precio);
		return filas > 0 ? "Vehiculo añadido" : "Error al añadir el vehiculo";
	}

	// Borra un vehiculo por su ID.
	// Antes de borrar comprueba si tiene alquileres activos, si los tiene
	// no se puede borrar porque romperia las claves foraneas de la tabla
	// alquileres.

	@GetMapping("/vehiculos/delete/{id}")
	public String deleteVehiculo(@PathVariable long id) {
		List<AlquilerVehiculo> activos = jdbcTemplate
				.query("SELECT * FROM alquileres WHERE idVehiculo = ? AND estado = TRUE", new RowMapperRentig(), id);
		if (!activos.isEmpty()) {
			return "No se puede eliminar: el vehiculo tiene un alquiler activo";
		}
		int filas = jdbcTemplate.update("DELETE FROM vehiculos WHERE idVehiculo = ?", id);
		return filas > 0 ? "Vehiculo eliminado" : "Vehiculo no encontrado";
	}

	// Devuelve todos los clientes de la tabla.

	@GetMapping("/clientes")
	public List<Cliente> listarClientes() {
		return jdbcTemplate.query("SELECT * FROM clientes", new RowMapperCliente());
	}

	// Busca un cliente concreto por su ID.

	@GetMapping("/clientes/{id}")
	public Cliente buscarCliente(@PathVariable long id) {
		List<Cliente> lista = jdbcTemplate.query("SELECT * FROM clientes WHERE idCliente = ?", new RowMapperCliente(),
				id);
		return lista.isEmpty() ? null : lista.get(0);
	}

	// Busca un cliente por su DNI.
	// Lo usa la web para que el usuario se identifique con su DNI
	// sin tener que saber su ID.

	@GetMapping("/clientes/dni/{dni}")
	public Cliente buscarClientePorDni(@PathVariable String dni) {
		List<Cliente> lista = jdbcTemplate.query("SELECT * FROM clientes WHERE DNI = ?", new RowMapperCliente(), dni);
		return lista.isEmpty() ? null : lista.get(0);
	}

	// Inserta un cliente nuevo en la tabla.
	// El try/catch captura el error de la BD cuando intentas insertar un email,
	// telefono o DNI que ya existe.

	@GetMapping("/clientes/aniadir")
	public String insertarCliente(@RequestParam String nombre, @RequestParam String apellido,
			@RequestParam String email, @RequestParam String telefono, @RequestParam String DNI) {
		try {
			int filas = jdbcTemplate.update(
					"INSERT INTO clientes (nombre, apellido, email, telefono, DNI) VALUES (?,?,?,?,?)", nombre,
					apellido, email, telefono, DNI);
			return filas > 0 ? "Cliente añadido" : "Error al añadir cliente";
		} catch (Exception e) {
			if (e.getMessage().contains("Duplicate entry")) {
				return "Error: ya existe un cliente con ese email, teléfono o DNI.";
			}
			return "Error al añadir cliente.";
		}
	}

	// Borra un cliente por su ID.
	// Antes de borrar comprueba si tiene alquileres activos.

	@GetMapping("/clientes/delete/{id}")
	public String deleteCliente(@PathVariable long id) {
		List<AlquilerVehiculo> activos = jdbcTemplate
				.query("SELECT * FROM alquileres WHERE idCliente = ? AND estado = TRUE", new RowMapperRentig(), id);
		if (!activos.isEmpty()) {
			return "No se puede eliminar: el cliente tiene un alquiler activo";
		}
		int filas = jdbcTemplate.update("DELETE FROM clientes WHERE idCliente = ?", id);
		return filas > 0 ? "Cliente eliminado" : "Cliente no encontrado";
	}

	// Devuelve todos los alquileres de la tabla (activos y cerrados).

	@GetMapping("/alquileres")
	public List<AlquilerVehiculo> listarAlquileres() {
		return jdbcTemplate.query("SELECT * FROM alquileres", new RowMapperRentig());
	}

	// Devuelve solo los alquileres con estado = TRUE (activos).

	@GetMapping("/alquileres/activos")
	public List<AlquilerVehiculo> listarAlquileresActivos() {
		return jdbcTemplate.query("SELECT * FROM alquileres WHERE estado = TRUE", new RowMapperRentig());
	}

	// Busca un alquiler concreto por su ID.

	@GetMapping("/alquileres/{id}")
	public AlquilerVehiculo buscarAlquiler(@PathVariable long id) {
		List<AlquilerVehiculo> lista = jdbcTemplate.query("SELECT * FROM alquileres WHERE idAlquiler = ?",
				new RowMapperRentig(), id);
		return lista.isEmpty() ? null : lista.get(0);
	}

	// Operativa de negocio principal: crea un alquiler nuevo.
	// Antes de insertar hace 3 validaciones:
	// 1. Cliente existe en la BD.
	// 2. Vehiculo existe en la BD.
	// 3. Vehiculo tiene disponible = TRUE.
	// Si todo esta bien inserta el alquiler con costeTotal = 0 (aun no se ha
	// devuelto)
	// y hace un UPDATE para marcar el vehiculo como disponible = FALSE.

	@GetMapping("/alquileres/crear")
	public String crearAlquiler(@RequestParam long idCliente, @RequestParam long idVehiculo,
			@RequestParam String fechaInicio, @RequestParam String fechaDevolucion) {

		List<Cliente> clientes = jdbcTemplate.query("SELECT * FROM clientes WHERE idCliente = ?",
				new RowMapperCliente(), idCliente);
		if (clientes.isEmpty()) {
			return "No existe el cliente";
		}

		List<Vehiculo> vehiculos = jdbcTemplate.query("SELECT * FROM vehiculos WHERE idVehiculo = ?",
				new RowMapperVehiculo(), idVehiculo);
		if (vehiculos.isEmpty()) {
			return "No existe el vehiculo";
		}

		Vehiculo vehiculo = vehiculos.get(0);
		if (!vehiculo.isDisponible()) {
			return "El vehiculo no esta disponible";
		}

		int filasAlquiler = jdbcTemplate.update(
				"INSERT INTO alquileres (idCliente, idVehiculo, fechaInicio, fechaDevolucion, estado, costeTotal) VALUES (?,?,?,?,TRUE,0)",
				idCliente, idVehiculo, fechaInicio, fechaDevolucion);

		int filasVehiculo = jdbcTemplate.update("UPDATE vehiculos SET disponible = FALSE WHERE idVehiculo = ?",
				idVehiculo);

		if (filasAlquiler > 0 && filasVehiculo > 0) {
			return "Alquiler creado correctamente";
		} else {
			return "Error al crear el alquiler";
		}
	}

	// Cierra un alquiler y libera el vehiculo.

	@GetMapping("/alquileres/devolver/{id}")
	public String devolverVehiculo(@PathVariable long id, @RequestParam double costeTotal) {

		List<AlquilerVehiculo> alquileres = jdbcTemplate.query("SELECT * FROM alquileres WHERE idAlquiler = ?",
				new RowMapperRentig(), id);
		if (alquileres.isEmpty()) {
			return "No existe el alquiler";
		}

		AlquilerVehiculo alquiler = alquileres.get(0);

		if (!alquiler.isEstado()) {
			return "El alquiler ya estaba cerrado";
		}

		int filasAlquiler = jdbcTemplate
				.update("UPDATE alquileres SET estado = FALSE, costeTotal = ? WHERE idAlquiler = ?", costeTotal, id);

		int filasVehiculo = jdbcTemplate.update("UPDATE vehiculos SET disponible = TRUE WHERE idVehiculo = ?",
				alquiler.getIdVehiculo());

		if (filasAlquiler > 0 && filasVehiculo > 0) {
			return "Vehiculo devuelto correctamente";
		} else {
			return "Error al devolver el vehiculo";
		}
	}

	// Devuelve todos los alquileres (activos y cerrados) de un cliente concreto.

	@GetMapping("/clientes/{id}/alquileres")
	public List<AlquilerVehiculo> alquileresPorCliente(@PathVariable long id) {
		return jdbcTemplate.query("SELECT * FROM alquileres WHERE idCliente = ?", new RowMapperRentig(), id);
	}

	// Devuelve todos los alquileres (activos y cerrados) de un vehiculo concreto.

	@GetMapping("/vehiculos/{id}/alquileres")
	public List<AlquilerVehiculo> alquileresPorVehiculo(@PathVariable long id) {
		return jdbcTemplate.query("SELECT * FROM alquileres WHERE idVehiculo = ?", new RowMapperRentig(), id);
	}
}