{
    "id": "${id}",
    "params": {
        "data": {
            <#list parametersList as param>
                <#if param.value?starts_with("{") && param.value?ends_with("}")>
                "${param.name}": "${(param.value?js_string)!}"<#if param_has_next>,</#if>
                <#else>
                "${param.name}": "${param.value!}"<#if param_has_next>,</#if>
                </#if>
            </#list>
        }
    }
}
