package org.aksw.rdf_dataset_catalog.web.api;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.aksw.rdf_dataset_catalog.model.Dataset;
import org.aksw.rdf_dataset_catalog.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;


@Service
@Path("/store")
@Transactional
public class ServletDatastore
{
	@PersistenceContext
	private EntityManager em;

    @Autowired
    private PasswordEncoder passwordEncoder;
	
    @Resource(name="authService")
    private UserDetailsService userDetailsService;

    
    private Gson gson = new Gson();
    
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
    public String registerUser(@FormParam("username") String username, @FormParam("password") String rawPassword, @FormParam("email") String email) {
	    UserInfo userInfo = new UserInfo();
	    userInfo.setUsername(username);
	    userInfo.setEmail(email);

	    // TODO Make sure the email does not already exist - right now 
	    
	    String encodedPassword = passwordEncoder.encode(rawPassword);
	    userInfo.setPassword(encodedPassword);

	    em.persist(userInfo);
	    em.flush();
	    
	    
	    login(username, rawPassword);
	    
	    //Gson gson = new Gson();
	    String result = gson.toJson(userInfo);
	    
	    return result;
	}

	
	public UserInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Object details = auth.getPrincipal();
        
        UserInfo result;
        if(!auth.isAuthenticated() || !(details instanceof UserInfo)) {
            result = null;
        } else {
            result = (UserInfo)details;
        }
	    
        return result;
	}
	
	public String getUserJson(UserInfo user) {
        String result = user == null ? "{\"isAuthenticated\": false}" : gson.toJson(user);  

        return result;	    
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/checkSession")
	public String checkSession() {
	    
	    UserInfo user = getUserInfo();	    
	    String result = getUserJson(user);  

	    return result;
	}

//	@Autowired
//	private AuthenticationManager authenticationManager;
	
//	@Autowired
//	private ProviderManager authenticationManager;

//	@Inject
//	@InjectParam("org.springframework.security.authenticationManager")
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/logIn")
	public String login(@FormParam("username") String username, @FormParam("password") String password) {
	    Authentication request = new UsernamePasswordAuthenticationToken(username, password);
	    
	    Authentication auth = authenticationManager.authenticate(request);
	    
	    SecurityContextHolder.getContext().setAuthentication(auth);

	    
        UserInfo user = getUserInfo();      
        String result = getUserJson(user);
        
        return result;
	}
	
	private @Autowired HttpServletRequest request;

	@POST
    @Path("/logOut")
	public void logout() throws ServletException {
	    request.logout();
	    
	    SecurityContextHolder.clearContext();

        //if (invalidateHttpSession) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        //}	    
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfo user = (UserInfo)auth.getPrincipal();

        Dataset dataset = gson.fromJson(json, Dataset.class);

        boolean isUpdate = false;

        // This is a request to update a dataset - get the old state
        if(dataset.getId() != null) {
            isUpdate = true;

            Dataset oldState = em.find(Dataset.class, dataset.getId());
            
            // Check if the user of the oldState equals the current user
            Long ownerId = oldState.getOwner().getId();
            
            if(!user.getId().equals(ownerId)) {
                throw new RuntimeException("Cannot update dataset of another owner");
            }
        }

		dataset.setOwner(user);
		
//		System.out.println(json);
//		System.out.println(dataset);
		em.persist(dataset);

		em.flush();

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
