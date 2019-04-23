package bachelorthesis.irinabilc.thesis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getUsers(){ return userRepository.findAll(); }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user){
        if (isValidPassword(user.getPassword()) && !containsUser(user))
            return userRepository.save(user);
        return null;
    }



    private Boolean containsUser(User user){
        for (User element:userRepository.findAll()){
            if (element.getUsername().equals(user.getUsername()))
                return true;
        }
        return false;
    }

    private Boolean isValidPassword(String password){
        return password.length() >= 8;
    }
}
