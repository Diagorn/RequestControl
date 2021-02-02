<#import "parts/common.ftlh" as c>

<@c.page>
    <h3>Список исполнителей</h3>
    <div class="form-row">
        <form method="get" class="form-inline mb-3" action="/<#if request.isPhysical()>physical/<#else>organisation/</#if>request/#{request.id}/set-executer">
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
                </tr>
                </thead>
                <tbody>
                <#list users as us>
                    <tr>
                        <th scope="row">${us.getPerson().lastName}</th>
                        <td>${us.getPerson().firstName}</td>
                        <td>${us.getPerson().secondName}</td>
                        <td>${us.username}</td>
                        <td>
                            <#list us.roles as role>${userService.getStringRoleName(role)}<#sep>, </#list>
                        </td>
                            <td>
                                <#if request??>
                                <form action="/<#if request.isPhysical()>physical/<#else>organisation/</#if>request/${request.id}/set-executer" method="post">
                                    <input type="hidden" name="userId" value="${us.id}">
                                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                                    <button type="submit" class="btn btn-light">Назначить</button>
                                    </form><#else> Произошла ошибка
                                </#if>
                            </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
    </div>
</@c.page>