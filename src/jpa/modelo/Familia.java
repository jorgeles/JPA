package jpa.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
@Entity
public class Familia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String Description;
	@OneToMany(cascade=CascadeType.PERSIST)
	private List<Persona> personas = new ArrayList<Persona>();

	public void setId(Long id) {
		this.id = id;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public void setPersonas(ArrayList<Persona> personas) {
		this.personas = personas;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return Description;
	}

	public List<Persona> getPersonas() {
		return personas;
	}

}
