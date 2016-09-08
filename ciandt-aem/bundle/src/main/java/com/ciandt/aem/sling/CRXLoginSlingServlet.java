package com.ciandt.aem.sling;

import java.io.IOException;

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;


@SlingServlet(
        paths = { "/bin/services/crx/login" },
        methods = { "GET", "POST" }
)
public class CRXLoginSlingServlet extends SlingAllMethodsServlet{
	
	private static final long serialVersionUID = 1L;
	
	@Reference
	protected SlingRepository repository;
	   
	@Reference
    private ResourceResolverFactory resolverFactory;
	
	private JSONObject result;
	
	
	@Override
    protected final void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) 
    throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		response.setHeader("Access-Control-Max-Age", "1209600");
		
		String login 	= request.getParameter("login");
		String password = request.getParameter("password");
		
		result 	= new JSONObject();

		try{

			Session session = repository.login(new SimpleCredentials(login, password.toCharArray()));
			
			result 	= new JSONObject();
			result.put("msg", "Usuario " + session.getUserID() + " autenticado com sucesso.");
			result.put("status", SlingHttpServletResponse.SC_OK);
	 	   
			
			session.logout();	
			
		}catch(Exception e){
			e.printStackTrace();
			
			try{
				result.put("msg", "Erro ao efetuar autenticação do usuário");
				result.put("status", SlingHttpServletResponse.SC_BAD_REQUEST);

			}catch(JSONException jsex){
				jsex.printStackTrace();
			}	
		}

		response.getWriter().print(result.toString());
        response.getWriter().close();
    }
}
