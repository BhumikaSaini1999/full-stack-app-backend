package com.fullstackapp.ecommerce.config;

import com.fullstackapp.ecommerce.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Value("${allowed.origins}")
    private String[] theAllowedOrigins;

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] theUnsupportedActions = { HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH };

        // disable Http Methods for Product: PUT, POST, DELETE
        disableHttpMethods(Product.class, config, cors, theUnsupportedActions);

        // disable Http Methods for ProductCategory: PUT, POST, DELETE
        disableHttpMethods(ProductCategory.class, config, cors, theUnsupportedActions);

        // disable Http Methods for Country: PUT, POST, DELETE
        disableHttpMethods(Country.class, config, cors, theUnsupportedActions);

        // disable Http Methods for State: PUT, POST, DELETE
        disableHttpMethods(State.class, config, cors, theUnsupportedActions);

        // disable Http Methods for Order: PUT, POST, DELETE
        disableHttpMethods(Order.class, config, cors, theUnsupportedActions);

        // call an internal helper method to expose id's for product-category
        exposeIds(config);

        //configure cors mapping
        cors.addMapping(config.getBasePath()+"/**").allowedOrigins(theAllowedOrigins);
    }

    private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, CorsRegistry cors, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration().
                forDomainType(theClass).
                withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)).
                withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        // expose entity id's for product-category to display menu

        // get a list of all entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // create an array of the entity types
        List<Class> entityClasses = new ArrayList<>();

        // get the entity types for the entities
        for(EntityType tempEntityType : entities){
            entityClasses.add(tempEntityType.getJavaType());
        }

        // expose the entity types for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
