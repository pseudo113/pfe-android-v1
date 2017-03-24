package com.labrosse.suivicommercial.service.resquest;

/**
 * Created by ahmedhammami on 01/01/2017.
 */

public class LoginRequest {

    private String login;

    private String password;

    public String getLogin ()
    {
        return login;
    }

    public void setLogin (String login)
    {
        this.login = login;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [login = "+login+", password = "+password+"]";
    }
}
