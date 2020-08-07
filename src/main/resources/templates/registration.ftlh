<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<@c.page>
    <#if message?ifExists></#if>
    <@l.login "/registration" true>

    </@l.login>
</@c.page>