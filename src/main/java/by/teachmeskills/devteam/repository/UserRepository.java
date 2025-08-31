package by.teachmeskills.devteam.repository;

import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByRolesContainsOrderByUsernameAsc(Role role);

    List<User> findAllByOrderByUsernameAsc();
}
