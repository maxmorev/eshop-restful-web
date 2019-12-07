/**
 * Created by recommendation from the book
 * «Java Persistence with Hibernate, Second Edition»
 * https://www.manning.com/books/java-persistence-with-hibernate-second-edition
 */
@GenericGenerators({
   @GenericGenerator(
      name = Constants.ID_GENERATOR,
      strategy = "enhanced-sequence",
      parameters = {
         @Parameter(
            name = "sequence_name",
            value = Constants.ID_GENERATOR_SEQUENCE_NAME
         ),
         @Parameter(
            name = "initial_value",
            value = "100"
         ),
         @Parameter(
            name = "increment_size",
            value = "100"
         ),
         @Parameter(
            name = "optimizer",
            value = "pooled-lo"
         )
      })
})

package ru.maxmorev.restful.eshop.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.GenericGenerators;
import org.hibernate.annotations.Parameter;

