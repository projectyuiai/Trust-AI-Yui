package com.trustai.Trust_AI_Yui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class TrustAiYuiController {


	/**
	 * onClick Menu Button
	 * @return
	 */
	@RequestMapping(value = "/menu")
	public String menuForm() {
		return "menu";
	}
	
	/**
	 * onClick Menu Button
	 * @return
	 */
	@RequestMapping("test_abe")
	public String menuFormTest() {
		return "test/test_menu";
	}

	/**
	 * onClick CloudVisionAPI Button
	 * @return
	 */
	@RequestMapping("cloudVisionApi")
	String cloudVisionApi() {
		return "cloudVisionApiForm";
	}

	/**
	 * onClick Run Button
	 * @param model
	 * @param multipartFile
	 * @return Object
	 * @throws Exception
	 */
	@RequestMapping("cloudVisionApiRun")
	public Object cloudVisionApiRun(Model model,
			@RequestParam("uploadFile") MultipartFile multipartFile ,@RequestParam("featureType")String featureType) throws Exception{

		CloudVisionApi VisionApi = new CloudVisionApi();
		VisionApi.run(model, multipartFile,featureType);
		return "cloudVisionApiForm";
	}


}
