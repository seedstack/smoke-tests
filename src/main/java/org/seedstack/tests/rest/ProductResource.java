package org.seedstack.tests.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.seedstack.business.api.domain.Factory;
import org.seedstack.business.api.domain.Repository;
import org.seedstack.business.api.interfaces.assembler.FluentAssembler;
import org.seedstack.business.api.interfaces.assembler.ModelMapper;
import org.seedstack.seed.transaction.api.Transactional;
import org.seedstack.tests.domain.product.Product;

@Path("product")
public class ProductResource {

    @Inject
    Repository<Product, Long> repository;
    
    @Inject
    Factory<Product> factory;

    @Inject
    FluentAssembler fluentAssembler ;
    
    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ProductRepresentation product() {
        repository.persist(factory.create(1L));
        return fluentAssembler.assemble(repository.load(1L)).with(ModelMapper.class).to(ProductRepresentation.class);
    }
}
