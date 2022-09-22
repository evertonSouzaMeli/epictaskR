package br.com.fiap.epictaskapi.controller;

import br.com.fiap.epictaskapi.dto.UserDTO;
import br.com.fiap.epictaskapi.exception.InvalidArgumentException;
import br.com.fiap.epictaskapi.model.User;
import br.com.fiap.epictaskapi.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(userService.listAll(pageable));
    }

    @GetMapping("/{user_id}")
    public ResponseEntity getById(@PathVariable(name = "user_id") Long userId){
       return userService.findById(userId)
               .map( user -> ResponseEntity.ok(UserDTO.toDTO(user)))
               .orElseGet( () -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity insert(@RequestBody @Valid User user){
        try {
            return new ResponseEntity(userService.insert(user), HttpStatus.CREATED);
        }catch (InvalidArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{user_id}")
    public ResponseEntity update(@PathVariable(name = "user_id") Long userId, @RequestBody @Valid User user){
        try {
            return userService.update(userId, user)
                    .map( usr -> ResponseEntity.ok(UserDTO.toDTO(usr)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }catch (InvalidArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity delete(@PathVariable(name = "user_id") Long userId){
        try {
            userService.delete(userId);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
}
