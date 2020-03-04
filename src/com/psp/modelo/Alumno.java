package com.psp.modelo;

public class Alumno {

	private String nombre;
	private String apellido;
	private String curso;
	private String email;
	
	public Alumno() {
		
	}

	public Alumno(String nombre, String apellido, String curso, String email) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.curso = curso;
		this.email = email;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	
	
	
}
