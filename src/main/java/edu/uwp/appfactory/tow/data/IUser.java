/**
 * interface for the user
 */
package edu.uwp.appfactory.tow.data;

import java.io.Serializable;


public interface IUser extends Serializable {

     String getUUID();

     void setUUID(String UUID) ;

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

     int getResetToken();

     void setResetToken(int resetToken);
}
