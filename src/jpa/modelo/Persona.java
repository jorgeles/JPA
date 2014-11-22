package jpa.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
@Entity
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String apellidos;
	@OneToMany(cascade=CascadeType.PERSIST)
	private List<Empleo> empleos = new ArrayList<Empleo>();
	@ManyToOne(cascade=CascadeType.PERSIST)
	private Familia familias = new Familia();

	
	public void setId(Long id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public void setEmpleos(ArrayList<Empleo> empleos) {
		this.empleos = empleos;
	}

	public void setFamilias(Familia familias) {
		this.familias = familias;
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public Familia getFamilias() {
		return familias;
	}

	public List<Empleo> getEmpleos() {
		return empleos;
	}

}
