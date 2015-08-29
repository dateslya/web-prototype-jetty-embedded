package ru.teslya.prototype.security;

import java.util.Collections;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;

public class BasicAuthenticationHandler extends ConstraintSecurityHandler {

    public BasicAuthenticationHandler() {
        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"admin"});

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setPathSpec("/*");
        constraintMapping.setConstraint(constraint);

        setConstraintMappings(Collections.singletonList(constraintMapping));
        setAuthenticator(new BasicAuthenticator());
    }
}
