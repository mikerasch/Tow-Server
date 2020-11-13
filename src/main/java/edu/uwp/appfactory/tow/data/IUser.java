package edu.uwp.appfactory.tow.data;

import java.io.Serializable;

public interface IUser extends Serializable {

     String getUUID();
     void setUUID(String UUID);

     String getEmail();
     void setEmail(String email);

     String getUsername();
     void setUsername(String username);

     String getPassword();
     void setPassword(String password);

     String getFirstname();
     void setFirstname(String firstname);

     String getLastname();
     void setLastname(String lastname);

     String getRoles();
     void setRoles(String role);

     int getResetToken();
     void setResetToken(int resetToken);

     String getResetDate();
     void setResetDate(String date);

     String getVerifyToken();
     void setVerifyToken(String verifyToken);

     String getVerifyDate();
     void setVerifyDate(String date);

     boolean getVerEnabled();
     void setVerEnabled(boolean verEnabled);
}
