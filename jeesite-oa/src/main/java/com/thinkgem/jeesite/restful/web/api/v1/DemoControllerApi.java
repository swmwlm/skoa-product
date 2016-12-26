package com.thinkgem.jeesite.restful.web.api.v1;

import com.google.common.base.Preconditions;
import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.common.utils.cglib.SimpleBeanCopier;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.restful.module.TestUserModel;
import com.thinkgem.jeesite.restful.web.api.BaseController;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * API 文档地址访问路径： http://localhost:8080/swagger-ui.html
 * 文档写法可参照springfox-petstore.jar包中的controller
 * restful 规范 参照http://www.tuicool.com/articles/ruqqaiE
 */
@RestController
@RequestMapping(value = "/api/userDemos", headers = "api-version=v1")
//@RequestMapping(value = "/api1/userDemo")
@Api(
		value = "/api/userDemos",
		description = "Demo Operations about user"
)
@Scope("prototype")
public class DemoControllerApi extends BaseController {

	@Autowired
	private UserDao userDao;//临时使用dao,应该改为userService

    /*-----------------------------------restful api demo----------------------------------------------*/

	// @ApiIgnore //使用这个注解忽略这个接口
	@ApiOperation(value = "根据ProjectInfo projectInfo, String[] param1, String param2进行hello方法调用", httpMethod = "POST", produces = "application/json", consumes = "application/x-www-form-urlencoded")
	@ApiResponse(code = 200, message = "success", response = JsonResultModel.class)
	//@ApiImplicitParam(name = "projectInfo", value = "项目实体projectInfo", required = true, dataType = "ProjectInfo",paramType="query")
	@RequestMapping(value = "/hello", method = RequestMethod.POST)
	public JsonResultModel hello(@ApiParam(name = "projectInfo", value = "项目实体类", required = true) ProjectInfo projectInfo, @ApiParam(name = "param1", value = "字符数组", required = true) String[] param1, @ApiParam(name = "param2", value = "字符串", required = true) String param2) {
		try {
			Preconditions.checkNotNull(param1, "参数param1不能为空");
			Preconditions.checkNotNull(param2, "参数param2不能为空");
			jsonResultModel=new JsonResultModel();
			jsonResultModel.setStateSuccess();
			jsonResultModel.setData(new User("1", "evan"));
			System.out.println(projectInfo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("参数校验失败：", e);
			jsonResultModel.setMessage(e.getMessage());
			jsonResultModel.setStateError();
		}
		return jsonResultModel;
	}

	//-------------------Retrieve All Users------------------------------------------------------
	@ApiOperation(value = "获取用户列表", notes = "")
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public ResponseEntity<List<User>> getUserList() {
		//Page<User> page = systemService.findUser(new Page<User>(), new User());
		List<User> users = userDao.findAllList(new User());
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	//-------------------Retrieve Single User----------------------------------------------------
	@ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户详细信息")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUser(@PathVariable String id) {
		System.out.println("Fetching User with id " + id);
		User user = userDao.get(id);
		if (null == user) {
			System.out.println("User with id " + id + " not found");
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	//-------------------Create a User--------------------------------------------------------

	/**
	 * 第一种方式:使用@RequestBody注解前提是,需要客户端指定请求头Content-Type:application/json,
	 * 请求头digest算法是对json对象序列化的字符串进行摘要算法
	 * ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder)
	 * @param user
	 * @param ucBuilder
	 * @return
	 */
	@ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
	//@ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(
			@ApiParam(
					value = "Updated user object",
					required = true
			) @RequestBody User user
			, UriComponentsBuilder ucBuilder) {
		System.out.println("Creating User " + user.getName());

		try {
			System.out.println(1 / 0);
			//userDao.insert(user);//不做真实插入;抛出异常
		} catch (Exception e) {
			System.out.println("A User with name " + user.getName() + " already exist");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	/**
	 * 第二种方式:不使用@RequestBody注解,则不需要客户端指定请求头Content-Type:application/json,
	 * ResponseEntity<Void> createUser(User user, UriComponentsBuilder ucBuilder)
	 * @param user
	 * @param ucBuilder
	 * @return
	 */
	@ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
	//@ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
	@RequestMapping(value = "/second", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser1(
			@ApiParam(
					value = "Updated user object",
					required = true
			) User user
			, UriComponentsBuilder ucBuilder) {
		System.out.println("Creating User " + user.getName());

		try {
			System.out.println(1 / 0);
			//userDao.insert(user);//不做真实插入;抛出异常
		} catch (Exception e) {
			System.out.println("A User with name " + user.getName() + " already exist");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}


	//------------------- Update a User -----------------------------------------------------------------
	@ApiOperation(value = "更新用户详细信息", notes = "根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
	/*@ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    })*/
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<TestUserModel> putUser(@PathVariable String id,
												 @ApiParam(
												value = "Updated user object",
												required = true
										) @RequestBody User user) {
		System.out.println("Updating User " + id);

		User currentUser = userDao.get(id);

		if (currentUser == null) {
			System.out.println("User with id " + id + " not found");
			return new ResponseEntity<TestUserModel>(HttpStatus.NOT_FOUND);
		}

		currentUser.setName(user.getName());

		TestUserModel user1= (TestUserModel) new SimpleBeanCopier(User.class, TestUserModel.class).copy(currentUser);
		//userDao.update(user);//不做真实更新
		return new ResponseEntity<TestUserModel>(user1, HttpStatus.OK);
	}

	//------------------- Delete a User --------------------------------------------------------
	@ApiOperation(value = "删除用户", notes = "根据url的id来指定删除对象")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteUser(@PathVariable String id) {
		System.out.println("Fetching & Deleting User with id " + id);

		User user = userDao.get(id);
		if (user == null) {
			System.out.println("Unable to delete. User with id " + id + " not found");
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}

		//userDao.delete(user);//不做真实删除
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	//------------------- Delete All Users --------------------------------------------------------

	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteAllUsers() {
		System.out.println("Deleting All Users");
		//userDao.deleteAllUsers();//不做真实删除
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
}

/**
 * @RestController :首先我们使用的是Spring 4的新注解 @RestController注解,此注解避免了每个方法都要加上@ResponseBody注解,等同于
 * @Controller 和 @ResponseBody的结合体
 * @RequestBody : 如果方法参数被 @RequestBody注解，Spring将绑定HTTP请求体 到那个参数上;
 * Spring将根据请求中的ACCEPT或者 Content-Type header（私下）使用 HTTP Message converters 来
 * 将http请求体 转化为domain对象。
 * @ResponseBody : 如果方法加上了@ResponseBody注解，Spring返回值到响应体。Spring将根据请求中的
 * Content-Type header（私下）使用 HTTP Message converters 来将domain对象转换为响应体。
 * <p>
 * ResponseEntity: 是一个真实数据.它代表了整个 HTTP 响应（response）. 它的好处是你可以控制任何对象放到它内部,并可以指定状态码、
 * 头信息和响应体。它包含你想要构建HTTP Response 的信息。
 * @PathVariable : 此注解意味着一个方法参数应该绑定到一个url模板变量[在'{}'里的一个]中
 * <p>
 * MediaType : 带着 @RequestMapping 注解,通过特殊的控制器方法你可以额外指定,MediaType来生产或者消耗。
 */