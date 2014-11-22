package jpa.eclipselink.principal;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;

import jpa.modelo.Familia;
import jpa.modelo.Persona;

public class JpaTest {

	private static final String PERSISTENCE_UNIT_NAME = "people";
	private EntityManagerFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		// Comenzar una nueva transaccion local de tal forma que pueda persistir
		// como una nueva entidad
		em.getTransaction().begin();

		// Leer las entrada que ya hay en la base de datos
		Query q = em.createQuery("select m from Persona m");
		// Las personas no deben tener ningun atributo asignado todavia
		// Comprobar si necesitamos crear entradas en la base
		// Vamos a ello... si fuera necesario
		if (q.getResultList().size() == 0) {
			Familia familia = new Familia();
			familia.setDescription("Familia Martinez");
			em.persist(familia);
			for (int i = 0; i < 20; i++) {
				Persona persona = new Persona();
				persona.setNombre("Pepe_" + i);
				persona.setApellidos("Martinez_" + i);
				em.persist(persona);
				// ahora hacemos que persista la relacion familia-persona
				familia.getPersonas().add(persona);
				em.persist(persona);
				em.persist(familia);
			}
		}

		// Ahora hay que hacer "commit" de la transaccion, lo que causara que la
		// entidad se almacene en la base de datos
		em.getTransaction().commit();
		// Ahora hay que cerrar el EntityManager o perderemos nuestras entradas.
		em.close();
	}

	@Test
	public void comprobarPersonas() {
		// ahora vamos a comprobar la base de datos para ver si las entradas que
		// hemos creado estan alli
		// Para eso, nos crearemos un gestor de entidades "fresco"
		EntityManager fresco = factory.createEntityManager();

		// Realizaremos una consulta simple que consistira en seleccionar a
		// todas las personas
		Query q = fresco.createQuery("select p from Persona p");
		// Si todo ha ido bien, en la lista de personas heos de tener a 20
		// miembros: utilizar "assertTrue()", "getResultList()" y "size()" para
		// comprobarlo
		assertTrue(q.getResultList().size() == 20);
		// Acordarse de cerrar el gestor de entidades
		fresco.close();

	}

	@Test
	public void comprobarFamilia() {
		// Para esto, nos crearemos un gestor de entidades "fresco"
		EntityManager fresco = factory.createEntityManager();
		// Recorrer cada una de las entidades y mostrar cada uno de sus campos
		// asi como la fecha de creacion
		Query q = fresco.createQuery("select f from Familia f ");
		List<Familia> listaCompleto = q.getResultList();
		for (Familia lista : listaCompleto) {
			System.out.println(lista.getId());
			System.out.println(lista.getDescription());
			System.out.println(lista.getPersonas());

		}
		// Deberiamos tener una familia con 20 personas
		// Utilizar "assertTrue()", "getResultList()", "size()" y
		// "getSingleResult()" para determinarlo
		// Acordarse de cerrar el gestor de entidades
		fresco.close();
	}

	@Test(expected = javax.persistence.NoResultException.class)
	public void eliminarPersona() {
		// Para esto, nos crearemos un gestor de entidades "fresco"
		EntityManager fresco = factory.createEntityManager();
		// Comenzar una nueva transaccion local de tal manera que podamos hacer
		// persitente una nueva entidad
		fresco.getTransaction().begin();
		// Ahora me creare la consulta necesaria eliminar la persona de nombre y
		// apellidos que indicare despues

		Query q = fresco
				.createQuery("SELECT p FROM Persona p WHERE p.nombre = :nombre AND p.apellidos = :apellido");

		// Ahora asigno los parametros
		q.setParameter("nombre", "Pepe_1");
		q.setParameter("apellido", "Martinez_!");

		// Ahora utilizo el metodo: "getSingleResult()" para obtener a la
		// persona que me interesa y los metodos: "remove(persona)" y "commit()"
		// para eliminarla de la entidad y confirmar la eliminacion,
		// respectivamente
		Persona persona = (Persona) q.getSingleResult();
		fresco.remove(persona);
		fresco.getTransaction().commit();
		// Acordarse de cerrar el gestor de entidades
		fresco.close();
	}
}
