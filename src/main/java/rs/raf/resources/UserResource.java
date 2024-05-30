package rs.raf.resources;

import rs.raf.entities.User;
import rs.raf.request.LoginRequest;
import rs.raf.request.UserForChangeRequest;
import rs.raf.services.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/users")
public class UserResource {

    @Inject
    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response all() {
        return Response.ok(this.userService.allUsers()).build();
    }

    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("email") String email) {
        return Response.ok(this.userService.findUser(email)).build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginRequest loginRequest)
    {
        Map<String, String> response = new HashMap<>();

        String jwt = this.userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (jwt == null) {
            response.put("message", "These credentials do not match our records");
            return Response.status(422, "Unprocessable Entity").entity(response).build();
        }

        response.put("jwt", jwt);
     //   response.put("name", this.userService.findUser(loginRequest.getEmail()).getName());
     //   response.put("surname", this.userService.findUser(loginRequest.getEmail()).getSurname());
      //  response.put("email", loginRequest.getEmail());
        response.put("user_type", this.userService.findUser(loginRequest.getEmail()).getUserType().toString());


        return Response.ok(response).build();
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@Valid User user) {
        if(userService.findUser(user.getEmail()) != null){
            return Response.status(422, "Unprocessable Entity").entity("User with this email already exists").build();
        }

        return Response.ok(this.userService.register(user)).build();
    }

    @PUT
    @Path("/changeActivity/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeActivity(@PathParam("email") String email) {
        return Response.ok(this.userService.changeActivityForUser(email)).build();
    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@Valid UserForChangeRequest userForChangeRequest) {
        if (this.userService.findUser(userForChangeRequest.getNewEmail()) != null && !userForChangeRequest.getOldEmail().equals(userForChangeRequest.getNewEmail()))
            return Response.status(422, "Unprocessable Entity").entity("User with this email already exists").build();
        return Response.ok(this.userService.changeUserData(userForChangeRequest)).build();
    }


}
