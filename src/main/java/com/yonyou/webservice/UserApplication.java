package com.yonyou.webservice;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(value = "/user")
@WebService(endpointInterface="Test",serviceName="handler")
public interface UserApplication {

	@GET
	@Path("/sayHello/{name}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<UserVO> sayHello(@PathParam("name") String name);
	
	@PUT
	@Path("/{name}|{age}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public int insert(@PathParam("name") String name,@PathParam("age") int age);
	
	@DELETE
	@Path("/{name}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public int delete(@PathParam("name") String name);
	
	@POST
	@Path("/{name}|{age}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public int add(@PathParam("name") String name,@PathParam("age") int age);
	
	@POST
	@Path("/add")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public int adds(String name,String age);
}
