package top.codezx.system.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import top.codezx.common.constant.ControllerConstant;
import top.codezx.common.plugin.logging.aop.annotation.Logging;
import top.codezx.common.plugin.logging.aop.enums.BusinessType;
import top.codezx.common.plugin.submit.annotation.RepeatSubmit;
import top.codezx.common.tools.DateTimeUtil;
import top.codezx.common.tools.SecurityUtil;
import top.codezx.common.tools.SequenceUtil;
import top.codezx.common.web.base.BaseController;
import top.codezx.common.web.domain.request.PageDomain;
import top.codezx.common.web.domain.response.Result;
import top.codezx.common.web.domain.response.module.ResultTable;
import top.codezx.system.domain.SysArrivalInfo;
import top.codezx.system.domain.SysConfig;
import top.codezx.system.domain.SysPlace;

import top.codezx.system.domain.SysUser;
import top.codezx.system.service.ISysConfigService;
import top.codezx.system.service.ISysPlaceService;
import top.codezx.system.service.ISysUserService;

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
    @Resource
    private ISysUserService iSysUserService;
    @Resource
    private ISysConfigService iSysConfigService;


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
//          根据username查询出该用户的密码，然后得 到用户信息，返回到这层
        SysUser sysUser = iSysPlaceService.arrivalLogin(username);
//        根据BCryptPasswordEncoder类中的matches方法对密码进行判断，因为数据库中的密码是加密了的，所以要这样进行判断

        boolean result = new BCryptPasswordEncoder().matches(password, sysUser.getPassword());
        if(result==true){
            //把时间格式设置为年月日的规范型
            //把时间格式设置为年月日的规范型
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//            判断数据库中有没有该场馆今日的到场信息，如果有，就直接添加，否则就插入再添加
            SysArrivalInfo arrivalInfo = iSysPlaceService.alreadyHaveTheDate(sdf.format(new Date()),placeName);
      //            已经有了？那就根据生日得到年龄
            if(arrivalInfo==null){
                SysArrivalInfo sysArrivalInfo = new SysArrivalInfo();
                sysArrivalInfo.setArrivalInfoId(SequenceUtil.makeStringId());
                sysArrivalInfo.setPlaceName(placeName);
                sysArrivalInfo.setPlaceId(placeId);
                sysArrivalInfo.setDate(new Date());
                iSysPlaceService.insertArrivalInfo(sysArrivalInfo);
            }
            /**
             * 对年龄分类
             */
            arrivalInfo = iSysPlaceService.alreadyHaveTheDate(sdf.format(new Date()),placeName);

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
            /**
             * 对性别分类
             */
            if(sysUser.getSex()=="0"){
                iSysPlaceService.updateManNumber(arrivalInfo);
            }else{
                iSysPlaceService.updateWomanNumber(arrivalInfo);
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
    @GetMapping("add")
    @ApiOperation(value="跳转到添加场馆信息页面")
    @PreAuthorize("hasPermission('/system/place/add','sys:place:add')")
    @Logging(title="跳转到添加场馆信息页面",describe = "跳转到添加场馆信息页面",type = BusinessType.OTHER)
    public ModelAndView add(Model model){
        List<SysUser> list = iSysUserService.list(null);
        model.addAttribute("users",list);
        System.out.println("====="+list);
        return jumpPage(MODULE_PATH+"add");
    }

    @RepeatSubmit
    @PostMapping("save")
    @ApiOperation(value = "保存场馆信息")
    @PreAuthorize("hasPermission('/system/place/add','sys:place:add')")
    @Logging(title = "保存场馆信息",describe = "保存场馆信息",type = BusinessType.ADD)
    public Result save(@RequestBody SysPlace sysPlace){
        SysConfig local_add = iSysConfigService.getByCode("local_add");
        sysPlace.setPlaceId(SequenceUtil.makeStringId());
        sysPlace.setCreateDate(new Date());
        sysPlace.setQRInfo("http://"+local_add.getConfigValue()+"/system/place/toArrivalLogin/"+sysPlace.getPlaceName()+"/"+sysPlace.getPlaceId());
        boolean result = iSysPlaceService.insert(sysPlace);
        return decide(result);
    }
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping("remove/{id}")
    @ApiOperation(value = "删除场馆数据")
    @PreAuthorize("hasPermission('/system/place/remove','sys:place:remove')")
    @Logging(title = "删除场馆", describe = "删除场馆", type = BusinessType.REMOVE)
    public Result remove(@PathVariable String id){
        boolean result = iSysPlaceService.deleteById(id);
        return decide(result);
    }

    @DeleteMapping("batchRemove/{ids}")
    @ApiOperation(value = "批量删除场馆数据")
    @PreAuthorize("hasPermission('/system/place/remove','sys:place:remove')")
    @Logging(title = "删除场馆", describe = "删除场馆", type = BusinessType.REMOVE)
    public Result batchRemove(@PathVariable String ids){
        boolean result = iSysPlaceService.batchRemove(ids.split(","));
        return decide(result);
    }
    @GetMapping("edit/{id}")
    @ApiOperation(value = "获取场所修改视图")
    @PreAuthorize("hasPermission('/system/place/edit','sys:place:edit')")
    public ModelAndView edit(Model model,@PathVariable("id") String placeId){
        System.out.println(placeId);
        List<SysUser> list = iSysUserService.list(null);
        model.addAttribute("users",list);
        model.addAttribute("sysPlace",iSysPlaceService.selectById(placeId));
        System.out.println(iSysPlaceService.selectById(placeId));
        return jumpPage(MODULE_PATH+"edit");
    }



}
