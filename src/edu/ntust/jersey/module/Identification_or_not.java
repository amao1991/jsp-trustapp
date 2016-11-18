package edu.ntust.jersey.module;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import edu.ntust.notary.Global;

@Path("or_not")
public class Identification_or_not {
	@Context
	ServletContext context;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String identification_or_not(@Context HttpServletRequest request) throws Exception{
		String returnString = null;
		JSONObject data = new JSONObject();
		
		// 時間判別, 
		// time = 0, 時間後, 
		// time = 1, 時間前, 
		// time = 2, 時間區間
		// time = 3, 不辨識
		
		//data.put("id", true);
		data.put("time", Integer.parseInt(Global.time_restrict_id));
		
		if (Integer.parseInt(Global.count_or_not) == 1) {
			data.put("count", true);
		}
		else {
			data.put("count", false);
		}
		System.out.println(data);
		returnString = data.toString();
		
		return returnString;
	}
}
