package com.sirma.football_api.dto;

import jakarta.validation.constraints.NotBlank;

public class TeamCreateUpdateDto {

    @NotBlank(message = "name must not be blank")
    private String name;

    private String managerFullName;

    private String groupLetter;

    public TeamCreateUpdateDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManagerFullName() {
        return managerFullName;
    }

    public void setManagerFullName(String managerFullName) {
        this.managerFullName = managerFullName;
    }

    public String getGroupLetter() {
        return groupLetter;
    }

    public void setGroupLetter(String groupLetter) {
        this.groupLetter = groupLetter;
    }
}
