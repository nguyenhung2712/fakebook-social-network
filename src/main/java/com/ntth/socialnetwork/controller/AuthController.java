package com.ntth.socialnetwork.controller;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntth.socialnetwork.exceptions.TokenRefreshException;
import com.ntth.socialnetwork.entity.ConfirmationToken;
import com.ntth.socialnetwork.entity.Districts;
import com.ntth.socialnetwork.entity.ERole;
import com.ntth.socialnetwork.entity.Location;
import com.ntth.socialnetwork.entity.Provinces;
import com.ntth.socialnetwork.entity.RefreshToken;
import com.ntth.socialnetwork.entity.Role;
import com.ntth.socialnetwork.entity.User;
import com.ntth.socialnetwork.entity.UserProfile;
import com.ntth.socialnetwork.entity.Wards;
import com.ntth.socialnetwork.payload.request.LoginRequest;
import com.ntth.socialnetwork.payload.request.SignupRequest;
import com.ntth.socialnetwork.payload.request.TokenRefreshRequest;
import com.ntth.socialnetwork.payload.response.JwtResponse;
import com.ntth.socialnetwork.payload.response.MessageResponse;
import com.ntth.socialnetwork.payload.response.TokenRefreshResponse;
import com.ntth.socialnetwork.repository.ConfirmationTokenRepository;
import com.ntth.socialnetwork.repository.DistrictsRepository;
import com.ntth.socialnetwork.repository.LocationRepository;
import com.ntth.socialnetwork.repository.ProvincesRepository;
import com.ntth.socialnetwork.repository.RoleRepository;
import com.ntth.socialnetwork.repository.UserProfileRepository;
import com.ntth.socialnetwork.repository.UserRepository;
import com.ntth.socialnetwork.repository.WardsRepository;
import com.ntth.socialnetwork.security.jwt.JwtUtils;
import com.ntth.socialnetwork.security.services.EmailService;
import com.ntth.socialnetwork.security.services.RefreshTokenService;
import com.ntth.socialnetwork.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;
  
  @Autowired
  UserProfileRepository profileRepository;
  
  @Autowired
  RoleRepository roleRepository;
  
  @Autowired
  ConfirmationTokenRepository confirmTokenRepository;
  
  @Autowired
  ProvincesRepository provinceRepository;
  
  @Autowired
  DistrictsRepository districtRepository;
  
  @Autowired
  WardsRepository wardRepository;
  
  @Autowired
  LocationRepository locationRepository;
  
  @Autowired
  EmailService emailService;
  
  @Autowired
  RefreshTokenService refreshTokenService;
  
  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
	  
	  Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());
	  
	  if (user.get().isEnabled()) {
		  Authentication authentication = authenticationManager.authenticate(
				  new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		  SecurityContextHolder.getContext().setAuthentication(authentication);
		  String jwt = jwtUtils.generateJwtToken(authentication);
	    
		  UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
		  List<String> roles = userDetails.getAuthorities().stream()
				 .map(item -> item.getAuthority())
				 .collect(Collectors.toList());
	    
		  RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		  return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
				  userDetails.getUsername(), userDetails.getEmail(), roles));
	  } else {
		  return ResponseEntity
				  .badRequest()
		          .body(new MessageResponse("Your account must be verified by email!"));
	  }
	  
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
	  
	  Provinces province = provinceRepository.findByCode(signUpRequest.getProvinceCode());
	  Districts district = districtRepository.findByCode(signUpRequest.getDistrictCode());
	  Wards ward = wardRepository.findByCode(signUpRequest.getWardCode());
	  
	  Location location = locationRepository.save(
			  new Location(province, district, ward, signUpRequest.getAddress()));
	  
	  Date currDate = new java.sql.Date(System.currentTimeMillis());
	  
	  if (userRepository.existsByUsername(signUpRequest.getUsername())) {
		  return ResponseEntity
			 .badRequest()
          	 .body(new MessageResponse("Error: Username is already taken!"));
	  }

	  if (userRepository.existsByEmail(signUpRequest.getEmail())) {
		  return ResponseEntity
             .badRequest()
             .body(new MessageResponse("Error: Email is already in use!"));
      }

	  User user = new User(signUpRequest.getUsername(), 
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()), currDate, false);

	  Set<String> strRoles = signUpRequest.getRole();
	  Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    User newUser = userRepository.save(user);
    
    profileRepository.save(new UserProfile(signUpRequest.getFirstName(), signUpRequest.getLastName(), 
    		new Long(1), new Date(0, 0, 0), 
    		"user.png", "background.png", "", currDate, user, location));
    
    ConfirmationToken confirmationToken = new ConfirmationToken(newUser);
    confirmTokenRepository.save(confirmationToken);
    
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(user.getEmail());
    mailMessage.setSubject("Complete Registration!");
    mailMessage.setFrom("hung.n.61cnttclc@ntu.edu.vn");
    mailMessage.setText("To confirm your account, please click here : "
    		+"http://localhost:3000/confirm-account/"+confirmationToken.getConfirmationToken());

    emailService.sendEmail(mailMessage);
    
	  return ResponseEntity.ok(new MessageResponse("User registered successfully! Please check your mail to confirm your account!"));
  }
  
  @GetMapping("/user/{token}")
  public ResponseEntity<?> getUserByToken(@PathVariable("token") String confirmationToken) {
	  try {
		  User user = userRepository.findByConfirmToken(confirmationToken);
		  return new ResponseEntity<>(user, HttpStatus.OK);
	  } catch (Exception e) {
		  return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	  }
  }
  
  
  @PostMapping("/confirm-account/{token}")
  public ResponseEntity<?> confirmUserAccount(@PathVariable("token") String confirmationToken) {
	  
	  ConfirmationToken token = 
			  confirmTokenRepository.findByConfirmationToken(confirmationToken);
	  
	  if (token != null) {
		  User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
		  user.setEnabled(true);
		  userRepository.save(user);
	  }
	  
	  return ResponseEntity.ok(new MessageResponse("Account confirmed! 5s redirect to login..."));
  }
  
  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
	        .map(refreshTokenService::verifyExpiration)
	        .map(RefreshToken::getUser)
	        .map(user -> {
	          String token = jwtUtils.generateTokenFromUsername(user.getUsername());
	          return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
	        })
        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
            "Refresh token is not in database!"));
  }
}