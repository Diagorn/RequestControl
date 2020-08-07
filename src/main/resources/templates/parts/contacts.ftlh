<#macro contact isRequestPage>
    <h3><#if isRequestPage>Список исполнителей<#else>Список администраторов и модераторов</#if></h3>
    <div class="form-row">
        <form method="get" class="form-inline mb-3" action="<#if isRequestPage>/request/#{request.id}/set-executer<#else>/user/contacts</#if>">
            <div class="col-auto">
                <input type="text" class="form-control" id="search" name="search" placeholder="Поиск...">
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-info">Найти</button>
            </div>
        </form>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table class="table">
                <thead class="thead-light">
                <tr>
                    <th scope="col">Фамилия</th>
                    <th scope="col">Имя</th>
                    <th scope="col">Отчество</th>
                    <th scope="col">E-mail</th>
                    <th scope="col">Роль</th>
                    <#if isRequestPage></#if>
                </tr>
                </thead>
                <tbody>
                <#list users as us>
                    <tr>
                        <th scope="row">${us.lastName}</th>
                        <td>${us.firstName}</td>
                        <td>${us.secondName}</td>
                        <td>${us.username}</td>
                        <td>
                            <#list us.roles as role>${userService.getStringRoleName(role)}<#sep>, </#list>
                        </td>
                        <#if isRequestPage>
                        <td>
                            <#if request??>
                            <form action="/request/${request.id}/set-executer" method="post">
                                <input type="hidden" name="userId" value="${us.id}">
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                                <button type="submit" class="btn btn-light">Назначить</button>
                            </form><#else> Произошла ошибка
                            </#if>
                        </td>
                        </#if>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
    </div>
</#macro>