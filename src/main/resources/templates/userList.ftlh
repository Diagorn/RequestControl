<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <div class="form-row">
        <div class="col-md-6">
            <h4>Список пользователей</h4>
            <form method="get" action="/user" class="form-inline mb-2">
                <div class="col-auto">
                    <input type="text" class="form-control" id="search" name="search" placeholder="Найти...">
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-info">Найти</button>
                </div>
            </form>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Электронная почта</th>
                    <th>Роли</th>
                </tr>
                </thead>
                <tbody>
                <#list users as user>
                    <tr>
                        <td>${user.username}</td>
                        <td><a href="/user/${user.id}">Изменить...</a></td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
        <div class="col-md-6">
            <h4>Добавление нового пользователя</h4>
            <form method="post" action="/user/new">
                <input type="text" class="form-control mb-2" name="email" placeholder="E-mail" required>
                <input type="text" class="form-control mb-2" name="firstName" placeholder="Имя" required>
                <input type="text" class="form-control mb-2" name="lastName" placeholder="Фамилия" required>
                <input type="text" class="form-control mb-2" name="secondName" placeholder="Отчество">
                <#list roles as role>
                    <label class="form-check-label ml-4"><input
                                type="checkbox"
                                name="${role}"
                                class="form-check-input">
                        ${userService.getStringRoleName(role)}</label><br>
                </#list>
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <button type="submit" class="btn btn-primary mt-2">Добавить</button>
            </form>
        </div>
        <!-- <div class="col-md-4">
            <h4>Удаление пользователя</h4>
            <form action="/user/search" method="get">
                <input type="text" class="form-control" name="search" placeholder="Поиск...">
                <button class="btn btn-secondary mt-2">Найти</button>
            </form>
            <form method="post" action="/user/delete">
                <#list deleteUsers as us>
                    <div class="mt-2 ml-4">
                        <input type="checkbox" class="form-check-input admin-search-checkbox" id="${us.username}">
                        <input type="hidden" name="${us.username}" value="${us.username}">
                        <label for="${us.username}" title="${us.username}">${us.getUsernameWithInitials()}</label>
                        <img src="../img/${us.getFileName()}" alt="" class="navbar-custom-avatar"><br>
                    </div>
                </#list>
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <button type="submit" class="btn btn-danger mt-2">Удалить</button>
            </form>
        </div> -->
    </div>
</@c.page>