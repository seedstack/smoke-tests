package org.seedstack.smoke.fixtures.jpa;

import org.seedstack.business.domain.BaseAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AutoConfiguredEntity extends BaseAggregateRoot<Long> {
    @Id
    private Long clientId;
    private String name;

    protected AutoConfiguredEntity() {
    }

    public AutoConfiguredEntity(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public Long getEntityId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
