{
    "id": "${id}",
    "params": {
        "data": {
            <#list parametersList as param>
                "${param.name}": "${param.value}"<#if param_has_next>,</#if>
            </#list>
        }
    }
}