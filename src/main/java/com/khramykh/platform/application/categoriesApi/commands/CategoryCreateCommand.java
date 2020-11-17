package com.khramykh.platform.application.categoriesApi.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateCommand {
    private String name;
    private String description;
}
