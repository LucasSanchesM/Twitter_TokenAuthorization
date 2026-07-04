package projetospring.authorization.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import projetospring.authorization.entity.Role;
import projetospring.authorization.entity.User;
import projetospring.authorization.repository.RoleRepository;
import projetospring.authorization.repository.UserRepository;

@Configuration
public class AdminUserConfig implements CommandLineRunner{

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    

    public AdminUserConfig(UserRepository userRepository, RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public void run(String... args) throws Exception {
        var roleAdmin = roleRepository.findByNome(Role.Values.Admin.name());
        var userAdmin = userRepository.findByUsername("Admin");
        userAdmin.ifPresentOrElse(
            
            user -> {
                    System.out.println("Usuario admin ja cadastrado");
                },
            () -> { var user = new User();
                    user.setUsername("Admin");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );
    }
}
