package edu.uwp.appfactory.tow.utilities;

import org.passay.*;

import java.util.Arrays;
import java.util.regex.Pattern;

public class AccountInformationValidator {
    private static final String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final PasswordValidator validator = new PasswordValidator(Arrays.asList(
            new LengthRule(8,30), // password must be 8 <-> 30
            new CharacterRule(EnglishCharacterData.UpperCase,1), // password must contain an uppercase
            new CharacterRule(EnglishCharacterData.Digit,1), // password must contain a digit
            new CharacterRule(EnglishCharacterData.Special,1), // password must contain a special character
            new IllegalSequenceRule(EnglishSequenceData.Numerical,5,false), // password must not contain 5 repeating numbers
            new WhitespaceRule() // password must not contain whitespace
    ));
    public static boolean validateEmail(String email){
        return Pattern.compile(emailPattern)
                .matcher(email)
                .matches();
    }

    public static boolean validatePassword(String password){
        RuleResult result = validator.validate(new PasswordData(password));
        return result.isValid();
    }
}
