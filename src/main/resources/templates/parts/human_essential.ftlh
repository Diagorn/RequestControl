<#macro human>
    <div class="form-group">
        <label for="lastName-input">Фамилия</label>
        <input name="lastName" type="text" class="form-control ${(lastNameError??)?string('is-invalid', '')}"
               id="lastName-input" aria-describedby="emailHelp" placeholder="Введите вашу фамилию">
        <#if lastNameError??>
            <div class="invalid-feedback">
                ${lastNameError}
            </div>
        </#if>
    </div>
    <div class="form-group">
        <label for="firstName-input">Имя</label>
        <input name="firstName" type="text" class="form-control  ${(firstNameError??)?string('is-invalid', '')}"
               id="firstName-input" aria-describedby="emailHelp" placeholder="Введите ваше имя">
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
               placeholder="Введите ваше отчество (необязательно)">
    </div>
    <div class="form-group">
        <label for="telephone-input">Номер телефона</label>
        <input type="tel" id="telephone-input" class="form-control" name="phone">
    </div>
</#macro>