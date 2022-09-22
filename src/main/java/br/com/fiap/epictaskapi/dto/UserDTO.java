package br.com.fiap.epictaskapi.dto;

import br.com.fiap.epictaskapi.model.Role;
import br.com.fiap.epictaskapi.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String name;

    private String email;

    private String password;

    @JsonIgnore
    private List<Role> roles;

    public static User toModel(UserDTO userDTO){
        return User.builder()
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();
    }

    public static UserDTO toDTO(User user){
        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles((List<Role>) user.getAuthorities())
                .build();
    }

    public static List<UserDTO> toDTO(List<User> users){
        return users.stream().map(UserDTO::toDTO).collect(Collectors.toList());
    }
}
