<#import "parts/common.ftlh" as c>
    <#import "parts/login.ftlh" as l>
    <#import "parts/human_essential.ftlh" as h>
<#include "parts/security.ftlh">

<@c.page>
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <form action="/registration/organisation" method="post" class="mt-5" enctype="multipart/form-data">
                <h4>Организация</h4>
                <@l.user_inputs/>
                <div class="form-group">
                    <label for="name-input">Наименование организации</label>
                    <input name="name" type="text" class="form-control"
                           id="name-input" aria-describedby="emailHelp" placeholder="Введите название организации">
                </div>
                <div class="form-group">
                    <label for="physical-adress-input">Физический адрес</label>
                    <input type="text" id="physical-adress-input" class="form-control" name="physicalAdress"
                           placeholder="Физический адрес">
                </div>
                <div class="form-group">
                    <label for="legal-adress-input">Юридический адрес</label>
                    <input type="text" id="legal-adress-input" class="form-control" name="legalAdress"
                           placeholder="Юридический адрес">
                </div>
                <div class="form-group">
                    <label for="company-telephone-input">Номер телефона организации</label>
                    <input type="tel" id="company-telephone-input" class="form-control" name="phone">
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <hr>
                <h4>Лицо компании</h4>
                <@h.human/>
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
                    <label for="position-input">Должность</label>
                    <input name="position" type="text" class="form-control"
                           id="position-input" aria-describedby="emailHelp"
                           placeholder="Введите вашу должность">
                </div>
                <div class="form-group">
                    <label for="avatar-input">Картинка профиля</label>
                    <input type="file" name="avatar">
                </div>
                <button type="submit" class="btn btn-primary">Отправить</button>
            </form>
        </div>
    </div>
</@c.page>