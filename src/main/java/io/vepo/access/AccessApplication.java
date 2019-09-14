package io.vepo.access;

import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;

/**
 * This class is just to Allow Microprofile.io to understand this is an
 * application! Keep it!
 * 
 * @author Victor Osório
 *
 */
@ApplicationPath("/")
public class AccessApplication extends Application {

}