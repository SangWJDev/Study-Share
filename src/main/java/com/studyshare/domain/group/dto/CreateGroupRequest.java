package com.studyshare.domain.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupRequest {


    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 1, max = 4)
    private int maxMembers;
    
}
