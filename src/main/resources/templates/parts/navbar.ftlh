<#include "security.ftlh">
<#import "login.ftlh" as l>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <a class="navbar-brand" href="/">Управление заявками</a>
    <button
            class="navbar-toggler"
            type="button"
            data-toggle="collapse"
            data-target="#navbarNav"
            aria-controls="navbarNav"
            aria-expanded="false"
            aria-label="Toggle navigation"
    >
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="/request"
                >Заявки <span class="sr-only">(current)</span></a
                >
            </li>
            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/user">Управление</a>
                </li>
            </#if>
            <li class="nav-item">
                <a class="nav-link" href="/user/contacts">Контакты</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/payment">Оплата</a>
            </li>
            <#if known>
            <#if isAdmin || isModer>
                <li class="nav-item">
                    <a class="nav-link" href="/chat">Чат</a>
                </li>
            </#if>
            </#if>
        </ul>
        <div class="float-right">
            <a href="/user/profile">
                <span class="name ml-auto mr-2">${shortName}</span
                ><#if known><img
                src="img/${avatar}"
                alt=""
                class="img-responsive navbar-custom-avatar"
                /></#if></a>
        </div>
        <#if name!=""><@l.logout/></#if>
    </div>
</nav>