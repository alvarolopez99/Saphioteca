package com.example.demo.Model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pregunta implements Serializable {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String enunciado;
	private String respuesta;
	
	public Pregunta() {}
	public Pregunta(String enunciado, String respuesta) {
		this.enunciado = enunciado;
		this.respuesta = respuesta;
	}
	
	public void setEnunciado(String enun) {
		this.enunciado = enun;
	}
	public String getEnunciado() {
		return enunciado;
	}
	public void setRespuesta(String resp) {
		this.respuesta = resp;
	}
	
	public String getRespuesta() {
		return respuesta;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	

}
