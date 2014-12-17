package com.gesaracino.gcm.server.boundary;

import javax.ejb.EJBException;
import javax.persistence.NoResultException;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Gerardo Saracino on 07/11/2014.
 */

@Provider
public class EJBExceptionHandler implements ExceptionMapper<EJBException> {
    @Override
    public Response toResponse(EJBException exception) {
    	Throwable cause = exception.getCause();
    	
        if(cause instanceof NoResultException) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No result found: " + cause.getMessage()).build();
        } else if(cause instanceof ValidationException) {
        	return Response.status(Status.BAD_REQUEST).entity("Fill all fields").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity("An error occurred while processing your request: " + exception.getCause().getMessage()).build();
    }
}
