<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <h2>Тема заявки</h2>
    <div class="row mb-2">
        <div class="col-md-4">
            <form action="/request-create/organisation/${request.getId()}" method="post">
                <input type="text" name="theme" value="${request.getTheme()}">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <button class="btn btn-primary">Сохранить</button>
            </form>
        </div>
        <div class="col-md-3">
            <a href="/request-create/organisation/${request.getId()}/add" class="btn btn-info">Управление сотрудниками</a>
        </div>
        <div class="col-md-3">
            <button class="btn btn-danger">Удалить заявку</button>
        </div>
        <div class="col-md-2">
            <button class="btn btn-success">Сохранить заявку</button>
        </div>
    </div>
    <div class="row">
    <#if request??>
        <div class="col-md-10">
            <table class="table table-striped">
                <tr>
                    <th>Фамилия</th>
                    <th>Имя</th>
                    <th>Отчество</th>
                    <th>Номер телефона</th>
                    <th>E-mail</th>
                    <th>Серия и номер паспорта</th>
                    <th>Адрес регистрации</th>
                    <th>Образование</th>
                    <th>Дата рождения</th>
                </tr>
            <#if employees??>
                <#list employees as employee>
                    <tr>
                        <td>${employee.lastName}</td>
                        <td>${employee.firstName}</td>
                        <td>${employee.secondName}</td>
                        <td>${employee.phoneNumber}</td>
                        <td>${employee.email}</td>
                        <td>${employee.passport}</td>
                        <td>${employee.registrationAdress}</td>
                        <td>${employee.education}</td>
                        <td>${utils.getCalendarAsString(employee.getDOB())}</td>
                    </tr>
                </#list>
            </#if>
            </table>
        </div>
        <div class="col-md-2"></div>

    </#if>
    </div>
</@c.page>