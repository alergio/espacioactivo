package com.alejodev.espacioactivo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestToCreateDisciplineDTO implements EntityIdentificatorDTO {

    private Long id;
    private UserDTO userDTO;
    private String disciplineName;
    private String disciplineType;
    private String status;

}
