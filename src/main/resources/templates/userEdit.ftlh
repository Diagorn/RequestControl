<#import "parts/common.ftlh" as c>

<@c.page>
    
    <h5>Изменение пользователя</h5>
    <form action="/user" method="post">
        <input type="text" name="username" value="${user.username}" class="form-control">
        <#list roles as role>
            <div>
                <label class="form-check-label"><input
                            type="checkbox"
                            name="${role}"
                            ${user.roles?seq_contains(role)?string("checked", "")}
                            class="form-check-input">
                    ${userService.getStringRoleName(role)}</label>
            </div>
        </#list>
        <input type="hidden" name="userId" value="${user.id}">
        <input type="hidden" value="${_csrf.token}" name="_csrf">
        <button type="submit" class="btn btn-success">Сохранить</button>
    </form>
</@c.page>