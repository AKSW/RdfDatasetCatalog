package org.aksw.rdf_dataset_catalog.web.api;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.aksw.rdf_dataset_catalog.model.Dataset;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;


@Service
@Path("/store")
public class ServletDatastore
{
	@Resource(name="entityManagerFactory")
	private EntityManagerFactory emf;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/test")
	public String test() {
		return "{}";
	}

	
	/**
	 * Create a new service based on the given configuration
	 * 
	 * @param json
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/put")
	public String createInstance(@FormParam("data") String json)
	{
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Gson gson = new Gson();
		Dataset dataset = gson.fromJson(json, Dataset.class);
		
		System.out.println(json);
		System.out.println(dataset);

		em.persist(dataset);

		em.flush();
		em.getTransaction().commit();
		em.close();

		return "{}";
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete")
	public String deleteDataset(@FormParam("id") Long id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Dataset config = em.find(Dataset.class, id);
		em.remove(config);
		
		em.getTransaction().commit();
		em.close();
				
				
		return "{}";
	}

}
