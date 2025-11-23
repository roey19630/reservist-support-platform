package com.milufamilies.supportapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegisterVolunteerDto {

    @NotBlank(message = "כתובת אימייל היא שדה חובה")
    @Email(message = "כתובת אימייל לא תקינה")
    private String email;

    @NotBlank(message = "סיסמה היא שדה חובה")
    @Size(min = 6, message = "הסיסמה חייבת להיות לפחות 6 תווים")
    private String password;

    @NotBlank(message = "שם מלא הוא שדה חובה")
    @Size(min = 2, message = "שם מלא חייב להכיל לפחות 2 תווים")
    private String fullName;

    @NotBlank(message = "מספר טלפון הוא שדה חובה")
    @Pattern(regexp = "^[0-9]{9,10}$", message = "מספר טלפון לא תקין")
    private String phone;

    @NotBlank(message = "תעודת זהות היא שדה חובה")
    @Pattern(regexp = "^[0-9]{9}$", message = "תעודת זהות לא תקינה")
    private String idNumber;

    private MultipartFile image;
}
