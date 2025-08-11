package com.example.mySecurityProject.services;

import com.example.mySecurityProject.config.PasswordEncoderConfig;
import com.example.mySecurityProject.domain.user.User;
import com.example.mySecurityProject.dto.RegistrationUserDto;
import com.example.mySecurityProject.dto.UserDto;
import com.example.mySecurityProject.exeptions.UserAlreadyExistExeption;
import com.example.mySecurityProject.exeptions.UserNotFoundExeption;
import com.example.mySecurityProject.exeptions.UsersNotFounExeptions;
import com.example.mySecurityProject.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoderConfig passwordEncoderConfig;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        user.setName(registrationUserDto.getName());
        user.setPassword(passwordEncoderConfig.passwordEncoder().encode(registrationUserDto.getPassword()));
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }

//    public List<User> getAllUsers() throws UsersNotFounExeptions {
//
//        List<User> users = userRepository.findAll();
//        if (users.isEmpty()) {
//            throw new UsersNotFounExeptions("Users not found");
//        }
//        return users;
//    }

    public List<UserDto> getAllUsers() throws UsersNotFounExeptions {

        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {throw new UsersNotFounExeptions("Users not found");}
        List<UserDto> userDtos = users.stream()
                .map(user -> new UserDto(user.getId(), user.getUsername(), user.getName())) // Assuming UserDto has a constructor that takes User
                .collect(Collectors.toList());
        return userDtos;
    }

//    public UserDto createNewUser(User user) throws UserAlreadyExistExeption {
//        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
//            throw new UserAlreadyExistExeption("User already exist");
//        }
//        return UserDto.toModel(userRepository.save(user));
//    }



    public UserDto getById(Long userId) throws UserNotFoundExeption {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundExeption("User not found with ID: " + userId));
        return new UserDto(existingUser.getId(), existingUser.getUsername(), existingUser.getName());
    }
}
