<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <form action="/registration/physical" class="mt-5" method="post">
                <div class="form-group">
                    <label for="exampleInputPassword1">Пароль</label>
                    <input name="password2" type="password" class="form-control ${(password2Error??)?string('is-invalid', '')}"
                           id="exampleInputPassword2" placeholder="Подтверждение пароля">
                    <#if password2Error??><div  class="invalid-feedback">${password2Error}</div></#if>
                </div>
                <div class="form-group">
                    <label for="lastName-input">Фамилия</label>
                    <input name="lastName" type="text" class="form-control ${(lastNameError??)?string('is-invalid', '')}"
                           id="lastName-input" aria-describedby="emailHelp" placeholder="Введите вашу фамилию">
                    <#if lastNameError??>
                        <div class="invalid-feedback">
                            ${lastNameError}
                        </div>
                    </#if>
                </div>
                <div class="form-group">
                    <label for="firstName-input">Имя</label>
                    <input name="firstName" type="text" class="form-control  ${(firstNameError??)?string('is-invalid', '')}"
                           id="firstName-input" aria-describedby="emailHelp" placeholder="Введите ваше имя">
                    <#if firstNameError??>
                        <div class="invalid-feedback">
                            ${firstNameError}
                        </div>
                    </#if>
                </div>
                <div class="form-group">
                    <label for="secondName-input">Отчество</label>
                    <input name="secondName" type="text" class="form-control"
                           id="secondName-input" aria-describedby="emailHelp"
                           placeholder="Введите ваше отчество (необязательно)">
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
                    <label for="telephone-input">Номер телефона</label>
                    <input type="tel" id="telephone-input" class="form-control" name="telephone">
                </div>
                <div class="form-group">
                    <label for="dob-input">Дата рождения</label>
                    <input type="date" id="dob-input" class="form-control" name="dob">
                </div>
                <input type="hidden" name="_csrf" value="${csrf_token}">
                <button type="submit" class="btn btn-primary">Отправить</button>
            </form>
        </div>
    </div>
</@c.page>