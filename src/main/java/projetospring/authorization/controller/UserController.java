package projetospring.authorization.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import projetospring.authorization.controller.dtos.CreateUserDto;
import projetospring.authorization.entity.Role;
import projetospring.authorization.entity.User;
import projetospring.authorization.repository.RoleRepository;
import projetospring.authorization.repository.UserRepository;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    

    public UserController(UserRepository userRepository, RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @PostMapping("/user/cadastro")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto createUserDto ) {
        var userFromDb = userRepository.findByUsername(createUserDto.username());

        if(userFromDb.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        var role = roleRepository.findByNome(Role.Values.Basic.name());
        var user = new User();
        user.setUsername(createUserDto.username());
        user.setPassword(passwordEncoder.encode(createUserDto.password()));
        user.setRoles(Set.of(role));
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
    

}
