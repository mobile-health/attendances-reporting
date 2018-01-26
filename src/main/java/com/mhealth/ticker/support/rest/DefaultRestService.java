package com.mhealth.ticker.support.rest;

import com.mhealth.ticker.support.data.Version;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 3/28/17.
 */
@Path("/")
public class DefaultRestService {

    private static final Logger logger = Logger.getLogger(DefaultRestService.class.getName());

    @Path("/version")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response version() {
        return Response.ok(Version.current()).build();
    }
}
