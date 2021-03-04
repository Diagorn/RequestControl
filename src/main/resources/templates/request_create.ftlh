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
            <form action="/request" method="post" id="form" onsubmit="return checkProgram()">
                <div class="form-group">
                    <p id="message" style="color: red"></p>
                    <label for="request-theme">Предмет/программа:</label>
                        <input type="text" name="choice" class="form-control mb-1" id="request-theme" placeholder="Поиск...">
                    <input type="text" name="theme" class="form-control" readonly id="theme" required>
                    <div id="theme-wrapper">
                        </div>
                    <br>
                    <#if client??>
                        <input type="hidden" name="username" value="${client.getUsername()}">
                    </#if>
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <button class="btn btn-success mt-3" type="submit" id="button">Создать</button>
                </div>
            </form>
        </div>
    </div>
    <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", ready);

        var arr = [];

        function ready() {
            <#list programs as program>
                arr.push('${program.title}');
            </#list>
        }

        var input = document.getElementById("request-theme");
        var div = document.getElementById("theme-wrapper");
        var form = document.getElementById("form");
        var button = document.getElementById("button");
        var themeInput = document.getElementById("theme");
        var message = document.getElementById("message");

        input.oninput = function() {
            let re=new RegExp(this.value), text=""
            for(let i=0; i<arr.length; i++){if(re.test(arr[i])) text+="<div id='" + i + "' onclick='programClick(" + i + ")'>" + arr[i] + "</div>"}
            div.innerHTML=text
        }

        input.onclick = function () {
            if (input.innerText === "") {
            let text = ""
            for(let i=0; i<arr.length; i++){text+="<div id='" + i + "' onclick='programClick(" + i + ")'>" + arr[i] + "</div>"}
            div.innerHTML=text
            } else {
                let re=new RegExp(this.value), text=""
                for(let i=0; i<arr.length; i++){if(re.test(arr[i])) text+="<div id='" + i + "' onclick='programClick(" + i + ")'>" + arr[i] + "</div>"}
                div.innerHTML=text
            }
        }

        function programClick(id) {
            let buffer = document.getElementById(id)
            input.setAttribute("value", "")
            themeInput.setAttribute("value", buffer.innerText)
            div.innerHTML = ""
        }

        function checkProgram() {
            if (themeInput.value === '') {
                message.innerText = "Выберите программу из списка"
                return false
            } else return  true;
        }
    </script>
</@c.page>