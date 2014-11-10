package com.gesaracino.gcm.boundary;

import javax.ejb.EJBException;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Gerardo Saracino on 07/11/2014.
 */

@Provider
public class EJBExceptionHandler implements ExceptionMapper<EJBException> {
    @Override
    public Response toResponse(EJBException exception) {
        if(exception.getCause() instanceof NoResultException) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No result found: " + exception.getCause().getMessage()).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity("An error occurred while processing your request: " + exception.getCause().getMessage()).build();
    }
}
