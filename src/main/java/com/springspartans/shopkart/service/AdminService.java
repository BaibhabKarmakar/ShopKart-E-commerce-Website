package com.springspartans.shopkart.service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springspartans.shopkart.model.Admin;
import com.springspartans.shopkart.repository.AdminRepository;
import com.springspartans.shopkart.util.PasswordEncoder;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;

@Service
public class AdminService {
	
	@Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private HttpSession httpSession;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    void addDemoAdmin() {
        if (adminRepository.findByEmail("admin@springspartans.com").isEmpty()) {
            Admin demoAdmin  = new Admin(0, "Demo Admin", passwordEncoder.encode("shopkart123@ADMIN") ,"admin@springspartans.com", "SECURITY123@ADMIN");
            adminRepository.save(demoAdmin);
        }
    }

    public boolean login(String email, String password , String security_key) {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent() && passwordEncoder.matches(password, admin.get().getPassword()) && security_key.equals(admin.get().getSecurity_key())) {
            httpSession.setAttribute("loggedInAdmin", admin.get());
            return true;
        }
        return false;
    }

    public void logout() {
        httpSession.invalidate();
    }
    public Admin getAdmin() {
        return (Admin) httpSession.getAttribute("loggedInAdmin");
    }
}
