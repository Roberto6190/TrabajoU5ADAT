package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RowMapperRentig implements RowMapper<AlquilerVehiculo> {
	
	public AlquilerVehiculo mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new AlquilerVehiculo(rs.getLong("idAlquiler"), rs.getLong("idVehiculo"), rs.getLong("idCliente"),
				rs.getString("fechaInicio"), rs.getString("fechaDevolucion"), rs.getBoolean("estado"),
				rs.getDouble("costeTotal"));
	}
}
