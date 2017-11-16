# 目录
<b><a name="api列表" id="api列表" >API列表</a></b>

<#list apiModelList as apiModelItem>
* <a href="#${apiModelItem.apiName}">${apiModelItem.apiName}</a>
</#list>

<b><a href="#错误码定义" >错误码定义</a></b>


# 接口列表详情
<#list apiModelList as apiModelItem>
## <a name="${apiModelItem.apiName}" id="${apiModelItem.apiName}" >[ ${apiModelItem.apiName} ]</a>
### 接口说明
```txt
${apiModelItem.description}
```
### 定义
```shell
${apiModelItem.curlExample}
```

<#if (apiModelItem.requestParamModelList?size>0) >
### 请求参数说明
<table>
<tr>
    <th width="100px">参数名</th><th width="100px">必要参数</th><th width="200px">限制</th><th>说明</th>
</tr>
<#list apiModelItem.requestParamModelList as paramModelItem>
${returnParamDetail(paramModelItem)}
</#list>
</table>
</#if>

<#if (apiModelItem.responseParamModelList?size>0) >
### 正确返回
```json
${successReturnJson(apiModelItem.successReturnJsonStr)}
```
### 正确返回参数说明
<table>
<tr>
    <th width="100px">参数名</th><th width="100px">必要参数</th><th width="200px">限制</th><th>说明</th>
</tr>
<#list apiModelItem.responseParamModelList as paramModelItem>
${returnParamDetail(paramModelItem)}
</#list>
</table>
</#if>

<a href="#api列表" style="font-size:8px;">返回api列表</a>


</#list>



<#if (exceptionModels?size>0) >
## <a name="#错误码定义" id="错误码定义" >错误码定义</a>
错误码|说明|
:------:|------|
<#list exceptionModels as exceptionModel>
  ${exceptionModel.code}|${exceptionModel.desc?html}
</#list>
</#if>
