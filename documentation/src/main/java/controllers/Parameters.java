package controllers;

import org.ow2.chameleon.wisdom.api.DefaultController;
import org.ow2.chameleon.wisdom.api.annotations.Controller;
import org.ow2.chameleon.wisdom.api.annotations.Parameter;
import org.ow2.chameleon.wisdom.api.annotations.Path;
import org.ow2.chameleon.wisdom.api.annotations.Route;
import org.ow2.chameleon.wisdom.api.http.HttpMethod;
import org.ow2.chameleon.wisdom.api.http.Result;

@Controller
@Path("/parameters")
public class Parameters extends DefaultController {

    // tag::path-parameter[]
    @Route(method= HttpMethod.GET, uri="/{id}")
    public Result get(@Parameter("id") String id) {
        // The value of id is computed from the {id} url fragment.
        return ok(id);
    }
    // end::path-parameter[]

    // tag::query-parameter[]
    @Route(method= HttpMethod.GET, uri="/")
    public Result get2(@Parameter("id") String id) {
        // The value of id is computed from the id parameter of the query.
        // In /?id=foo it would get the "foo" value.
        return ok(id);
    }
    // end::query-parameter[]
}