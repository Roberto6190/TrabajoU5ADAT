package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RowMapperCliente implements RowMapper<Cliente> {
	public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		return new Cliente(rs.getLong("idCliente"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("email"),
				rs.getString("telefono"), rs.getString("DNI"));
	}
}
