package com.rental.property;
import com.rental.property.entity.Role;
import com.rental.property.entity.User;
import com.rental.property.repo.RoleRepository;
import com.rental.property.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;
@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class RentHubApplication implements CommandLineRunner {
    private  final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public static void main(String[] args) {
        SpringApplication.run(RentHubApplication.class, args);
        log.info("After running  my app");
    }
    @Override
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.findByName("ROLE_LANDLORD")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_LANDLORD");
                    return roleRepository.save(newRole);
                });
        Role userRole = roleRepository.findByName("ROLE_TENANT")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_TENANT");
                    return roleRepository.save(newRole);
                });
        if (!userRepository.findByUsername("landlord").isPresent()) {
            User landlord = new User();
            landlord.setUsername("landlord");
            landlord.setPassword(passwordEncoder.encode("landlord123"));
            landlord.setRoles(Set.of(adminRole));
            landlord.setEmail("Saveetha.S@cognizant.com");
            landlord.setFirstName("SAVEETHA");
            landlord.setLastName("S");
            landlord.setMobileNo(876534567L);
            landlord.setRole(1L);
            userRepository.save(landlord);
        }
        if (!userRepository.findByUsername("tenant").isPresent()) {
            User tenant = new User();
            tenant.setUsername("tenant");
            tenant.setPassword(passwordEncoder.encode("tenant123"));
            tenant.setRoles(Set.of(userRole));
            tenant.setEmail("Ragul.M5@cognizant.com");
            tenant.setFirstName("RAGUL");
            tenant.setLastName("M");
            tenant.setMobileNo(987654321L);
            tenant.setRole(2L);
            userRepository.save(tenant);
        }
    }
}
