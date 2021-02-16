<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <div class="chat">
    <#list messages as message>
        <div class="chat-message mb-2 <#if message.authorName == name>right<#else>left</#if>"
             xmlns="http://www.w3.org/1999/html">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">
                        ${message.getAuthor().getUsernameWithInitials()}
                        <img src="/img/${message.author.getFileName()}" class="navbar-custom-avatar right" alt="">
                    </h5>
                    <p class="card-text">
                        ${message.text}<br>
                        <span>${utils.getCalendarForMessage(message.timeOfSending)}</span><br>
                        <#list message.getMessageFiles() as file>
                            <a href="/files/${file.newFileName}">${file.originalName}</a><br>
                        </#list>
                    </p>
                </div>
            </div>
        </div>
        </div>
    <#else>Здесь пока нет сообщений
    </#list>
    <div class="chat-footer">
        <form method="post" class="mb-n3" enctype="multipart/form-data">
            <input type="text" <#if status??><#if status=="FROZEN" || status=="COMPLETE" || status=="DELETED">disabled</#if></#if> name="text" class="form-control chat-input ${(textError??)?string('is-invalid', '')} <#if status??><#if !((status == "IN_PROCESS") || (status == "NO_EXECUTER" && user == request.getClient()))>mb-5</#if></#if>"
                   value="<#if message??>${message.text}</#if>">
            <div class="form-group mt-1">
                <label for="files">Прикрепить файлы</label>
                <input id="files" type="file" name="files" multiple>
                <#if status??><#if ((status == "IN_PROCESS") || (status == "NO_EXECUTER" && user == request.getClient()))></#if>
                    <button type="submit" class="btn btn-primary mt-2">Отправить</button>
                <#else><button type="submit" class="btn btn-primary mt-2">Отправить</button>
                </#if>
            </div>
            <#if textError??>
                <div class="invalid-feedback">
                    ${textError}
                </div>
            </#if>

            <input type="hidden" name="_csrf" value="${_csrf.token}">
        </form>
        <#if (!isAdminChat)>
            <#if user.equals(request.getExecuter())>
                <div class="float-right mt-n4">
                    <#if request.isPhysical()>
                <#else><form action="/organisation/request/${request.getId()}/delete" method="post" class="d-inline">
                        </#if>
                        <#if status == "IN_PROCESS" || status == "FROZEN">
                            <button type="submit" class="btn btn-success d-inline-block">Закрыть</button>
                        </#if>
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                    </form>
                    <#if request.isPhysical()>
                    <form action="/physical/request/${request.getId()}/<#if !(status == "FROZEN")>freeze<#else>unfreeze</#if>" method="post" class="d-inline">
                        <#else><form action="/organisation/request/${request.getId()}/<#if !(status == "FROZEN")>freeze<#else>unfreeze</#if>" method="post" class="d-inline">
                            </#if>
                            <#if status == "IN_PROCESS">
                                <button type="submit" class="btn btn-info d-inline-block">Заморозить</button>
                            </#if>
                            <#if status == "FROZEN">
                                <button type="submit" class="btn btn-info d-inline-block">Разморозить</button>
                            </#if>
                            <input type="hidden" name="_csrf" value="${_csrf.token}">
                        </form>
                        <#if request.isPhysical()>
                        <form action="/physical/request/${request.getId()}/delete" method="post" class="d-inline">
                            <#else><form action="/organisation/request/${request.getId()}/delete" method="post" class="d-inline">
                                </#if>
                                <#if status != "DELETED">
                                    <button type="submit" class="btn btn-danger d-inline-block">Удалить</button>
                                </#if>
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                            </form>
                </div>
            </#if>
        </#if>
    </div>
</@c.page>