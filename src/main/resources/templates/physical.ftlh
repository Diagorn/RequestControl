<#import "parts/common.ftlh" as c>
<#import "parts/human_essential.ftlh" as h>
<#import "parts/login.ftlh" as l>
<#include "parts/security.ftlh">

<@c.page>
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <form action="/registration/physical" class="mt-5" method="post" enctype="multipart/form-data">
                <@l.user_inputs/>
                <div class="form-group">
                    <label for="lastName-input">Фамилия</label>
                    <input name="lastName" type="text" class="form-control ${(lastNameError??)?string('is-invalid', '')}"
                           id="lastName-input" aria-describedby="emailHelp" placeholder="Иванов" required>
                    <#if lastNameError??>
                        <div class="invalid-feedback">
                            ${lastNameError}
                        </div>
                    </#if>
                </div>
                <div class="form-group">
                    <label for="firstName-input">Имя</label>
                    <input name="firstName" type="text" class="form-control  ${(firstNameError??)?string('is-invalid', '')}"
                           id="firstName-input" aria-describedby="emailHelp" placeholder="Иван" required>
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
                            placeholder="Иванович">
                </div>
                <div class="form-group">
                    <label for="telephone-input">Номер телефона</label>
                    <input type="tel" id="telephone-input" required class="form-control" name="telephone" placeholder="+7-123-456-78-90">
                </div>
                <div class="form-group">
                    <label>Ваш пол</label>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="sex" id="male" value="male" checked>
                        <label class="form-check-label" for="male">
                            Мужской
                        </label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="sex" id="female" value="female">
                        <label class="form-check-label" for="female">
                            Женский
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <label for="passport-input">Серия и номер паспорта</label>
                    <input type="number" id="passport-input" name="passport"
                           class="form-control" placeholder="Серия и номер вашего паспорта" required>
                </div>
                <div class="form-group">
                    <label for="passport-date-input">Дата выдачи</label>
                    <input type="date" id="passport-date-input" class="form-control" name="passportDate" required>
                </div>
                <div class="form-group">
                    <label for="passport-giving-organ-input">Кем выдан</label>
                    <input type="text" id="passport-giving-organ-input" class="form-control" name="passportOrgan"
                           placeholder="МВД РФ" required>
                </div>
                <div class="form-group">
                    <label for="passport-citizenship">Гражданство</label>
                    <input type="text" id="passport-citizenship" class="form-control" name="citizenship"
                           placeholder="Российская федерация" required>
                </div>
                <div class="form-group">
                    <label for="adress-input">Адрес регистрации</label>
                    <input type="text" id="adress-input" class="form-control" name="adress"
                           placeholder="Адрес регистрации, как в паспорте" required>
                </div>
                <div class="form-group">
                    <label for="index-input">Индекс</label>
                    <input type="number" id="index-input" name="index"
                           class="form-control" placeholder="999999" required>
                </div>
                <div class="form-group">
                    <label for="education-input">Образование</label>
                    <select name="education" id="education-input" class="custom-select custom-select-lg" required>
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
                <hr>
                <h3>Если Вы являетесь студентом(если нет, везде ставьте прочерк):</h3>
                <div class="form-group">
                    <label for="speciality-input">Специальность</label>
                    <input type="text" id="speciality-input" class="form-control" name="speciality"
                           placeholder="Теплоэнергетика и теплотехника" required>
                </div>
                <div class="form-group">
                    <label for="group-input">Номер группы</label>
                    <input type="text" id="group-input" class="form-control" name="groupName"
                           placeholder="ЭР-11-20" required>
                </div>
                <div class="form-group">
                    <label for="dob-input">Дата рождения</label>
                    <input type="date" id="dob-input" class="form-control" name="dob" required>
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