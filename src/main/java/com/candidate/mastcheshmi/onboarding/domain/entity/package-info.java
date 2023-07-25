
@GenericGenerator(
    name = IdGeneratorConstants.GENERATOR_NAME,
    type = SequenceStyleGenerator.class,
    parameters = {
        @Parameter(name = "prefer_sequence_per_entity", value = "true"),
        @Parameter(name = "initial_value", value = "1"),
        @Parameter(name = "increment_size", value = "5"),
        @Parameter(name = "optimizer", value = "pooled-lo")
    }
)

/**
 * <p>This package contains all the entities of the application. All the entities in this package can use a centralized
 * primary key generation strategy named {@link IdGeneratorConstants#GENERATOR_NAME}. This allows switching between different
 * primary key generation strategies without modifying the entities' code. </p>
 * <p>The "pooled-lo" optimizer is used for here that is a high-performance optimizer that reduces the
 * number of round-trips to the database for generating primary keys. </p>
 */
package com.candidate.mastcheshmi.onboarding.domain.entity;

import com.candidate.mastcheshmi.onboarding.domain.constant.IdGeneratorConstants;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;