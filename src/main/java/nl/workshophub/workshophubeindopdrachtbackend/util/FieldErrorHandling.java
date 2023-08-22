package nl.workshophub.workshophubeindopdrachtbackend.util;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class FieldErrorHandling {

    public static String getErrorToStringHandling (BindingResult bindingResult){
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : bindingResult.getFieldErrors()){
            sb.append("Field error: " + fe.getField() + ": ");
            sb.append(fe.getDefaultMessage());
            sb.append("\n");
        }
        return sb.toString();
    }
}
