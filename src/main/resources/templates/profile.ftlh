<#include "parts/security.ftlh">
<#import "parts/common.ftlh" as c>
<@c.page>
<h5>Редактирование профиля</h5>
<div class="row">
    <div class="col-md-4"></div>
    <div class="col-md-4">
        <form class="mt-5" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="exampleInputPassword1">Пароль</label>
                <input name="password" type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
            </div>
            <div class="form-group">
                <label for="lastName-input">Фамилия</label>
                <input name="lastName" type="text" class="form-control" id="lastName-input" aria-describedby="emailHelp" placeholder="Enter email" value="${lastName!''}">
            </div>
            <div class="form-group">
                <label for="firstName-input">Имя</label>
                <input name="firstName" type="text" class="form-control" id="firstName-input" aria-describedby="emailHelp" placeholder="Enter email" value="${firstName!''}">
            </div>
            <div class="form-group">
                <label for="secondName-input">Отчество</label>
                <input name="secondName" type="text" class="form-control" id="secondName-input" aria-describedby="emailHelp" placeholder="Enter email" value="${secondName!''}">
            </div>
            <div class="form-group">
                <label for="avatar-input">Картинка профиля</label>
                <input type="file" name="avatar">
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit" class="btn btn-primary">Сохранить</button>
        </form>
    </div>
</div>
</@c.page>