package com.peakform.config;

import com.peakform.model.Member;
import com.peakform.model.Role;
import com.peakform.model.User;
import com.peakform.repository.MemberRepository;
import com.peakform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(new User("admin1", passwordEncoder.encode("Admin@12345"), Role.ROLE_ADMIN));
            userRepository.save(new User("coach1", passwordEncoder.encode("Coach@12345"), Role.ROLE_COACH));
            userRepository.save(new User("client1", passwordEncoder.encode("Client@12345"), Role.ROLE_CLIENT));
        }

        if (memberRepository.count() == 0) {
            memberRepository.save(new Member("Margaret Chen", 78, "Torquay",
                    "m.chen@example.com",
                    "Recovering from cardiac surgery, avoid high-intensity sessions",
                    "Tuesday 10:00-11:00, home alone"));
            memberRepository.save(new Member("David Osei", 34, "Geelong",
                    "d.osei@example.com",
                    "No restrictions",
                    "Monday 06:00-07:00"));
            memberRepository.save(new Member("Priya Nair", 61, "Torquay",
                    "p.nair@example.com",
                    "Mild asthma, keep an inhaler nearby",
                    "Thursday 15:00-16:00, carer present"));
            memberRepository.save(new Member("Liam Fitzgerald", 29, "Ocean Grove",
                    "l.fitzgerald@example.com",
                    "No restrictions",
                    "Wednesday 18:00-19:00"));
        }
    }
}
