package com.rental.property.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.time.LocalDate;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalTransactionDto {

    private LocalDate startDate;
    private LocalDate endDate;
}