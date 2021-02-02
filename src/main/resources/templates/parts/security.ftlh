<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = user.getUsername()
    isAdmin = user.isAdmin()
    isModer = user.isModer()
    shortName = user.getUsernameWithInitials()
    avatar = user.getFileName()
        isPhysical = user.isPhysical()
    >
<#else>
    <#assign
    name = ""
    shortName = ""
    avatar="defaultavatar.png"
    isAdmin = false
    >
</#if>