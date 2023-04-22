package edu.uwp.appfactory.tow.requestobjects.rolerequest;

import java.util.UUID;

public record VerifyToken(UUID id, String email, String verifyToken, String verifyDate, boolean verEnabled) {
}
