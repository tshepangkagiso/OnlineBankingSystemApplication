package com.devteam.online_banking_system_backend.utility;

import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientLoginDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.OpenAccountDto;

public class util
{
    private util(){}

    public static ClientRegisterDto registerDto1()
    {
        return new ClientRegisterDto("Tom Holland", "TomHolland123", "tomholland@gmail.com");
    }

    public static ClientLoginDto loginDto1()
    {
        return new ClientLoginDto("TomHolland123", "tomholland@gmail.com");
    }
    public static OpenAccountDto openAccountDto1() { return new OpenAccountDto("tomholland@gmail.com"); }

    public static ClientRegisterDto registerDto2()
    {
        return new ClientRegisterDto("Florence Pugh", "FlorencePugh789_Security", "florence.pugh@example.com");
    }

    public static ClientLoginDto loginDto2() { return new ClientLoginDto("FlorencePugh789_Security", "florence.pugh@example.com"); }
    public static OpenAccountDto openAccountDto2() { return new OpenAccountDto("florence.pugh@example.com"); }


    public static ClientRegisterDto registerDto3()
    {
        return new ClientRegisterDto("Zendaya", "Zendaya123456", "zendaya@gmail.com");
    }

    public static ClientLoginDto loginDto3() {return new ClientLoginDto("Zendaya123456", "zendaya@gmail.com");}
    public static OpenAccountDto openAccountDto3() { return new OpenAccountDto("zendaya@gmail.com"); }
}
