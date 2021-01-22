<#import "parts/common.ftlh" as c>
<#import "parts/human_essential.ftlh" as h>
<#include "parts/security.ftlh">
<@c.page>
    <div class="row">
        <div class="col-md-5">
            <a href="/request-create/organisation/${request.getId()}" class="btn btn-primary">Назад</a>
        </div>
        <div class="col-md-4">
            <form action="/request-create/organisation/${request.getId()}/save" method="post">
                <button class="btn btn-success">Сохранить заявку</button>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-md-5">
            <h2>Добавленные сотрудники</h2>
            <table class="table table-striped">
                <tr>
                    <th>Фамилия</th>
                    <th>Имя</th>
                    <th>Отчество</th>
                    <th></th>
                </tr>
                <#list employees as employee>
                    <tr>
                        <td>${employee.lastName}</td>
                        <td>${employee.firstName}</td>
                        <td>${employee.secondName}</td>
                        <td>
                            <form action="/request-create/organisation/${request.id}/delete" method="post">
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                                <input type="hidden" name="employeeID" value="${employee.id}">
                                <button type="submit" class="btn btn-danger">Удалить</button>
                            </form>
                        </td>
                    </tr>
                </#list>
            </table>
        </div>
        <div class="col-md-7">
            <form action="/request-create/organisation/${request.id}/add" method="post">
                <@h.human/>
                <div class="form-group">
                    <label for="email-input">Адрес электронной почты</label>
                    <input type="email" required id="email-input" class="form-control" name="email" placeholder="some@some.com">
                </div>
                <div class="form-group">
                    <label for="passport-input">Паспорт</label>
                    <input type="number" id="passport-input" name="passport"
                           class="form-control" placeholder="Серия и номер вашего паспорта">
                </div>
                <div class="form-group">
                    <label for="adress-input">Адрес регистрации</label>
                    <input type="text" id="adress-input" class="form-control" name="adress"
                           placeholder="Адрес регистрации, как в паспорте">
                </div>
                <div class="form-group">
                    <label for="education-input">Образование</label>
                    <select name="education" id="education-input" class="custom-select custom-select-lg">
                        <option value="school-not-finished">Неоконченное среднее</option>
                        <option value="school-finished">Среднее</option>
                        <option value="college-not-finished">Среднее профессиональное неоконченное</option>
                        <option value="college-finished">Среднее профессиональное оконченное</option>
                        <option value="university-not-finished">Высшее неоконченное</option>
                        <option value="university-bachelor">Высшее (бакалавр)</option>
                        <option value="university-master">Высшее (магистр)</option>
                        <option value="university-candidate">Высшее (кандидат наук)</option>
                        <option value="university-doctor">Высшее (доктор наук)</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="dob-input">Дата рождения</label>
                    <input type="date" id="dob-input" class="form-control" name="dob">
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <button type="submit" class="btn btn-success">Добавить</button>
            </form>
        </div>
    </div>
</@c.page>