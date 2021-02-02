<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <div class="row">
        <div class="col-md-3">
            <a href="/request-create" class="btn btn-primary mb-3">Создать новую заявку</a>
            <div class="list-group">
                <a href="/request/" class="list-group-item list-group-item-action">Все</a>
                <#if isAdmin || isModer><a href="/request?status=no_executer" class="list-group-item list-group-item-action">Без исполнителя</a></#if>
                <a href="/request?status=in_process" class="list-group-item list-group-item-action">В процессе</a>
                <a href="/request?status=frozen" class="list-group-item list-group-item-action">Замороженные</a>
                <a href="/request?status=complete" class="list-group-item list-group-item-action">Закрытые</a>
                <a href="/request?status=deleted" class="list-group-item list-group-item-action">Удалённые</a>
                <#if !isPhysical || isAdmin || isModer><a href="/request?status=prototype" class="list-group-item list-group-item-action">Прототипы</a></#if>
            </div>
        </div>
        <div class="col-md-9">
            <ul class="list-group">
                <#list requests as request>
                    <li class="list-group-item">
                        <#if request.isPhysical()>
                            <a href="/request/physical/${request.id}">
                                ${request.theme}
                            </a>
                        <#else>
                            <a href="/request/organisation/${request.id}">
                                ${request.theme}
                            </a>
                        </#if>
                        <#if request.executer??><p>${request.executer.getUsernameWithInitials()}</p>
                            <#else><#if (isAdmin || isModer) && (request.status != "PROTOTYPE")><a href="/<#if request.isPhysical()>physical/<#else>organisation/</#if>request/${request.id}/set-executer" class="btn btn-light float-right">Назначить исполнителя</a></#if>
                        </#if>
                    </li>
                <#else>Заявок нет
                </#list>
            </ul>
        </div>
    </div>
</@c.page>