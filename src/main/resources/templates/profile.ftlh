<#include "parts/security.ftlh">
<#import "parts/common.ftlh" as c>
    <#import "parts/human_essential.ftlh" as h>
<@c.page>
    <#if user.isPhysical()>
<h5>Редактирование профиля</h5>
<div class="row">
    <div class="col-md-4"></div>
    <div class="col-md-4">
        <@h.human/>
        <div class="form-group">
            <label for="dob-input">Дата рождения</label>
            <input name="dob" type="date" class="form-control ${(lastNameError??)?string('is-invalid', '')}"
                   id="date-input" aria-describedby="emailHelp" placeholder="Иванов">
        </div>
        <div class="form-group">
            <label for="passport-input">Паспорт</label>
            <input name="passport" type="number" class="form-control ${(lastNameError??)?string('is-invalid', '')}"
                   id="passport-input" aria-describedby="emailHelp" placeholder="Иванов">
        </div>
        <div class="form-group">
            <label for="adress-input">Адрес регистрации</label>
            <input name="adress" type="text" class="form-control ${(lastNameError??)?string('is-invalid', '')}"
                   id="adress-input" aria-describedby="emailHelp" placeholder="Иванов">
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
            <label for="avatar-input">Картинка профиля</label>
            <input name="avatar" type="file" class="form-control ${(lastNameError??)?string('is-invalid', '')}"
                   id="adress-input" aria-describedby="emailHelp" placeholder="Иванов">
        </div>
    </div>
</div>
</#if>
</@c.page>