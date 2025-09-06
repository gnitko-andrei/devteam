package by.teachmeskills.devteam.service;

import by.teachmeskills.devteam.dto.user.UserDto;
import by.teachmeskills.devteam.dto.user.UserProfileUpdateDto;
import by.teachmeskills.devteam.dto.user.UserRegistrationDto;
import by.teachmeskills.devteam.dto.user.UserUpdateByAdminDto;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.exception.UserNotFoundException;
import by.teachmeskills.devteam.exception.UsernameAlreadyInUseException;
import by.teachmeskills.devteam.exception.WrongPasswordException;
import by.teachmeskills.devteam.mapper.UserMapper;
import by.teachmeskills.devteam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found. Username: %s".formatted(username)));
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::toUserDto).toList();
    }

    public List<UserDto> getAllUsersByRole(Role role) {
        return userRepository.findAllByRolesContainsOrderByUsernameAsc(role).stream().map(userMapper::toUserDto).toList();
    }

    public boolean isUserExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void createNewUser(UserRegistrationDto userRegistrationData) {
        var user = userMapper.toEntity(userRegistrationData);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(userRegistrationData.getPassword()));
        save(user);
    }

    public void updateUserByAdmin(Long userId, UserUpdateByAdminDto userUpdateData) {
        var user = getUserByIdOrThrow(userId);
        var newUsername = userUpdateData.getUsername();
        var currentUsername = user.getUsername();
        if (!StringUtils.isBlank(newUsername) && !Objects.equals(newUsername, currentUsername)) {
            if (isUserExists(newUsername)) {
                throw new UsernameAlreadyInUseException(newUsername);
            }
            user.setUsername(newUsername);
        }
        user.setRoles(userUpdateData.getRoles());
        userRepository.save(user);
    }

    public void updateUserProfile(Long userId, UserProfileUpdateDto userProfileUpdateData) {
        var user = getUserByIdOrThrow(userId);
        var currentPassword = userProfileUpdateData.getCurrentPassword();
        if (StringUtils.isBlank(currentPassword) || !passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new WrongPasswordException("Wrong current password provided!");
        }
        final var newPassword = userProfileUpdateData.getNewPassword();
        if (!StringUtils.isBlank(newPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword.trim()));
        }
        user.setFirstName(userProfileUpdateData.getFirstName());
        user.setLastName(userProfileUpdateData.getLastName());
        user.setEmail(userProfileUpdateData.getEmail());
        user.setContacts(userProfileUpdateData.getContacts());
        user.setSkills(userProfileUpdateData.getSkills());
        save(user);
    }

    public void updateDeveloperRate(Long id, Integer price) {
        var user = getUserByIdOrThrow(id);
        user.setPrice(price);
        save(user);
    }

    public void deleteById(Long userId) {
        var user = getUserByIdOrThrow(userId);
        userRepository.delete(user);
    }

    private User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private void save(User user) {
        userRepository.save(user);
    }
}
