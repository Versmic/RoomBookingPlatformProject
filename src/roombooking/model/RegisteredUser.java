package roombooking.model;

import java.util.ArrayList;
import java.util.List;

// superclass for authenticated users in the system (Student, Faculty, Partner, Staff, Administrator)
public abstract class RegisteredUser{
    
    public abstract double getHRate();
    public abstract String getIDNumber();
    
}