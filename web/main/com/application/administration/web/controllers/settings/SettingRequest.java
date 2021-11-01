package com.application.administration.web.controllers.settings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel("Model Setting")
public class SettingRequest {

    @NotNull
    @ApiModelProperty(required = true)
    private String name;

    @NotNull
    @ApiModelProperty(required = true)
    private String description;

    @NotNull
    @ApiModelProperty(required = true)
    private String value;

    public SettingRequest(String name, String description, String value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }
}
