<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <form action="/registration/organisation" method="post" class="mt-5">
                <div class="form-group">
                    <label for="name-input">Наименование организации</label>
                    <input name="name" type="text" class="form-control"
                           id="name-input" aria-describedby="emailHelp" placeholder="Введите название организации">
                </div>
                <div class="form-group">
                    <label for="position-input">Должность</label>
                    <input name="position" type="text" class="form-control"
                           id="position-input" aria-describedby="emailHelp" placeholder="Должность">
                </div>
                <div class="form-group">
                    <label for="physical-adress-input">Физический адрес</label>
                    <input type="text" id="physical-adress-input" class="form-control" name="physicalAdress"
                           placeholder="Физический адрес">
                </div>
                <div class="form-group">
                    <label for="legal-adress-input">Адрес регистрации</label>
                    <input type="text" id="legal-adress-input" class="form-control" name="legalAdress"
                           placeholder="Юридический адрес">
                </div>
                <div class="form-group">
                    <label for="telephone-input">Номер телефона организации</label>
                    <input type="tel" id="telephone-input" class="form-control" name="telephone">
                </div>
                <input type="hidden" name="user" value="${id}">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <button type="submit" class="btn btn-primary">Отправить</button>
            </form>
        </div>
    </div>
</@c.page>