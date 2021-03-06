package com.example.demo.Model;
import java.io.Serializable;

import javax.persistence.*;


@Entity
public class Mensaje implements Serializable {

	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private Usuario emisor;
	
	private String cuerpoMensaje;
	
	public Mensaje() {}
	public Mensaje(Usuario emisor, String mensaje) {
		this.emisor = emisor;
		this.cuerpoMensaje = mensaje;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getCuerpoMensaje() {
		return cuerpoMensaje;
	}
	
	public void setCuerpoMensaje(String s) {
		cuerpoMensaje = s;
	}

	
}
