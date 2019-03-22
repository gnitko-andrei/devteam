package by.teachmeskills.devteam.service;

import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

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
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        if (formRoles.get("inlineRadioOptions").equals("option1")) {
            roles.add(Role.CUSTOMER);
        }
        if (formRoles.get("inlineRadioOptions").equals("option2")) {
            roles.add(Role.MANAGER);
        }
        if (formRoles.get("inlineRadioOptions").equals("option3")) {
            roles.add(Role.DEVELOPER);
        }
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
        if (currentPassword == "" || !currentPassword.equals(user.getPassword())) {
            return false;
        }


        if (formData.get("password") == "") {
            user.setPassword(currentPassword);
        } else {
            user.setPassword(formData.get("password"));
        }
        user.setFirstName(formData.get("firstName"));
        user.setLastName(formData.get("lastName"));
        user.setEmail(formData.get("email"));
        user.setContacts(formData.get("contacts"));
        save(user);
        return true;
        //{username=manager, currentPassword=cp, password=np, firstName=manager, lastName=1,
        // email=manager@email, contacts=11111, userId=28, _csrf=a9801d4f-83ff-4875-891f-a6a47a969ff2}
    }
}
