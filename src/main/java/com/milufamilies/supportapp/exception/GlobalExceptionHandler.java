package com.milufamilies.supportapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception ex, Model model) {
        // ניסיון לקבל את קוד השגיאה
        Object statusCodeObj = request.getAttribute("jakarta.servlet.error.status_code");
        int statusCode = (statusCodeObj instanceof Integer) ? (Integer) statusCodeObj : 500;

        // יצירת stack trace כמחרוזת
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String stackTrace = sw.toString();

        // הדפסה ללוג
        log.error("❌ שגיאה נתפסה: {}", ex.getMessage(), ex);

        // הוספת פרטים למודל כדי שיוצגו בעמוד השגיאה
        model.addAttribute("status", statusCode);
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("stackTrace", stackTrace); // החלק שמוצג בטופס

        return "error";
    }
}
