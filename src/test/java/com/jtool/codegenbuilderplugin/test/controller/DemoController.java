package com.jtool.codegenbuilderplugin.test.controller;

import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenApi;
import com.jtool.codegenannotation.CodeGenRequest;
import com.jtool.codegenannotation.CodeGenResponse;
import com.jtool.codegenbuilderplugin.test.api.request.*;
import com.jtool.codegenbuilderplugin.test.api.response.*;
import com.jtool.codegenbuilderplugin.test.exception.BackEndException;
import com.jtool.codegenbuilderplugin.test.exception.ParamException;
import com.jtool.validator.ParamBeanValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@CodeGenApi(name = "查找用户", description = "根据用户国家，年纪，身高，是否结婚等条件过滤查找用户", genSDK = true)
	@CodeGenRequest(SearchUserApiRequest.class)
	@CodeGenResponse(SearchUserApiResponse.class)
	@ResponseBody
	@RequestMapping(value = "/searchUser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String searchUser(SearchUserApiRequest searchUserApiRequest) throws ParamException, BackEndException {


		log.debug("请求获得参数：" + searchUserApiRequest);

		log.debug("请求参数是否合法：" + !ParamBeanValidator.isNotValid(searchUserApiRequest));

		if(ParamBeanValidator.isNotValid(searchUserApiRequest)) {
			return "{\"code\":0}";
		}

		Pages pages = new Pages();
		pages.setTotalPage(100);
		pages.setVersion("abcdefghijklmn");

		List<String> urls = new ArrayList<>();
		urls.add("http://www.google.com");
		urls.add("http://www.facebook.com");

		List<User> userList = new ArrayList<>();

		User user = new User();
		user.setHeight(searchUserApiRequest.getHeight());
		user.setName("用户1");
		user.setAge(searchUserApiRequest.getAge());
		user.setCountry(searchUserApiRequest.getCountry());
		user.setIsMarried(!searchUserApiRequest.getIsMarried().equals("0"));

		userList.add(user);

		User user1 = new User();
		user1.setHeight(searchUserApiRequest.getHeight());
		user1.setName("用户2");
		user1.setAge(searchUserApiRequest.getAge());
		user1.setCountry(searchUserApiRequest.getCountry());
		user1.setIsMarried(searchUserApiRequest.getIsMarried().equals("0"));

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

		log.debug("请求获得参数：" + uploadAvatarRequest);

		log.debug("请求参数是否合法：" + !ParamBeanValidator.isNotValid(uploadAvatarRequest));

		if(ParamBeanValidator.isNotValid(uploadAvatarRequest)) {
			return "{\"code\":0}";
		}

		UploadAvatarApiResponse uploadAvatarResponse = new UploadAvatarApiResponse();
		uploadAvatarResponse.setCode("0");
		uploadAvatarResponse.setFileContent(Base64.getEncoder().encodeToString(uploadAvatarRequest.getFile().getBytes()));

		return JSON.toJSONString(uploadAvatarResponse);
	}

	@CodeGenApi(name = "获取信息", description = "无参数请求获取信息", forWho = "client")
	@CodeGenResponse(SearchUserApiResponse.class)
	@ResponseBody
	@RequestMapping(value = "/getUser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String getUser() throws ParamException, BackEndException {

		SearchUserApiResponse searchUserApiResponse = new SearchUserApiResponse();
		searchUserApiResponse.setCode("0");

		return JSON.toJSONString(searchUserApiResponse);
	}

	@CodeGenApi(name = "response是DIY", description = "测试response是DIY", genSDK = false)
	@ResponseBody
	@RequestMapping(value = "/getDIY", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String getDIY() throws ParamException, BackEndException {

		SearchUserApiResponse searchUserApiResponse = new SearchUserApiResponse();
		searchUserApiResponse.setCode("0");

		return JSON.toJSONString(searchUserApiResponse);
	}

	@CodeGenApi(name = "/order/querySingle/", description = "创建订单")
	@CodeGenRequest(QuerySingleRequest.class)
	@CodeGenResponse(QuerySingleResponse.class)
	@RequestMapping(value = "/{order}/querySingle/{myId}", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public @ResponseBody QuerySingleResponse querySingle(QuerySingleRequest querySingleRequest){
		return null;
	}

	@RequestMapping(value = "/{currency}/pay/pre", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=utf-8")
	@CodeGenApi(name = "预支付", description = "提交支付信息,生成定单,并显示确认页面")
	@CodeGenRequest(PrePaymentRequest.class)
	public String paymentPre(HttpServletRequest request, PrePaymentRequest preRequest) throws Exception {
		return null;
	}


	@CodeGenApi(name = "添加广告", description = "添加不同的广告")
	@CodeGenRequest(value = AppFullAd.class, isRest = true)
	@CodeGenResponse(CommonResponse.class)
	@ResponseBody
	@RequestMapping(value = "/subClass", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
	public String subClass(@RequestBody String requestBodyStr) throws ParamException, BackEndException, IOException {
		return null;
	}

	@CodeGenApi(name="检查插件", description = "检查插件是否需要更新")
	@CodeGenRequest(value = CheckSdkPluginsRequest.class, isRest = true)
	@CodeGenResponse(CheckSdkPluginsResponse.class)
	@RequestMapping(value = "/checkSdkPlugins", method = RequestMethod.POST)
	public String checkSdkPlugins() {

		return null;
	}

}
