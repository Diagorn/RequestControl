<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <form action="/registration/organisation/director" method="post">
                <#include "parts/human_essential.ftlh">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
            </form>
        </div>
    </div>
</@c.page>