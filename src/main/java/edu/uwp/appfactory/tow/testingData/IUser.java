package edu.uwp.appfactory.tow.testingData;

import java.io.Serializable;


public interface IUser extends Serializable {

     String getId();

     void setId(String id) ;

     String getEmail();

     String getUsername();

     void setUsername(String username);

     void setEmail(String email);

     String getPassword();

     void setPassword(String password);

     String getFirstname();

     void setFirstname(String firstname);

     String getLastname();

     void setLastname(String lastname);

     String getRoles(); //

     void setRoles(String role); //

     String getResetToken();

     void setResetToken(String resetToken);
}
