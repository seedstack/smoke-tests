package org.seedstack.smoke;

import org.junit.runner.JUnitCore;
import org.modelmapper.ModelMapper;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("tests")
public class TestsResource {
    private JUnitCore jUnitCore = new JUnitCore();
    private ModelMapper modelMapper = new ModelMapper();

    @GET
    @Path("/{testClass}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response runTest(@PathParam("testClass") String testClassName) {
        final Class<?> testClass;
        try {
            testClass = Class.forName(testClassName);
        } catch (ClassNotFoundException e) {
            throw new NotFoundException("Test class " + testClassName + " not found");
        }

        return Response.ok().entity(modelMapper.map(jUnitCore.run(testClass), TestResults.class)).build();
    }

}
