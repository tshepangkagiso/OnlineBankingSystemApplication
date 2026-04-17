package com.devteam.online_banking_system_backend.persistence.dtos.checkAccountDtos;


public class OverdraftToggleDto
{
    private Long checkAccountId;
    private Boolean toggle;

    public OverdraftToggleDto(){}
    public OverdraftToggleDto(Long checkAccountId, Boolean toggle)
    {
        this.setCheckAccountId(checkAccountId);
        this.toggle = toggle;
    }

    public Long getCheckAccountId(){return this.checkAccountId;}
    public void setCheckAccountId(Long checkAccountId){this.checkAccountId = checkAccountId;}

    public Boolean getToggle(){return  this.toggle;}
    public void setToggle(Boolean toggle){this.toggle = toggle;}
}
