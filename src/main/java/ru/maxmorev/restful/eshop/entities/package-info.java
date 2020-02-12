/**
 * Created by recommendation from the book
 * «Java Persistence with Hibernate, Second Edition»
 * https://www.manning.com/books/java-persistence-with-hibernate-second-edition
 */
@GenericGenerators({
        @GenericGenerator(
                name = Constants.ID_GENERATOR_COMMODITY,
                strategy = "enhanced-sequence",
                parameters = {
                        @Parameter(
                                name = "sequence_name",
                                value = Constants.ID_GENERATOR_COMMODITY_SEQUENCE_NAME
                        ),
                        @Parameter(
                                name = "initial_value",
                                value = "100"
                        ),
                        @Parameter(
                                name = "increment_size",
                                value = "10"
                        ),
                        @Parameter(
                                name = "optimizer",
                                value = "pooled-lo"
                        )
                }),
        @GenericGenerator(
                name = Constants.ID_GENERATOR_ATTRIBUTE,
                strategy = "enhanced-sequence",
                parameters = {
                        @Parameter(
                                name = "sequence_name",
                                value = Constants.ID_GENERATOR_ATTRIBUTE_SEQUENCE_NAME
                        ),
                        @Parameter(
                                name = "initial_value",
                                value = "100"
                        ),
                        @Parameter(
                                name = "increment_size",
                                value = "10"
                        ),
                        @Parameter(
                                name = "optimizer",
                                value = "pooled-lo"
                        )
                }),
        @GenericGenerator(
                name = Constants.ID_GENERATOR_ATTRIBUTE_VALUE,
                strategy = "enhanced-sequence",
                parameters = {
                        @Parameter(
                                name = "sequence_name",
                                value = Constants.ID_GENERATOR_ATTRIBUTE_VALUE_SEQUENCE_NAME
                        ),
                        @Parameter(
                                name = "initial_value",
                                value = "100"
                        ),
                        @Parameter(
                                name = "increment_size",
                                value = "10"
                        ),
                        @Parameter(
                                name = "optimizer",
                                value = "pooled-lo"
                        )
                }),
        @GenericGenerator(
                name = Constants.ID_GENERATOR_BRANCH,
                strategy = "enhanced-sequence",
                parameters = {
                        @Parameter(
                                name = "sequence_name",
                                value = Constants.ID_GENERATOR_BRANCH_SEQUENCE_NAME
                        ),
                        @Parameter(
                                name = "initial_value",
                                value = "100"
                        ),
                        @Parameter(
                                name = "increment_size",
                                value = "10"
                        ),
                        @Parameter(
                                name = "optimizer",
                                value = "pooled-lo"
                        )
                }),
        @GenericGenerator(
                name = Constants.ID_GENERATOR_BRANCH_ATTRIBUTE_SET,
                strategy = "enhanced-sequence",
                parameters = {
                        @Parameter(
                                name = "sequence_name",
                                value = Constants.ID_GENERATOR_BRANCH_ATTRIBUTE_SET_SEQUENCE_NAME
                        ),
                        @Parameter(
                                name = "initial_value",
                                value = "100"
                        ),
                        @Parameter(
                                name = "increment_size",
                                value = "10"
                        ),
                        @Parameter(
                                name = "optimizer",
                                value = "pooled-lo"
                        )
                }),
        @GenericGenerator(
                name = Constants.ID_GENERATOR_COMMODITY_TYPE,
                strategy = "enhanced-sequence",
                parameters = {
                        @Parameter(
                                name = "sequence_name",
                                value = Constants.ID_GENERATOR_COMMODITY_TYPE_SEQUENCE_NAME
                        ),
                        @Parameter(
                                name = "initial_value",
                                value = "100"
                        ),
                        @Parameter(
                                name = "increment_size",
                                value = "10"
                        ),
                        @Parameter(
                                name = "optimizer",
                                value = "pooled-lo"
                        )
                }),
        @GenericGenerator(
                name = Constants.ID_GENERATOR_CUSTOMER,
                strategy = "enhanced-sequence",
                parameters = {
                        @Parameter(
                                name = "sequence_name",
                                value = Constants.ID_GENERATOR_CUSTOMER_SEQUENCE_NAME
                        ),
                        @Parameter(
                                name = "initial_value",
                                value = "100"
                        ),
                        @Parameter(
                                name = "increment_size",
                                value = "10"
                        ),
                        @Parameter(
                                name = "optimizer",
                                value = "pooled-lo"
                        )
                }),
        @GenericGenerator(
                name = Constants.ID_GENERATOR_ORDER,
                strategy = "enhanced-sequence",
                parameters = {
                        @Parameter(
                                name = "sequence_name",
                                value = Constants.ID_GENERATOR_ORDER_SEQUENCE_NAME
                        ),
                        @Parameter(
                                name = "initial_value",
                                value = "100"
                        ),
                        @Parameter(
                                name = "increment_size",
                                value = "10"
                        ),
                        @Parameter(
                                name = "optimizer",
                                value = "pooled-lo"
                        )
                }),
        @GenericGenerator(
                name = Constants.ID_GENERATOR_SHOPPING_CART,
                strategy = "enhanced-sequence",
                parameters = {
                        @Parameter(
                                name = "sequence_name",
                                value = Constants.ID_GENERATOR_SHOPPING_CART_SEQUENCE_NAME
                        ),
                        @Parameter(
                                name = "initial_value",
                                value = "100"
                        ),
                        @Parameter(
                                name = "increment_size",
                                value = "10"
                        ),
                        @Parameter(
                                name = "optimizer",
                                value = "pooled-lo"
                        )
                }),
        @GenericGenerator(
                name = Constants.ID_GENERATOR_SHOPPING_CART_SET,
                strategy = "enhanced-sequence",
                parameters = {
                        @Parameter(
                                name = "sequence_name",
                                value = Constants.ID_GENERATOR_SHOPPING_CART_SET_SEQUENCE_NAME
                        ),
                        @Parameter(
                                name = "initial_value",
                                value = "100"
                        ),
                        @Parameter(
                                name = "increment_size",
                                value = "10"
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

