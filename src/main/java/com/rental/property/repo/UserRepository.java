package com.rental.property.repo;
import com.rental.property.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    Optional<Object> findByEmail(@NotBlank(message = "Email cannot be blank") @Email(message = "Email should be valid") String email);
    Optional<User> findByMobileNo(Long mobileNo);
}