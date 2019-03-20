package com.zookeeper_utils.configuration_server.rest;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.service.ZookeeperServicePropertiesInterface;
import com.zookeeper_utils.configuration_server.service.annotations.ZKServicePropertiesRequestScoped;
import com.zookeeper_utils.configuration_server.utils.JackJsonUtils;

@Path("/parametrosGlobalReq")
@RequestScoped
public class ZookeeperControllerGlobalReqScopedConfigProperties implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	@ZKServicePropertiesRequestScoped
	private	Instance<ZookeeperServicePropertiesInterface> zcInstance;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getParametersTree() throws ConfigPropertiesException, JsonProcessingException  {
		return JackJsonUtils.entityToJsonString(zcInstance.get().getPropertiesMap(true));
	}
	
	@GET
	@Path("/{key}")
//	@Path("{param:.*}/*")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(@PathParam("key") String key) throws ConfigPropertiesException, JsonProcessingException  {
		String realPath = StringUtils.join("/",key);
		String keyValue = zcInstance.get().getPropertyValue(realPath,true);
		zcInstance.destroy(zcInstance.get());
		return JackJsonUtils.createJsonLine(realPath, keyValue);
	}
}