package com.example.demo.Service.Impl;

import com.example.demo.DTO.*;
import com.example.demo.Exception.BusinessValidationException;
import com.example.demo.Model.EntityUser;
import com.example.demo.Repositroy.UserRepository;
import com.example.demo.Security.JwtUtil;
import com.example.demo.Service.BaseService;
import com.example.demo.Service.UserService;
import io.netty.util.internal.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseService implements UserService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JavaMailSender mailSender;
    //    @Autowired
//    private MapperFacade mapper;
//    private final ModelMapper mapper; // Autowire the ModelMapper
//
//    public UserServiceImpl(ModelMapper mapper) {
//        this.mapper = mapper;
//    }

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,8}$"
    );
    @Value("${spring.mail.username}")
    private String fromEmail;

    public UserServiceImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public Object signUp(SignUpDTO signUpDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new BusinessValidationException(errorMessage);
        }
        if (signUpDTO.getFirstName() == null) {
            throw new BusinessValidationException("User firstName required");
        }
        if (signUpDTO.getLastName() == null) {
            throw new BusinessValidationException("User last name required");
        }
        if (signUpDTO.getEmail() == null) {
            throw new BusinessValidationException("User Email required");
        }
        if (mongoTemplate.exists(Query.query(Criteria.where("email").is(signUpDTO.getEmail())), EntityUser.class)) {
            throw new BusinessValidationException("This email id user already exists.");
        }
        if (signUpDTO.getPassword() == null) {
            throw new BusinessValidationException("User password required");
        }
        if (!PASSWORD_PATTERN.matcher(signUpDTO.getPassword()).matches()) {
            throw new BusinessValidationException("Password must contain 6 to 8 characters, including one uppercase letter, one lowercase letter, one special character, and no whitespace.");
        }
        if (StringUtil.isNullOrEmpty(signUpDTO.getConfirmPassword()) && !signUpDTO.getPassword().equals(signUpDTO.getConfirmPassword())) {
            throw new BusinessValidationException("Please enter the confirm password correct.");
        }
        if (StringUtil.isNullOrEmpty(signUpDTO.getMasterPassword())) {
            throw new BusinessValidationException("Please enter the master password to secure your notes");
        }
        if (StringUtil.isNullOrEmpty(signUpDTO.getConfirmMasterPassword())) {
            throw new BusinessValidationException("Please enter the confirm master password");
        }
        if (!signUpDTO.getConfirmMasterPassword().equals(signUpDTO.getMasterPassword())){
            throw new BusinessValidationException("Both master password is not match");
        }
        String userName = signUpDTO.getFirstName() + " " + signUpDTO.getLastName();
        signUpDTO.setPassword(getEncryptedPassword(signUpDTO.getPassword()));
        signUpDTO.setMasterPassword(getEncryptedPassword(signUpDTO.getMasterPassword()));
        EntityUser entityUser = signUpDTO.toModel(EntityUser.class, getMapper());
        entityUser.setUserName(userName);
        userRepository.save(entityUser);
        return entityUser.toDTO(SignUpResponseDTO.class, getMapper());

    }

    @Override
    public Object login(LoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new BusinessValidationException(errorMessage);
        }
        EntityUser entityUser = mongoTemplate.findOne(Query.query(Criteria.where(EntityUser.Fields.email).is(loginDTO.getEmail())), EntityUser.class);
        if (entityUser != null) {
            if (isPasswordValid(loginDTO.getPassword(), entityUser.getPassword())) {
                return jwtUtil.generateToken1(loginDTO.getEmail(), loginDTO.getPassword(), entityUser.getUserId());
            } else {
                throw new BusinessValidationException("Password is invalid");
            }
        } else {
            throw new BusinessValidationException("Email is invalid");
        }
    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new BusinessValidationException(errorMessage);
        }
        EntityUser entityUser = mongoTemplate.findOne(Query.query(Criteria.where(EntityUser.Fields.email).is(resetPasswordDTO.getEmail())), EntityUser.class);
        if (entityUser != null) {
            if (isPasswordValid(resetPasswordDTO.getPassword(), entityUser.getPassword())) {
                if (!PASSWORD_PATTERN.matcher(resetPasswordDTO.getNewPassword()).matches()) {
                    throw new BusinessValidationException("Password must contain 6 to 8 characters, including one uppercase letter, one lowercase letter, one special character, and no whitespace.");
                }
                entityUser.setPassword(getEncryptedPassword(resetPasswordDTO.getNewPassword()));
                mongoTemplate.save(entityUser);
            } else {
                throw new BusinessValidationException("Password is invalid");
            }
        } else {
            throw new BusinessValidationException("Email is invalid");
        }

    }

    @Override
    public void forgotPassword(ForgotPasswordDTO forgotPasswordDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new BusinessValidationException(errorMessage);
        }
        EntityUser entityUser = mongoTemplate.findOne(Query.query(Criteria.where(EntityUser.Fields.email).is(forgotPasswordDTO.getEmail())), EntityUser.class);
        if (entityUser != null) {
            String otp = generateOtp();
            sendOtpByEmail(entityUser.getEmail(), otp);
            entityUser.setOtp(otp);
            entityUser.setOptGenerateDate(new Date());
            userRepository.save(entityUser);
        } else {
            throw new BusinessValidationException("This is email id user not exists");
        }
    }

    @Override
    public void verifyOtpAndSetNewPassword(ForgotPasswordDTO forgotPasswordDTO, BindingResult bindingResult) {
        EntityUser entityUser = mongoTemplate.findOne(Query.query(Criteria.where(EntityUser.Fields.email).is(forgotPasswordDTO.getEmail())
                .and(EntityUser.Fields.otp).is(forgotPasswordDTO.getOtp())), EntityUser.class);
        if (entityUser != null) {

            LocalDateTime otpTimeStamp = LocalDateTime.ofInstant(entityUser.getOptGenerateDate().toInstant(), ZoneId.systemDefault());
            // Calculate the time difference between the current time and OTP generation timestamp
            LocalDateTime currentTime = LocalDateTime.now();
            Duration duration = Duration.between(otpTimeStamp, currentTime);
            long minutesPassed = duration.toMinutes();
            if (minutesPassed > 5) {
                throw new BusinessValidationException("OTP has expired. Please request a new OTP.");
            } else {
                if (forgotPasswordDTO.getPassword() == null) {
                    throw new BusinessValidationException("Password Required");
                }
                if (forgotPasswordDTO.getConfirmPassword() == null) {
                    throw new BusinessValidationException("Rewrite you password to confirm the password");
                }
                if (!PASSWORD_PATTERN.matcher(forgotPasswordDTO.getPassword()).matches()) {
                    throw new BusinessValidationException("Password must contain 6 to 8 characters, including one uppercase letter, one lowercase letter, one special character, and no whitespace.");
                }
                if (forgotPasswordDTO.getPassword().equals(forgotPasswordDTO.getConfirmPassword())) {
                    entityUser.setPassword(getEncryptedPassword(forgotPasswordDTO.getPassword()));
                    entityUser.setOtp(null);
                    userRepository.save(entityUser);
                } else {
                    throw new BusinessValidationException("Both password is not the same");
                }
            }
        } else {
            throw new BusinessValidationException("This is email id or otp is invalid");
        }
    }

    @Override
    public Object updateProfile(UpdateProfileDTO updateProfileDTO, BindingResult bindingResult) {
        EntityUser entityUser = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(updateProfileDTO.getId())), EntityUser.class);
        if (entityUser != null) {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException(errorMessage);
            }
            if (!StringUtil.isNullOrEmpty(updateProfileDTO.getFirstName())){
                entityUser.setFirstName(updateProfileDTO.getFirstName());
            }
            if (!StringUtil.isNullOrEmpty(updateProfileDTO.getLastName())){
                entityUser.setLastName(updateProfileDTO.getLastName());
            }
            if (!StringUtil.isNullOrEmpty(updateProfileDTO.getEmail())){
                entityUser.setEmail(updateProfileDTO.getEmail());
            }
            if (!StringUtil.isNullOrEmpty(updateProfileDTO.getOldMasterPassword()) && !StringUtil.isNullOrEmpty(updateProfileDTO.getMasterPassword())){
                if (!isPasswordMatch(updateProfileDTO.getOldMasterPassword(),entityUser.getMasterPassword())){
                    throw new BusinessValidationException("The old master password is not correct");
                }
                if (!StringUtil.isNullOrEmpty(updateProfileDTO.getConfirmMasterPassword()) && updateProfileDTO.getMasterPassword().equals(updateProfileDTO.getConfirmMasterPassword())){
                    entityUser.setMasterPassword(getEncryptedPassword(updateProfileDTO.getMasterPassword()));
                }
                else {
                    throw new BusinessValidationException("Please enter the correct confirm master password");
                }
            }
            entityUser.setUserName(entityUser.getFirstName() + " " + entityUser.getLastName());
            userRepository.save(entityUser);
        } else {
            throw new BusinessValidationException("User not found");
        }

        return entityUser.toDTO(SignUpResponseDTO.class, getMapper());
    }

    private void sendOtpByEmail(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Notes Password Manager Otp");
            message.setText("Your otp is " + otp + ", this OTP only valid for 5 minutes");
            mailSender.send(message);
        } catch (Exception e) {
            throw new BusinessValidationException(e.getLocalizedMessage());
        }
    }

    private String generateOtp() {
        String otpCharacters = "0123456789";
        int otpLength = 8;
        Random random = new Random();
        StringBuilder otp = new StringBuilder(otpLength);
        for (int i = 0; i < otpLength; i++) {
            int randomIndex = random.nextInt(otpCharacters.length());
            char otpChar = otpCharacters.charAt(randomIndex);
            otp.append(otpChar);
        }
        return otp.toString();
    }
}
