package ru.javawebinar.topjava.model;

public class AuthorizedEntity extends BaseEntity {
    private Integer authorizationId;

    public AuthorizedEntity() {
        this(null, null);
    }

    public AuthorizedEntity(Integer authorizationId) {
        this(null, authorizationId);
    }

    public AuthorizedEntity(Integer id, Integer authorizationId) {
        super(id);
        this.authorizationId = authorizationId;
    }

    public Integer getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(Integer authorizationId) {
        this.authorizationId = authorizationId;
    }
}
