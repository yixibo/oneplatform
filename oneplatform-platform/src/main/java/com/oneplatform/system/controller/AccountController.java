package com.oneplatform.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesuite.springweb.model.WrapperResponse;
import com.oneplatform.base.LoginContext;
import com.oneplatform.base.exception.AssertUtil;
import com.oneplatform.base.model.PageResult;
import com.oneplatform.platform.auth.AuthPermHelper;
import com.oneplatform.system.dao.entity.AccountEntity;
import com.oneplatform.system.dto.param.AccountParam;
import com.oneplatform.system.dto.param.AccountQueryParam;
import com.oneplatform.system.dto.param.AssignmentParam;
import com.oneplatform.system.dto.param.SwitchParam;
import com.oneplatform.system.dto.param.UpdatepasswordParam;
import com.oneplatform.system.service.AccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("/account")
@Api("Account Management API")
public class AccountController {

	private @Autowired AccountService accountService;
	
	@ApiOperation(value = "分页查询账号列表")
	@RequestMapping(value = "list", method = RequestMethod.POST)
    public @ResponseBody PageResult<AccountEntity> pageQueryAccounts(@ModelAttribute AccountQueryParam param) {
		PageResult<AccountEntity> page = accountService.pageQuery(param,param);
		return page;
	}
	
	@ApiOperation(value = "按id查询")
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody WrapperResponse<AccountEntity> getById(@PathVariable("id") int id) {
		AccountEntity entity = accountService.findById(id);
		return new WrapperResponse<>(entity);
	}
	
	@ApiOperation(value = "新增账号")
	@RequestMapping(value = "add", method = RequestMethod.POST)
    public @ResponseBody WrapperResponse<String> addAccount(@RequestBody AccountParam param) {
		accountService.addAccount(LoginContext.getLoginUserId(), param);
		return new WrapperResponse<>();
	}
	
	@ApiOperation(value = "更新账号")
	@RequestMapping(value = "update", method = RequestMethod.POST)
    public @ResponseBody WrapperResponse<String> updateAccount(@RequestBody AccountParam param) {
		AssertUtil.notInitData(param.getId());
		accountService.updateAccount(LoginContext.getLoginUserId(), param);
		return new WrapperResponse<>();
	}
	
	@ApiOperation(value = "删除账户")
	@RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    public @ResponseBody WrapperResponse<String> deleteAccount(@PathVariable("id") int id) {
		AssertUtil.notInitData(id);
		accountService.deleteAccount(LoginContext.getLoginUserId(), id);
		return new WrapperResponse<>();
	}
	
	@ApiOperation(value = "更新密码")
	@RequestMapping(value = "update/password", method = RequestMethod.POST)
    public @ResponseBody WrapperResponse<String> updatePassword(@RequestBody UpdatepasswordParam param) {
		Integer loginUserId = LoginContext.getLoginUserId();
		param.setUserId(loginUserId);
		accountService.updatePassword(loginUserId, param);
		return new WrapperResponse<>();
	}
	
	@ApiOperation(value = "启用/停止账号")
	@RequestMapping(value = "switch", method = RequestMethod.POST)
    public @ResponseBody WrapperResponse<String> switchAccount(@RequestBody SwitchParam param) {
		AssertUtil.notInitData(param.getId());
		accountService.switchAccount(LoginContext.getLoginUserId(), param.getId(),param.getValue());
		return new WrapperResponse<>();
	}
	
	@ApiOperation(value = "分配角色")
	@RequestMapping(value = "assignment/roles", method = RequestMethod.POST)
    public @ResponseBody WrapperResponse<String> assignmentRoles(@RequestBody AssignmentParam param) {
		AssertUtil.notInitData(param.getId());
		accountService.assignmentRoles(LoginContext.getLoginUserId(), param.getId(), param.getAssignmentIds());
		//
		AuthPermHelper.refreshPermData(param.getId());
		return new WrapperResponse<>();
	}
    
}
