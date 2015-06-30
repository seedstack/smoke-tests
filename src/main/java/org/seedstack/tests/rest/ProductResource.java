package org.seedstack.tests.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.seedstack.business.api.domain.Factory;
import org.seedstack.business.api.domain.Repository;
import org.seedstack.business.api.interfaces.assembler.FluentAssembler;
import org.seedstack.business.api.interfaces.assembler.ModelMapper;
import org.seedstack.seed.transaction.api.Transactional;
import org.seedstack.tests.domain.product.Product;

/**
 * REST Resource to create and load a {@link Product}.
 * @author thierry.bouvet@mpsa.com
 */
@Path("product")
public class ProductResource {

    @Inject
    Repository<Product, Long> repository;
    
    @Inject
    Factory<Product> factory;

    @Inject
    FluentAssembler fluentAssembler ;
    
    /**
     * Initialize products.
     * @return String content
     */
    @GET
    @Path("init")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public String init() {
        final Long products = 10L;
        for (Long i = 0L; i < products; i++) {
            repository.persist(factory.create(i));
        }
        return "Products created";
    }
    
    /**
     * Load a product.
     * @param id the {@link Product} id to load.
     * @return the {@link ProductRepresentation} loaded.
     */
    @GET
    @Path("load/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ProductRepresentation load(@PathParam("id") Long id) {
        return fluentAssembler.assemble(repository.load(id)).with(ModelMapper.class).to(ProductRepresentation.class);
    }

}
