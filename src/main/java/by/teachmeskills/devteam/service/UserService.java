package by.teachmeskills.devteam.service;

import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    public static final String ROLE_SELECTION_FORM = "inlineRadioOptions";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUserByAdmin(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void saveNewUser(User user, Map<String, String> formRoles) {
        user.setActive(true);
        user.setPrice(3);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        if (formRoles.get(ROLE_SELECTION_FORM).equals("option1")) {
            roles.add(Role.CUSTOMER);
        }
        if (formRoles.get(ROLE_SELECTION_FORM).equals("option2")) {
            roles.add(Role.MANAGER);
        }
        if (formRoles.get(ROLE_SELECTION_FORM).equals("option3")) {
            roles.add(Role.DEVELOPER);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        save(user);
    }

    public String getUserAppRoleName(User user) {
        Set<Role> roles = user.getRoles();
        if (roles.contains(Role.CUSTOMER)) {
            return "Заказчик";
        }
        if (roles.contains(Role.MANAGER)) {
            return "Менеджер";
        }
        if (roles.contains(Role.DEVELOPER)) {
            return "Разработчик";
        }
        return null;
    }

    public boolean updateProfile(User user, Map<String, String> formData) {
        String currentPassword = formData.get("currentPassword");
        if (currentPassword.isBlank() || !BCrypt.checkpw(currentPassword, user.getPassword())) {
            return false;
        }

        if (formData.get("password").isBlank()) {
            user.setPassword(passwordEncoder.encode(currentPassword));
        } else {
            user.setPassword(passwordEncoder.encode(formData.get("password")));
        }
        user.setFirstName(formData.get("firstName"));
        user.setLastName(formData.get("lastName"));
        user.setEmail(formData.get("email"));
        user.setContacts(formData.get("contacts"));
        user.setSkills(formData.get("skills"));
        save(user);
        return true;
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void deleteById(Long userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalStateException(String.format("User id:%s not found", id)));
    }

    public Set<User> getAllDevelopers() {
        List<User> allUsers = userRepository.findAll();
        Set<User> allDevelopers = new HashSet<>();
        for (User user : allUsers) {
            if (user.getRoles().contains(Role.DEVELOPER)) {
                allDevelopers.add(user);
            }
        }
        return allDevelopers;
    }

    public void updatePrice(Long id, Integer price) {
        User user = findById(id);
        user.setPrice(price);
        save(user);
    }
}
