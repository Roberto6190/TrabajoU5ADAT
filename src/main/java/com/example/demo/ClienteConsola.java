package com.example.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Scanner;

public class ClienteConsola {

	// URL base del servidor Spring Boot.
	static final String BASE = "http://localhost:8080";
	static HttpClient cliente = HttpClient.newHttpClient();
	static Scanner in = new Scanner(System.in);

	// Método auxiliar: construye la request, la envía y devuelve el body.
	static String get(String url) {
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
			HttpResponse<String> response = cliente.send(request, BodyHandlers.ofString());
			return response.body();
		} catch (Exception e) {
			return "ERROR DE CONEXION: " + e.getMessage();
		}
	}

	// MAIN
	
	public static void main(String[] args) {
		System.out.println("==== PANEL DE ADMINISTRACION — RENTING DE VEHICULOS ====");

		int opcion = -1;

		do {
			System.out.println("\n---- MENU PRINCIPAL ----");
			System.out.println("1. Gestion de vehiculos.");
			System.out.println("2. Gestion de clientes.");
			System.out.println("3. Supervision de alquileres.");
			System.out.println("0. Salir.");
			System.out.print("Elegir opcion: ");
			opcion = in.nextInt();
			in.nextLine();
			System.out.println();

			switch (opcion) {
			case 1:
				menuVehiculos();
				break;
			case 2:
				menuClientes();
				break;
			case 3:
				menuAlquileres();
				break;
			case 0:
				System.out.println("Saliendo...");
				break;
			default:
				System.err.println("ERROR: Opcion no valida.");
				break;
			}
		} while (opcion != 0);
	}

	// MENÚ VEHÍCULOS
	private static void menuVehiculos() {

		int opcion = 0;
		String regexTipos = "^(Pequeño|Mediano|Grande|Todo-terreno|Lujo|Mono-volumen|Furgoneta)$";

		System.out.println("==== MENU VEHICULOS ====");
		System.out.println("1. Listar todos los vehiculos.");
		System.out.println("2. Listar vehiculos disponibles.");
		System.out.println("3. Buscar vehiculo por ID.");
		System.out.println("4. Añadir vehiculo.");
		System.out.println("5. Borrar vehiculo.");
		System.out.print("Elige una opcion: ");
		opcion = in.nextInt();
		in.nextLine();

		switch (opcion) {

		case 1:
			System.out.println(get(BASE + "/vehiculos"));
			break;

		case 2:
			System.out.println(get(BASE + "/vehiculos/disponibles"));
			break;

		case 3:
			long idBuscar = leerLong("ID del vehiculo a buscar: ");
			String resultado = get(BASE + "/vehiculos/" + idBuscar);
			if (resultado.isEmpty() || resultado.equals("null")) {
				System.err.println("ERROR: VEHICULO NO ENCONTRADO.");
			} else {
				System.out.println(resultado);
			}
			break;

		case 4:
			String marca = leerCadena("Marca: ");
			String modelo = leerCadena("Modelo: ");
			String matricula = leerCadena("Matricula: ");

			String tipo;
			do {
				System.out.print("Tipo (Pequeño/Mediano/Grande/Todo-terreno/Lujo/Mono-volumen/Furgoneta): ");
				tipo = in.nextLine().trim();
				if (tipo.isEmpty()) {
					System.err.println("ERROR: NO PUEDE ESTAR VACIO.");
				} else if (!tipo.matches(regexTipos)) {
					System.err.println("ERROR: TIPO NO VALIDO. (Ej: Pequeño, Lujo, Furgoneta...)");
				}
			} while (!tipo.matches(regexTipos));

			double precio = leerDouble("Precio por dia: ");

			String urlAniadir = BASE + "/vehiculos/aniadir"
					+ "?marca=" + marca.trim()
					+ "&modelo=" + modelo.trim()
					+ "&matricula=" + matricula.trim()
					+ "&tipo=" + tipo.trim()
					+ "&precio=" + precio;

			System.out.println(get(urlAniadir));
			break;

		case 5:
			long idBorrar = leerLong("ID del vehiculo a borrar: ");
			String resultadoBorrar = get(BASE + "/vehiculos/delete/" + idBorrar);
			if (resultadoBorrar.isEmpty() || resultadoBorrar.equals("null")) {
				System.err.println("ERROR: VEHICULO NO ENCONTRADO.");
			} else {
				System.out.println(resultadoBorrar);
			}
			break;

		default:
			System.err.println("ERROR: Opcion no valida.");
		}
	}

	// MENÚ CLIENTES

	private static void menuClientes() {

		int opcion = 0;
		String regexEmail = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
		String regexTelefono = "^[0-9]{9}$";
		String regexDNI = "^[0-9]{8}[A-Za-z]$";

		System.out.println("==== MENU CLIENTES ====");
		System.out.println("1. Listar todos los clientes.");
		System.out.println("2. Buscar cliente por ID.");
		System.out.println("3. Añadir cliente.");
		System.out.println("4. Borrar cliente.");
		System.out.print("Elige una opcion: ");
		opcion = in.nextInt();
		in.nextLine();

		switch (opcion) {

		case 1:
			System.out.println(get(BASE + "/clientes"));
			break;

		case 2:
			long idBuscar = leerLong("ID del cliente a buscar: ");
			String resultado = get(BASE + "/clientes/" + idBuscar);
			if (resultado.isEmpty() || resultado.equals("null")) {
				System.err.println("ERROR: CLIENTE NO ENCONTRADO.");
			} else {
				System.out.println(resultado);
			}
			break;

		case 3:
			String nombre = leerCadena("Nombre: ");
			String apellido = leerCadena("Apellido: ");

			String email;
			do {
				System.out.print("Email: ");
				email = in.nextLine().trim();
				if (email.isEmpty()) {
					System.err.println("ERROR: NO PUEDE ESTAR VACIO.");
				} else if (!email.matches(regexEmail)) {
					System.err.println("ERROR: FORMATO DE EMAIL NO VALIDO. (Ej: nombre@dominio.com)");
				}
			} while (!email.matches(regexEmail));

			String telefono;
			do {
				System.out.print("Telefono (9 digitos): ");
				telefono = in.nextLine().trim();
				if (telefono.isEmpty()) {
					System.err.println("ERROR: NO PUEDE ESTAR VACIO.");
				} else if (!telefono.matches(regexTelefono)) {
					System.err.println("ERROR: EL TELEFONO DEBE TENER 9 DIGITOS.");
				}
			} while (!telefono.matches(regexTelefono));

			String dni;
			do {
				System.out.print("DNI (8 numeros + letra): ");
				dni = in.nextLine().trim().toUpperCase();
				if (dni.isEmpty()) {
					System.err.println("ERROR: NO PUEDE ESTAR VACIO.");
				} else if (!dni.matches(regexDNI)) {
					System.err.println("ERROR: FORMATO DE DNI NO VALIDO. (Ej: 12345678A)");
				}
			} while (!dni.matches(regexDNI));

			String urlAnhadir = BASE + "/clientes/anhadir"
					+ "?nombre=" + nombre.trim()
					+ "&apellido=" + apellido.trim()
					+ "&email=" + email.trim()
					+ "&telefono=" + telefono.trim()
					+ "&DNI=" + dni.trim();

			System.out.println(get(urlAnhadir));
			break;

		case 4:
			long idBorrar = leerLong("ID del cliente a borrar: ");
			String resultadoBorrar = get(BASE + "/clientes/delete/" + idBorrar);
			if (resultadoBorrar.isEmpty() || resultadoBorrar.equals("null")) {
				System.err.println("ERROR: CLIENTE NO ENCONTRADO.");
			} else {
				System.out.println(resultadoBorrar);
			}
			break;

		default:
			System.err.println("ERROR: Opcion no valida.");
		}
	}

	// MENÚ ALQUILERES — solo supervisión, sin crear ni devolver
	
	private static void menuAlquileres() {

		int opcion = 0;

		System.out.println("==== MENU ALQUILERES (SUPERVISION) ====");
		System.out.println("1. Listar todos los alquileres.");
		System.out.println("2. Listar alquileres activos.");
		System.out.println("3. Buscar alquiler por ID.");
		System.out.println("4. Ver alquileres de un cliente.");
		System.out.println("5. Ver alquileres de un vehiculo.");
		System.out.print("Elige una opcion: ");
		opcion = in.nextInt();
		in.nextLine();

		switch (opcion) {

		case 1:
			System.out.println(get(BASE + "/alquileres"));
			break;

		case 2:
			System.out.println(get(BASE + "/alquileres/activos"));
			break;

		case 3:
			long idBuscar = leerLong("ID del alquiler a buscar: ");
			String resultado = get(BASE + "/alquileres/" + idBuscar);
			if (resultado.isEmpty() || resultado.equals("null")) {
				System.err.println("ERROR: ALQUILER NO ENCONTRADO.");
			} else {
				System.out.println(resultado);
			}
			break;

		case 4:
			long idCliente = leerLong("ID del cliente: ");
			System.out.println(get(BASE + "/clientes/" + idCliente + "/alquileres"));
			break;

		case 5:
			long idVehiculo = leerLong("ID del vehiculo: ");
			System.out.println(get(BASE + "/vehiculos/" + idVehiculo + "/alquileres"));
			break;

		default:
			System.err.println("ERROR: Opcion no valida.");
		}
	}

	// MÉTODOS AUXILIARES

	// Lee un String no vacío.
	private static String leerCadena(String mensaje) {
		String entrada;
		do {
			System.out.print(mensaje);
			entrada = in.nextLine().trim();
			if (entrada.isEmpty()) {
				System.err.println("ERROR: ESTE CAMPO NO PUEDE ESTAR VACIO.");
			}
		} while (entrada.isEmpty());
		return entrada;
	}

	// Lee un long mayor a 0.
	private static long leerLong(String mensaje) {
		long valor = 0;
		boolean esValido = false;
		System.out.print(mensaje);
		do {
			if (in.hasNextLong()) {
				valor = in.nextLong();
				in.nextLine();
				if (valor <= 0) {
					System.err.println("ERROR: INTRODUZCA UN VALOR MAYOR A 0.");
				} else {
					esValido = true;
				}
			} else {
				System.err.println("ERROR: INTRODUZCA UN VALOR VALIDO.");
				in.nextLine();
			}
		} while (!esValido);
		return valor;
	}

	// Lee un double mayor a 0.
	private static double leerDouble(String mensaje) {
		double valor = 0;
		boolean esValido = false;
		System.out.print(mensaje);
		do {
			if (in.hasNextDouble()) {
				valor = in.nextDouble();
				in.nextLine();
				if (valor <= 0) {
					System.err.println("ERROR: EL VALOR DEBE SER MAYOR A 0.");
				} else {
					esValido = true;
				}
			} else {
				System.err.println("ERROR: INTRODUZCA UN VALOR NUMERICO VALIDO.");
				in.nextLine();
			}
		} while (!esValido);
		return valor;
	}
}