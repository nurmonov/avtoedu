package org.example.fileupload.service;


import lombok.RequiredArgsConstructor;
import org.example.fileupload.entity.User;
import org.example.fileupload.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User topilmadi: " + phoneNumber));

        return user;
    }
}
