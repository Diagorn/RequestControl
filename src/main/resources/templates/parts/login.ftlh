<#include "security.ftlh">

<#macro user_inputs>
    <div class="form-group">
        <label for="exampleInputEmail1">Адрес электронной почты</label>
        <input name="username" type="email" class="form-control ${(usernameError??)?string('is-invalid', '')}"
               id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Введите электронную почту" value="<#if user??>${user.username}</#if>">
        <#if usernameError??>
            <div class="invalid-feedback">
                ${usernameError}
            </div>
        </#if>
    </div>

    <div class="form-group">
        <label for="exampleInputPassword1">Пароль</label>
        <input name="password" type="password" class="form-control ${(passwordError??)?string('is-invalid', '')}"
               id="exampleInputPassword1" placeholder="Введите пароль">
        <#if passwordError??>
            <div class="invalid-feedback">
                ${passwordError}
            </div>
        </#if>
    </div>
</#macro>

<#macro login path isRegisterForm>
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <#if isRegisterForm><form class="mt-5" id="register-form" action="${path}" method="post" <#if isRegisterForm>enctype="multipart/form-data" </#if>>
                <div class="form-group">
                    <label for="exampleInputPassword2">Подтверждение пароля</label>
                    <input name="password2" type="password" class="form-control ${(password2Error??)?string('is-invalid', '')}"
                           id="exampleInputPassword2" placeholder="Введите пароль">
                    <#if password2Error??>
                        <div class="invalid-feedback">
                            ${password2Error}
                        </div>
                    </#if>
                </div>
                <div id="registration-div">
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
                        <label for="telephone-input">Номер телефона</label>
                        <input type="tel" id="telephone-input" class="form-control" name="telephone">
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
                    <div class="col-md-6">
                        <div class="g-recaptcha" data-sitekey="6Leq46MZAAAAAI9KAaIsGa2k3aHo6YZkGtO2wdTQ"></div>
                        <#if captchaError??>
                            <div class="alert alert-danger" role="alert">
                                ${captchaError}
                            </div>
                        </#if>
                    </div>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                </form>
            <#else>
                <form action="/login" method="post">
                    <@user_inputs/>
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <button type="submit" class="btn btn-primary"><#if isRegisterForm>Зарегистрироваться<#else>Войти</#if></button>
                </form>
            </#if>
            <input type="hidden" name="_csrf" value="${_csrf.token}">

            <#if !isRegisterForm>
                <br>
                <a href="/registration/physical">Зарегистрироваться как физическое лицо...</a><br>
                <a href="/registration/organisation">Зарегистрироваться как юридическое лицо...</a>
            </#if>
        </div>
    </div>
    </div>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button class="btn btn-light ml-3" type="submit">Выйти</button>
    </form>
</#macro>