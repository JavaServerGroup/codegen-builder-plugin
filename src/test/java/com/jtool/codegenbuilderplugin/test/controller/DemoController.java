package com.jtool.codegenbuilderplugin.test.controller;

import com.jtool.codegenannotation.CodeGenApi;
import com.jtool.codegenannotation.CodeGenException;
import com.jtool.codegenannotation.CodeGenRequest;
import com.jtool.codegenannotation.CodeGenResponse;
import com.jtool.codegenbuilderplugin.test.api.request.SearchUserApiRequest;
import com.jtool.codegenbuilderplugin.test.api.response.SearchUserApiResponse;
import com.jtool.codegenbuilderplugin.test.exception.BackEndException;
import com.jtool.codegenbuilderplugin.test.exception.ParamException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/*
<logicInfo>系统为不同的屏幕尺寸提供共4种可选的分辨率<br/>
    正方形的缩略图有64*64，120*120，180*180，270*270；<br/>
    比如你获取到的缩略图地址是：<br/>
    "img0:/images/2015/11/5/64_64/f8cd21233d69d7c3ba5e74dda7ff3475.jpg"<br/>
    那么在大屏手机里，你可以替换为<br/>
    /images/2015/11/5/270_270/f8cd21233d69d7c3ba5e74dda7ff3475.jpg的地址来获取大屏的缩略图</logicInfo>
 */
@Controller
public class DemoController {

	/*
	<logicInfo>只有没有被丢黑名单的用户才会出现在搜索结果，每十分钟更新缓存一次。</logicInfo>
	 */
	@CodeGenApi(name = "查找用户", docSeq = 1, description = "根据用户国家，年纪，身高，是否结婚等条件过滤查找用户", remark = "这是一个备注信息")
	@CodeGenRequest(SearchUserApiRequest.class)
	@CodeGenResponse(SearchUserApiResponse.class)
	@CodeGenException(BackEndException.class)
	@RequestMapping(value = "/searchUser", method = RequestMethod.POST)
	public @ResponseBody String searchUser() throws ParamException {
		return null;
	}

	@CodeGenApi(name = "查找用户2", docSeq = 2, description = "根据用户国家，年纪，身高，是否结婚等条件过滤查找用户", remark = "这是一个备注信息")
	@CodeGenRequest(SearchUserApiRequest.class)
	@CodeGenResponse(SearchUserApiResponse.class)
	@CodeGenException(BackEndException.class)
	@RequestMapping(value = "/searchUser2", method = RequestMethod.POST)
	public @ResponseBody String searchUser2() throws ParamException {
		return null;
	}

}
