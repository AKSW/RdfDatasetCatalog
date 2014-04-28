package org.aksw.rdf_dataset_catalog.web.api;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.aksw.rdf_dataset_catalog.model.Dataset;
import org.aksw.rdf_dataset_catalog.model.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;


@Service
@Path("/store")
@Transactional
public class ServletDatastore
{
//	@Resource(name="entityManagerFactory")
//	private EntityManagerFactory emf;

	
	//@Autowired
	@PersistenceContext
	private EntityManager em;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/test")
	public String test() {
		return "{}";
	}

	
	/**
	 * Note: NEVER send the password in plain text - send a hash instead (and if possible, use HTTPS)
	 * 
	 * @param username
	 * @return
	 */
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/registerUser")
    public String registerUser(@FormParam("username") String username, @FormParam("password") String password, @FormParam("email") String email) {
	    UserInfo userInfo = new UserInfo();
	    userInfo.setUsername(username);
	    userInfo.setPasswordHash(password);
	    //userInfo.setEmail(email);
	    
	    em.persist(userInfo);
	    em.flush();
	    
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
//		EntityManager em = emf.createEntityManager();
//		em.getTransaction().begin();

		Gson gson = new Gson();
		Dataset dataset = gson.fromJson(json, Dataset.class);
		
		if(dataset.getOwner() != null) {
		    // Check if the claimed owner matches the one in the db
		    
		    // Check if the current owner equals the current user

		}
		
		
		UserInfo user = new UserInfo();
		user.setId(1l);
		/*
		user.setUsername("testuser");
		user.setPasswordHash("pwhash");
		user.setPasswordSalt("salt");
		*/
		dataset.setOwner(user);
		
		System.out.println(json);
		System.out.println(dataset);

		em.persist(dataset);

		em.flush();
//		em.getTransaction().commit();
//		em.close();

		return "{}";
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete")
	public String deleteDataset(@FormParam("id") Long id) {
//		EntityManager em = emf.createEntityManager();
//		em.getTransaction().begin();

		Dataset config = em.find(Dataset.class, id);
		em.remove(config);
		
//		em.getTransaction().commit();
//		em.close();
				
				
		return "{}";
	}

}
