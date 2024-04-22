package com.alejodev.espacioactivo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de Reservation para la interaccion entre el cliente y el servidor.
 *
 * @author alejo
 * @version 1.0 21-04-2024
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationDTO implements EntityIdentificatorDTO {

    private Long id;
    private String date;
    private String time;
    private ActivityDTO activityDTO;
    private UserDTO userDTO;

}
