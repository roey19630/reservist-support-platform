package com.milufamilies.supportapp.dto;

import com.milufamilies.supportapp.model.enums.SoldierRelation;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterFamilyDto {

    @NotBlank(message = "שם מלא הוא שדה חובה")
    @Size(min = 2, message = "שם מלא חייב להיות לפחות 2 תווים")
    private String fullName;

    @NotBlank(message = "אימייל הוא שדה חובה")
    @Email(message = "כתובת אימייל לא תקינה")
    private String email;

    @NotBlank(message = "סיסמה היא שדה חובה")
    @Size(min = 6, message = "הסיסמה חייבת להיות לפחות 6 תווים")
    private String password;

    @NotBlank(message = "מספר טלפון הוא שדה חובה")
    @Pattern(regexp = "^0[0-9]{8,9}$", message = "מספר טלפון לא תקין")
    private String phone;

    @NotBlank(message = "תעודת זהות היא שדה חובה")
    @Pattern(regexp = "^[0-9]{9}$", message = "תעודת זהות לא תקינה")
    private String idNumber;

    @NotBlank(message = "שם החייל הוא שדה חובה")
    private String soldierName;

    @NotNull(message = "יש לבחור את הקשר לחייל")
    private SoldierRelation soldierRelation;
}
