
package rs.raf.filters;


import rs.raf.resources.UserResource;
import rs.raf.services.UserService;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;

@Provider
public class AuthFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Inject
    UserService userService;
    private Boolean isAdmin;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("AuthFilter - Request Filter");

        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
            System.out.println("OPTIONS request detected, aborting.");
            requestContext.abortWith(Response.ok().build());
            return;
        }

        if (!this.isAuthRequired(requestContext)) {
            System.out.println("Authentication not required for this request.");
            return;
        }

        try {
            String token = requestContext.getHeaderString("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.replace("Bearer ", "");
            }

            if (!this.userService.isAuthorized(token, isAdmin)) {
                System.out.println("Unauthorized request.");
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        } catch (Exception exception) {
            System.out.println("Error during authorization check: " + exception.getMessage());
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        System.out.println("AuthFilter - Response Filter");

        containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        containerResponseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        containerResponseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private boolean isAuthRequired(ContainerRequestContext req) {
        if (req.getUriInfo().getPath().contains("login")) {
            return false;
        }

        List<Object> matchedResources = req.getUriInfo().getMatchedResources();
        for (Object matchedResource : matchedResources) {
            if (matchedResource instanceof UserResource) {
                isAdmin = true;
                return true;
            }
        }
        isAdmin = false;
        return true;
    }
}