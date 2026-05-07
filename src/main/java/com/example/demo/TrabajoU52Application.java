package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Clase principal del servidor Spring Boot.
// @SpringBootApplication le dice a Spring que arranque todo automaticamente:
// escanea el paquete buscando clases anotadas, configura la conexion a la BD
// con lo que hay en application.properties, y levanta el servidor.

@SpringBootApplication
public class TrabajoU52Application {

	// SpringApplication.run() arranca la aplicacion pasandole la propia clase como
	// referencia.
	public static void main(String[] args) {
		SpringApplication.run(TrabajoU52Application.class, args);
	}
}