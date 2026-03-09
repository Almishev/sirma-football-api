package com.sirma.football_api.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class UpdateRolesRequest {

    @NotEmpty(message = "At least one role is required")
    private List<String> roleNames;

    public UpdateRolesRequest() {}

    public UpdateRolesRequest(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public List<String> getRoleNames() { return roleNames; }
    public void setRoleNames(List<String> roleNames) { this.roleNames = roleNames; }
}
