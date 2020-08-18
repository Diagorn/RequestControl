<#import "parts/common.ftlh" as c>
    <#import "parts/human_essential.ftlh" as h>
    <#import "parts/login.ftlh" as l>
<#include "parts/security.ftlh">

<@c.page>
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <form action="/registration/physical" class="mt-5" method="post">
                <@l.user_inputs/>
                <@h.human/>
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
                <div class="form-group">
                    <label for="avatar-input">Картинка профиля</label>
                    <input type="file" name="avatar">
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <button type="submit" class="btn btn-primary">Отправить</button>
            </form>
        </div>
    </div>
</@c.page>