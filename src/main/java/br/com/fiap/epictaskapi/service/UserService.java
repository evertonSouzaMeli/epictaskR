package br.com.fiap.epictaskapi.service;

import br.com.fiap.epictaskapi.dto.UserDTO;
import br.com.fiap.epictaskapi.exception.InvalidArgumentException;
import br.com.fiap.epictaskapi.model.User;
import br.com.fiap.epictaskapi.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserDTO> listAll(Pageable pageable){
        return UserDTO.toDTO(userRepository.findAll());
    }

    public Optional<User> findById(Long userId) throws RuntimeException  {
        return userRepository.findById(userId);
    }

    public User insert(User user) throws InvalidArgumentException {
        validateValues(user);
        encryptPassword(user);
        return userRepository.save(user);
    }

    public Optional<User> update(Long userId, User userUpdated) throws InvalidArgumentException {
        Optional<User> user = findById(userId).map(usr -> {
                    BeanUtils.copyProperties(userUpdated, usr);

                    if(Objects.nonNull(usr.getPassword()))
                        encryptPassword(usr);

                    return usr;
                });

        user.ifPresent(userRepository::save);

        return user;
    }

    public void delete(Long userId) throws UsernameNotFoundException {
        findById(userId)
                .ifPresentOrElse(userRepository::delete,
                                 () -> { throw new RuntimeException("User not found"); });
    }

    private void encryptPassword(User user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
    }

    private void validateValues(User user) throws InvalidArgumentException {
        if(Objects.isNull(user.getName()))
            throw new InvalidArgumentException("Username can't be null");

        if(Objects.isNull(user.getEmail()))
            throw new InvalidArgumentException("Email can't be null");

        if(!user.getEmail().matches("[a-zA-Z0-9.-]+@[a-zA-Z-]+\\.(com|edu|net)(\\.[a-z]{2})?$"))
            throw new InvalidArgumentException("Insert valid email");

        if(Objects.isNull(user.getPassword()))
            throw new InvalidArgumentException("Password can't be null");
    }
}
