<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <div class="row">
        <div class="col-md-3">
            <a href="/request-create" class="btn btn-primary mb-3">Создать новую заявку</a>
            <div class="list-group">
                <a href="/request/" class="list-group-item list-group-item-action">Все</a>
                <#if isAdmin || isModer><a href="/request?status=no_executer&sort=${sort}" class="list-group-item list-group-item-action">Без исполнителя</a></#if>
                <a href="/request?status=in_process&sort=${sort}" class="list-group-item list-group-item-action">В процессе</a>
                <a href="/request?status=frozen&sort=${sort}" class="list-group-item list-group-item-action">Замороженные</a>
                <a href="/request?status=complete&sort=${sort}" class="list-group-item list-group-item-action">Закрытые</a>
                <a href="/request?status=deleted&sort=${sort}" class="list-group-item list-group-item-action">Удалённые</a>
                <#if !isPhysical || isAdmin || isModer><a href="/request?status=prototype&sort=${sort}" class="list-group-item list-group-item-action">Прототипы</a></#if>
            </div>
        </div>
        <div class="col-md-9 requests">
            <h5>Сортировка</h5>
            <div class="d-inline">
                <button class="btn btn-link"><a href="/request?status=${status}&sort=last_message">По последнему сообщению</a></button>
                <button class="btn btn-link"><a href="/request?status=${status}&sort=id">По порядку</a></button>
                <button class="btn btn-link"><a href="/request?status=${status}&sort=executer">По исполнителю</a></button>
                <button class="btn btn-link"><a href="/request?status=${status}&sort=theme">По предмету</a></button>
                <button class="btn btn-link"><a href="/request?status=${status}&sort=client">По клиенту</a></button>
            </div>
            <ul class="list-group">
                <#list requests as request>
                    <#if request.client.equals(user)>
                        <li class="list-group-item">
                            <a href="/request/physical/${request.id}">
                                ${request.theme}
                            </a><br>
                            <p>Статус: ${utils.getRequestStateAsString(request.status)}</p>
                            <p>Исполнитель: <#if request.executer??>${request.executer.getUsernameWithInitials()}</#if></p>
                            <#if utils.getLastMessage(request)??>
                                <p>Время последнего ответа: ${utils.getCalendarForMessage(utils.getLastMessage(request).timeOfSending)}</p>
                                <p>Последний ответ от: ${utils.getLastMessage(request).author.getUsernameWithInitials()}</p>
                            </#if>
                        </li>
                    <#elseif user.isExecuter()>
                        <li class="list-group-item">
                            <p><#if request.isPhysical()>
                                    <a href="/request/physical/${request.id}">
                                        ${request.theme}
                                    </a>
                                <#else>
                                    <a href="/request/organisation/${request.id}">
                                        ${request.theme}
                                    </a>
                                </#if></p>
                            <p>№ заявки: ${request.getId()}</p>
                            <p>Заявка от: ${request.getClient().getUsernameWithInitials()}</p>
                            <#if utils.getLastMessage(request)??>
                                <p>Время последнего ответа: ${utils.getCalendarForMessage(utils.getLastMessage(request).timeOfSending)}</p>
                                <p>Последний ответ от: ${utils.getLastMessage(request).author.getUsernameWithInitials()}</p>
                            </#if>
                        </li>
                    <#elseif user.isAdmin() || user.isModer()>
                        <li class="list-group-item">
                            <p><#if request.isPhysical()>
                                    <a href="/request/physical/${request.id}">
                                        ${request.theme}
                                    </a>
                                <#else>
                                    <a href="/request/organisation/${request.id}">
                                        ${request.theme}
                                    </a>
                                </#if></p>
                            <p>№ заявки: ${request.getId()}</p>
                            <p>Исполнитель: <#if request.executer??>${request.executer.getUsernameWithInitials()}</#if></p>
                            <#if utils.getLastMessage(request)??>
                                <p>Время последнего ответа: ${utils.getCalendarForMessage(utils.getLastMessage(request).timeOfSending)}</p>
                                <p>Последний ответ от: ${utils.getLastMessage(request).author.getUsernameWithInitials()}</p>
                            </#if>
                            <#if request.status != "PROTOTYPE"><a href="/<#if request.isPhysical()>physical/<#else>organisation/</#if>request/${request.id}/set-executer" class="btn btn-light float-right">Назначить исполнителя</a></#if>
                        </li>
                    </#if>
                <#else>Заявок нет
                </#list>
            </ul>
        </div>
    </div>
</@c.page>