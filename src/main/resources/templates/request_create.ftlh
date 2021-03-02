<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <div class="row">
        <div class="col-md-3"></div>
        <div class="col-md-6"><#if !isClientRequest>
                <form method="post" action="/request-create">
                    <label for="request-client">Клиент</label>
                    <input id="request-client" type="text" class="form-control" name="name" placeholder="От..." value="<#if query??>${query}</#if>">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <button class="btn btn-primary mt-3" type="submit">Найти</button>
                    <#if message??>${message}</#if>
                </form>
            </#if>

            <#if client??>
                ${client.getUsernameWithInitials()}
                <img src="img/${client.fileName}" alt="" class="navbar-custom-avatar"><br>
            </#if>
            <form action="/request" method="post">
                <div class="form-group">
                    <label for="request-theme">Предмет/программа:</label>
                    <div id="theme-wrapper">
                        <input type="text" name="theme" class="form-control" id="request-theme" placeholder="Тема">
                    </div>
                    <br>
                    <#if client??>
                        <input type="hidden" name="username" value="${client.getUsername()}">
                    </#if>
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <button class="btn btn-success mt-3" type="submit">Создать</button>
                </div>
            </form>
        </div>
    </div>
    <script type="text/javascript">
        document.onload(function () {
            var arr = [];
            <#list programs as program>
                arr.push('${program.title}');
            </#list>
        })

        var input = document.getElementById("request-theme");
        var div = document.getElementById("theme-wrapper");
        input.oninput = function() {
            let re=new RegExp(this.value), text=""
            for(let i=0; i<arr.length; i++){if(re.test(arr[i])) text+=arr[i]+"<br>"}
            div.innerHTML=text
        }
    </script>
</@c.page>