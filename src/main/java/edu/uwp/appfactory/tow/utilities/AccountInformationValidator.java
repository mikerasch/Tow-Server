package edu.uwp.appfactory.tow.utilities;

import org.passay.*;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Utility class for providing validation of email and password.
 */
public class AccountInformationValidator {
    private static final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final PasswordValidator VALIDATOR = new PasswordValidator(Arrays.asList(
            new LengthRule(8,30), // password must be 8 <-> 30
            new CharacterRule(EnglishCharacterData.UpperCase,1), // password must contain an uppercase
            new CharacterRule(EnglishCharacterData.Digit,1), // password must contain a digit
            new CharacterRule(EnglishCharacterData.Special,1), // password must contain a special character
            new IllegalSequenceRule(EnglishSequenceData.Numerical,5,false), // password must not contain 5 repeating numbers
            new WhitespaceRule() // password must not contain whitespace
    ));

    private AccountInformationValidator(){

    }
    /**
     * Validates email based on a strict regex pattern.
     * @param email - email to be validated
     * @return true if good email, false otherwise
     */
    public static boolean validateEmail(String email){
        if(email.length() > 50){
            return false;
        }
        return Pattern.compile(EMAIL_PATTERN)
                .matcher(email)
                .matches();
    }

    /**
     * Validates password based on a strict rule set.
     * See validator variable for rule set.
     * @param password - password to be validated
     * @return true if good password, false otherwise.
     */
    public static boolean validatePassword(String password){
        RuleResult result = VALIDATOR.validate(new PasswordData(password));
        return result.isValid();
    }
}
