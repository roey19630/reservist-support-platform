package com.milufamilies.supportapp.repository;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsByRole(com.milufamilies.supportapp.model.enums.Role role);

    long countByRoleAndApproved(Role role, boolean approved);

    List<User> findByRoleAndApproved(Role role, boolean b);
    List<User> findByRole(Role role);
    Optional<User> findByIdNumber(String idNumber);

}
