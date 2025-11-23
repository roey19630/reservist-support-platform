package com.milufamilies.supportapp.service;

import com.milufamilies.supportapp.model.enums.HelpType;
import com.milufamilies.supportapp.model.enums.Region;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.model.enums.Role;
import com.milufamilies.supportapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


    //  פונקציות חדשות לשימוש האדמין
    public List<User> getPendingFamilies() {
        return userRepository.findByRoleAndApproved(Role.ROLE_FAMILY, false);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void approveUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setApproved(true);
            userRepository.save(user);
        });
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void rejectUser(User user) {
        userRepository.delete(user);
    }

    public void updateUserBasicInfo(Long id, User updatedUser) {
        userRepository.findById(id).ifPresent(existingUser -> {
            existingUser.setFullName(updatedUser.getFullName());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setIdNumber(updatedUser.getIdNumber());
            existingUser.setApproved(updatedUser.isApproved());
            existingUser.setRole(updatedUser.getRole());
            userRepository.save(existingUser);
        });
    }
    public List<User> filterUsers(String role, String approved, String keyword) {
        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .filter(u -> (role == null || role.isEmpty() || u.getRole().name().equals(role)))
                .filter(u -> (approved == null || approved.isEmpty() || String.valueOf(u.isApproved()).equals(approved)))
                .filter(u -> (keyword == null || keyword.isEmpty() ||
                        u.getFullName().toLowerCase().contains(keyword.toLowerCase()) ||
                        u.getEmail().toLowerCase().contains(keyword.toLowerCase())))
                .collect(Collectors.toList());
    }

    public List<User> findVolunteersByFilters(HelpType helpType, Region region) {
        List<User> allVolunteers = userRepository.findByRole(Role.ROLE_VOLUNTEER);

        return allVolunteers.stream()
                .filter(v -> helpType == null || v.getOffers().stream().anyMatch(o -> o.getHelpType() == helpType))
                .filter(v -> region == null || v.getOffers().stream().anyMatch(o -> o.getRegions().contains(region)))
                .toList();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public boolean idNumberExists(String idNumber) {
        return userRepository.findByIdNumber(idNumber).isPresent();
    }

}
