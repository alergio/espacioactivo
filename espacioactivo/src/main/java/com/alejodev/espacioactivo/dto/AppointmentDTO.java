package com.alejodev.espacioactivo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de Appointment para la interaccion entre el cliente y el servidor.
 *
 * @author alejo
 * @version 1.0 21-04-2024
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentDTO implements EntityIdentificatorDTO {

    private Long id;
    private String date;
    private String time;
    private boolean isFull;
    private Integer maxPeople;
    private ActivityDTO activityDTO;
    private AppointmentStateDTO appointmentStateDTO;

}
