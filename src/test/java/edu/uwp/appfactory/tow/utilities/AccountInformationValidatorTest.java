package edu.uwp.appfactory.tow.utilities;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static edu.uwp.appfactory.tow.utilities.AccountInformationValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccountInformationValidatorTest {

    @Test
    void onCorrectEmailReturnTrue() {
        // emails by nature aren't regex-friendly so this is the best that can be done
        // given
        List<String> emails = List.of(
                "test@gmail.com",
                "test-mail@gmail.com",
                "test-mailz@gmail.co.com",
                "test_hello@gmail.com",
                "email@sub.google.com",
                "firstname.lastname@gmail.com"

        );
        boolean isValid = true;
        // when
        for(String email: emails){
            isValid = validateEmail(email);
            if(!isValid){
                break;
            }
        }
        
        // then
        assertTrue(isValid);
    }

    @Test
    void onIncorrectEmailReturnFalse(){
        // given
        List<String> emails = List.of(
                "very.unusual.'@'.hello@gmail.com",
                "plain",
                "@example.com",
                "Joe hello @ gmail.com",
                "Joe hello@gmail.com",
                "email@-gmail.com",
                "a...@example.com",
                "あいうえお@example.com",
                "just”not”right@example.com",
                "this\\ is\"really\"not\\allowed@example.com"
        );
        List<Boolean> isFalse = new ArrayList<>();

        // when
        for(String email: emails){
            isFalse.add(validateEmail(email));
        }

        // then
        assertThat(isFalse).containsOnly(false);
    }

    @Test
    void onCorrectPasswordReturnTrue() {
        // given
        List<String> passwords = List.of(
                "dingledart123A!",
                "b+cr+5odR$PHlmochuRi",
                "0e?osw2mafr8&reTupHe",
                "n+Ta9p_JL0Hep0Ak#&o*",
                "RAyISt7SpLTruDRu?rlg",
                "helloA!123goodbye",
                "putYourPasswordHere193l!"
        );

        // when
        boolean isValid = true;
        for(String password: passwords){
            List<String> problems = validatePassword(password);
            if(!problems.isEmpty()){
                isValid = false;
                break;
            }
        }

        // then
        assertTrue(isValid);
    }

    @Test
    void onIncorrectPasswordReturnFalse(){
        // given
        List<String> passwords = List.of(
                "123456",
                "password",
                "12345",
                "qewrty",
                "password1",
                "princess",
                "letmein",
                "monkey",
                "asdfasksf1",
                "superman",
                "maybeALegitPassword12353ksafiwe",
                "!totallyCool"
        );
        List<Boolean> isFalse = new ArrayList<>();

        // when
        for(String password: passwords){
            List<String> problems = validatePassword(password);
            if(problems.isEmpty()){
                isFalse.add(true);
            }
            else{
                isFalse.add(false);
            }
        }

        // then
        assertThat(isFalse).containsOnly(false);
    }
}