package com.jtool.codegenbuilderplugin.test.controller;

import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenApi;
import com.jtool.codegenannotation.CodeGenException;
import com.jtool.codegenannotation.CodeGenRequest;
import com.jtool.codegenannotation.CodeGenResponse;
import com.jtool.codegenbuilderplugin.test.api.request.SearchUserApiRequest;
import com.jtool.codegenbuilderplugin.test.api.request.UploadAvatarApiRequest;
import com.jtool.codegenbuilderplugin.test.api.response.Pages;
import com.jtool.codegenbuilderplugin.test.api.response.SearchUserApiResponse;
import com.jtool.codegenbuilderplugin.test.api.response.UploadAvatarApiResponse;
import com.jtool.codegenbuilderplugin.test.api.response.User;
import com.jtool.codegenbuilderplugin.test.exception.BackEndException;
import com.jtool.codegenbuilderplugin.test.exception.ParamException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
	@CodeGenApi(name = "查找用户", description = "根据用户国家，年纪，身高，是否结婚等条件过滤查找用户")
	@CodeGenRequest(SearchUserApiRequest.class)
	@CodeGenResponse(SearchUserApiResponse.class)
	@CodeGenException(BackEndException.class)
	@ResponseBody
	@RequestMapping(value = "/searchUser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String searchUser(SearchUserApiRequest searchUserApiRequest) throws ParamException {

		Pages pages = new Pages();
		pages.setTotalPage(100);
		pages.setVersion("abcdefghijklmn");

		List<String> urls = new ArrayList<>();
		urls.add("http://www.google.com");
		urls.add("http://www.facebook.com");

		List<User> userList = new ArrayList<>();

		User user = new User();
		user.setHeight(1.56);
		user.setName("用户1");
		user.setAge(30);
		user.setCountry("China");
		user.setIsMarried(true);

		userList.add(user);

		User user1 = new User();
		user1.setHeight(1.70);
		user1.setName("用户2");
		user1.setAge(36);
		user1.setCountry("China");
		user1.setIsMarried(false);

		userList.add(user1);

		SearchUserApiResponse searchUserApiResponse = new SearchUserApiResponse();
		searchUserApiResponse.setCode("0");
		searchUserApiResponse.setPages(pages);
		searchUserApiResponse.setUrls(urls);
		searchUserApiResponse.setUsers(userList);

		return JSON.toJSONString(searchUserApiResponse);
	}

	@CodeGenApi(name = "上传用户头像", description = "上传用户头像")
	@CodeGenRequest(UploadAvatarApiRequest.class)
	@CodeGenResponse(UploadAvatarApiResponse.class)
	@ResponseBody
	@RequestMapping(value = "/uploadAvatar", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
	public String uploadAvatar(UploadAvatarApiRequest uploadAvatarRequest) throws ParamException, BackEndException, IOException {

		UploadAvatarApiResponse uploadAvatarResponse = new UploadAvatarApiResponse();
		uploadAvatarResponse.setCode("0");
		uploadAvatarResponse.setFileContent(Base64.getEncoder().encodeToString(uploadAvatarRequest.getFile().getBytes()));

		return JSON.toJSONString(uploadAvatarResponse);
	}

}
