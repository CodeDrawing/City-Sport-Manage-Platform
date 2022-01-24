package top.codezx.system.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import top.codezx.common.constant.ControllerConstant;
import top.codezx.common.plugin.logging.aop.annotation.Logging;
import top.codezx.common.plugin.logging.aop.enums.BusinessType;
import top.codezx.common.tools.DateTimeUtil;
import top.codezx.common.tools.SecurityUtil;
import top.codezx.common.web.base.BaseController;
import top.codezx.common.web.domain.request.PageDomain;
import top.codezx.common.web.domain.response.Result;
import top.codezx.common.web.domain.response.module.ResultTable;
import top.codezx.system.domain.SysArrivalInfo;
import top.codezx.system.domain.SysPlace;

import top.codezx.system.domain.SysUser;
import top.codezx.system.service.ISysPlaceService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags = {"场所管理"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "place")
public class SysPlaceController extends BaseController {
    /**
     * Describe: 基础路径
     */
    private static String MODULE_PATH = "system/place/";

    @Resource
    private ISysPlaceService iSysPlaceService;


    /**
     * Describe: 获取场所列表视图
     * Param ModelAndView
     * Return 场所列表视图
     */
    @GetMapping("main")
    @ApiOperation(value = "获取场所列表视图")
    @PreAuthorize("hasPermission('/system/place/main','sys:place:main')")
    public ModelAndView main() {
        return jumpPage(MODULE_PATH + "main");
    }

    /**
     * Describe: 跳转到打卡登陆
     * Param ModelAndView
     * Return 打卡登陆页面
     */
    @GetMapping("toArrivalLogin/{placeName}/{placeId}")
//    @ApiOperation(value = "打卡登陆")
//    @PreAuthorize("hasPermission('/system/place/toArrivalLogin','sys:place:toArrivalLogin')")
    public ModelAndView toArrivalLogin(HttpSession session, Model model, @PathVariable("placeName")String placeName, @PathVariable("placeId")String placeId) {
        model.addAttribute("placeName",placeName);
        model.addAttribute("placeId",placeId);
        return jumpPage(MODULE_PATH + "arrivalLogin");
    }

    /**
     * Describe: 打卡登陆
     * Param ModelAndView
     * Return 成功或者失败页面
     */
    @RequestMapping("arrivalLogin")
//    @ApiOperation(value = "打卡登陆")
//    @PreAuthorize("hasPermission('/system/place/toArrivalLogin','sys:place:toArrivalLogin')")
    public Result arrivalLogin(@RequestParam("username")String username,
                               @RequestParam("password")String password,
                               @RequestParam("placeName")String placeName,
                               @RequestParam("placeId")String placeId){
//          根据username查询出该用户的密码，然后得到用户信息，返回到这层
        SysUser sysUser = iSysPlaceService.arrivalLogin(username);
//        根据BCryptPasswordEncoder类中的matches方法对密码进行判断，因为数据库中的密码是加密了的，所以要这样进行判断

        boolean result = new BCryptPasswordEncoder().matches(password, sysUser.getPassword());
        if(result==true){
            //把时间格式设置为年月日的规范型
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//            判断数据库中有没有该场馆今日的到场信息，如果有，就直接添加，否则就插入再添加
            SysArrivalInfo arrivalInfo = iSysPlaceService.alreadyHaveTheDate(sdf.format(new Date()),placeName);
      //            已经有了？那就根据生日得到年龄
            if(arrivalInfo!=null){
                //days表示天数
                long days = DateTimeUtil.getYear(new Date(),sysUser.getBirthday());
                //days除365表示多少岁（大概）
                long year=days/365;
        System.out.println(year);
                //分别判断小于18，小于30，小于60，和60以上的用户
                if(year<18){
                    iSysPlaceService.updateUnder18(arrivalInfo);
                }else if(year<30){
                    iSysPlaceService.updateUnder18To30(arrivalInfo);
                }else if(year<60){
                    iSysPlaceService.updateUnder31To60(arrivalInfo);
                }else{
                    iSysPlaceService.updateAbove61(arrivalInfo);
                }
            }
        }
        return decide(result);
    }

    /**
     * Describe: 登陆成功
     * Param ModelAndView
     * Return 成功页面
     */
    @RequestMapping("/to/success")
    public ModelAndView toSuccess(){
        return jumpPage(MODULE_PATH + "success");
    }
    /**
     * Describe: 登陆失败
     * Param ModelAndView
     * Return 失败页面
     */
    @RequestMapping("/to/fail")
    public ModelAndView toFail(){
        return jumpPage(MODULE_PATH + "fail");
    }



    /**
     * Describe: 获取场所列表数据
     * Param ModelAndView
     * Return 场所列表数据
     */
    @GetMapping("data")
    @ApiOperation(value = "获取场所列表数据")
    @PreAuthorize("hasPermission('/system/place/data','sys:place:data')")
    @Logging(title = "查询场所", describe = "查询场所", type = BusinessType.QUERY)
    public ResultTable data(PageDomain pageDomain, SysPlace param) {
        PageInfo<SysPlace> pageInfo = iSysPlaceService.page(param, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }
    /**
     * Describe: 生成二维码
     * Param ModelAndView
     * Return 跳转到生成二维码页面
     */
    @GetMapping("qrInfo/{placeId}")
    @ApiOperation(value = "生成二维码")
    @PreAuthorize("hasPermission('/system/place/qrInfo','sys:place:qrInfo')")
    @Logging(title = "生成二维码", describe = "生成二维码", type = BusinessType.QUERY)
    public ModelAndView qrInfo(Model model,@PathVariable("placeId") String placeId) {
        SysPlace sysPlace = iSysPlaceService.selectById(placeId);
        model.addAttribute("qrInfo",sysPlace.getQRInfo());
        model.addAttribute("placeName",sysPlace.getPlaceName());
        return jumpPage(MODULE_PATH+"placeQR");
    }




}
