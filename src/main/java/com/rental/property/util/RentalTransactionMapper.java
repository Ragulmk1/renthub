package com.rental.property.util;
import com.rental.property.dto.RentalTransactionDto;
import com.rental.property.dto.RentalTransactionResponseDto;
import com.rental.property.dto.RentalTransactionTenantResponse;
import com.rental.property.entity.Property;
import com.rental.property.entity.RentalTransaction;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class RentalTransactionMapper {
    public static RentalTransactionDto toDTO(RentalTransaction rentalTransaction) {
        return RentalTransactionDto.builder()
                .startDate(rentalTransaction.getStartDate())
                .endDate(rentalTransaction.getEndDate())
                .build();
    }
    public static RentalTransaction toEntity(RentalTransactionDto dto) {
        Property property = new Property();
        return RentalTransaction.builder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .property(property)
                .build();
    }
    public static List<RentalTransactionDto> tenantListToTenantDtoList(List<RentalTransaction> rentalTransactionList){
        List<RentalTransactionDto> rentalTransactionDtoList = rentalTransactionList.stream().map(t->toDTO(t)).collect(Collectors.toList());
        return rentalTransactionDtoList;
    }
    public  static  List<RentalTransaction> tenantDtoListToTenantList(List<RentalTransactionDto> rentalTransactionDtoList){
        List<RentalTransaction> rentalTransactionList = rentalTransactionDtoList.stream().map(m->toEntity(m)).collect(Collectors.toList());
        return rentalTransactionList;
    }
    public static List<RentalTransactionResponseDto> convertToResponseDtoList(List<RentalTransaction> transactions) {
        return transactions.stream()
                .map(r->convertToResponseDto(r))
                .collect(Collectors.toList());
    }
    public static  RentalTransactionResponseDto convertToResponseDto(RentalTransaction transaction) {
        return RentalTransactionResponseDto.builder()
                .transactionId(transaction.getTransactionId())
                .propertyId(transaction.getProperty().getPropertyId())
                .address(transaction.getProperty().getAddress().getStreetName() + ", " + // Improved address formatting
                        transaction.getProperty().getAddress().getCity() + ", " +
                        transaction.getProperty().getAddress().getState() + " " +
                        transaction.getProperty().getAddress().getPinCode())
                .bhk(transaction.getProperty().getBhk())
                .status(transaction.getStatus())
                .startDate(transaction.getStartDate())
                .endDate(transaction.getEndDate()).image(transaction.getProperty().getImage1())
                .userName(transaction.getUser().getUsername()) // added username
                .mobileNo(String.valueOf(transaction.getUser().getMobileNo()))  // added mobile number
                .build();
    }
    public static List<RentalTransactionTenantResponse> convertToTenantResponseList(List<RentalTransaction> transactions) {
        return transactions.stream()
                .map(r->convertToTenantResponse(r))
                .collect(Collectors.toList());
    }
    public static  RentalTransactionTenantResponse convertToTenantResponse(RentalTransaction transaction) {
        return RentalTransactionTenantResponse.builder()
                .propertyId(transaction.getProperty().getPropertyId())
                .transactionId(transaction.getTransactionId())
                .address(transaction.getProperty().getAddress().getStreetName() + ", " + // Improved address formatting
                        transaction.getProperty().getAddress().getCity() + ", " +
                        transaction.getProperty().getAddress().getState() + " " +
                        transaction.getProperty().getAddress().getPinCode())
                .bhk(transaction.getProperty().getBhk())
                .description(transaction.getProperty().getDescription())
                .status(transaction.getStatus())
                .image(transaction.getProperty().getImage1())
                .build();
    }
}
