<#include "security.ftlh">
<#macro login path isRegisterForm>
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <form class="mt-5" action="${path}" method="post" <#if isRegisterForm>enctype="multipart/form-data" </#if>>
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
                <#if isRegisterForm>
                    <div class="form-group">
                        <label for="avatar-input">Картинка профиля</label>
                        <input type="file" name="avatar">
                    </div>
                    <div class="col-md-6">
                        <div class="g-recaptcha" data-sitekey="6Leq46MZAAAAAI9KAaIsGa2k3aHo6YZkGtO2wdTQ"></div>
                        <#if captchaError??>
                            <div class="alert alert-danger" role="alert">
                                ${captchaError}
                            </div>
                        </#if>
                    </div>
                    <label for="isOrganisationCheckbox">Я являюсь юридическим лицом</label>
                    <input type="checkbox" name="isOrganisation" id="isOrganisationCheckbox">
                </#if>

                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <button type="submit" class="btn btn-primary"><#if isRegisterForm>Зарегистрироваться<#else>Войти</#if></button>
            </form>
            <#if !isRegisterForm><a href="/registration">Зарегистрироваться...</a></#if>
        </div>
    </div>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button class="btn btn-light ml-3" type="submit">Выйти</button>
    </form>
</#macro>