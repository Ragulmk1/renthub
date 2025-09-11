package com.rental.property.repo;
import com.rental.property.entity.Property;
import com.rental.property.entity.RentalTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PropertyRepository  extends JpaRepository<Property,Long> {
    @Query("SELECT p FROM Property p WHERE " +
            "(:city IS NULL OR LOWER(p.address.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
            "(:minPrice IS NULL OR p.rentAmount >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.rentAmount <= :maxPrice) AND " +
            "(:bhk IS NULL OR p.bhk = :bhk)")
    List<Property> searchProperties(String city, String maxPrice, String minPrice, String bhk);
    @Query("SELECT p from Property  p where p.user.id=:id")
    Page<Property> findPropertyByOwnerId(Long id, Pageable page);

}
