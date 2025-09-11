package com.rental.property.repo;
import com.rental.property.dto.RentalTransactionResponseDto;
import com.rental.property.entity.Property;
import com.rental.property.entity.RentalTransaction;
import com.rental.property.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface RentalTransactionRepository extends JpaRepository<RentalTransaction, Long> {
    List<RentalTransaction> findByProperty(Property property);
    List<RentalTransaction> findAllByUser_Id(Long userId);
    @Query("SELECT rt FROM RentalTransaction rt JOIN rt.property p WHERE p.user.id = :landlordId")
    List<RentalTransaction> findByLandLordId(Long landlordId);

    List<RentalTransaction> findByUser(User user);

}
