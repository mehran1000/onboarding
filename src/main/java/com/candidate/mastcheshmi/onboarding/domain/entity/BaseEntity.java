package com.candidate.mastcheshmi.onboarding.domain.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

/**
 * This is the base class for entities that share common fields. Currently, it includes the 'version' field used for
 * Optimistic Locking. In the future, we may add some more fields to this class to include for example audit fields.
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Version
    private int version;
}
