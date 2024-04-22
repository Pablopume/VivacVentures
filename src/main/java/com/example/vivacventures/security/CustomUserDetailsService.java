package com.example.vivacventures.security;



import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.data.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUsername(username);
if (user == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(
                        user.getRol()

                               )
                .build();

    }
}
