package validator;

import annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class DtoValidator {
    private static final String REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";

    public List<String> validateDto(Object dto) throws IllegalAccessException {
        List<String> messages = new LinkedList<>();
        Field[] declaredFields = dto.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            if (declaredField.isAnnotationPresent(Length.class)) {
                StringBuilder lengthMessage = new StringBuilder();
                Length annotation = declaredField.getAnnotation(Length.class);
                String name = (String) declaredField.get(dto);
                if (name.length() < annotation.min() || name.length() > annotation.max()) {
                    lengthMessage.append("Invalid format : ")
                            .append(declaredField.getName())
                            .append(", ").append(" Min length: ")
                            .append(annotation.min()).append(" Max length: ")
                            .append(annotation.max()).append(" Actual: ")
                            .append(name);
                }
                messages.add(lengthMessage.toString());
            } else if (declaredField.isAnnotationPresent(Email.class)) {
                StringBuilder emailMessage = new StringBuilder();
                String email = (String) declaredField.get(dto);
                Pattern pattern = Pattern.compile(REGEX);
                if (!pattern.matcher(email).matches()) {
                    emailMessage.append("Invalid format for email " + " : ")
                            .append(email).append(" Actual :  someemail@domain.com");
                }
                messages.add(emailMessage.toString());
            } else if (declaredField.isAnnotationPresent(AdultHood.class)) {
                StringBuilder adultHoodMessage = new StringBuilder();
                LocalDate birthDate = (LocalDate) declaredField.get(dto);
                int years = Period.between(birthDate, LocalDate.now()).getYears();
                if (years < 20) {
                    adultHoodMessage.append("You haven't permission " + " your age : ")
                            .append(years).append(" Actual 20 or old");
                }
                messages.add(adultHoodMessage.toString());
            } else {
                StringBuilder minMaxMessage = new StringBuilder();
                int discountRate = (Integer) declaredField.get(dto);
                Annotation[] annotations = declaredField.getAnnotations();
                Min min = null;
                Max max = null;
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Min) {
                        min = (Min) annotation;
                    } else {
                        max = (Max) annotation;
                    }
                }
                if (min != null && (discountRate < min.value())) {
                    minMaxMessage.append("Discount Min must be greater than : ").append(min.value());
                }
                if (max != null && (discountRate > max.value())) {
                    minMaxMessage.append(" Discount Max value must be smaller than : ").append(max.value());
                }
                messages.add(minMaxMessage.toString());

            }
        }
        return messages;
    }
}
