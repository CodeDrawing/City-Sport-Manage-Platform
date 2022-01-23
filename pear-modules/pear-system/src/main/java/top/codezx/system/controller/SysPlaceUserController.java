package top.codezx.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Api(tags = {"场所使用者"})
@RequestMapping("placeUser")
public class SysPlaceUserController {

    /**
     * Describe: 基础路径
     */
    private static String MODULE_PATH = "placeUser/";

    @GetMapping("ScanQRCode")
    @ApiOperation(value = "获取用户列表视图")
    @PreAuthorize("hasPermission('/placeUser/ScanQRCode','placeUser:ScanQRCode')")
    public ModelAndView main() {
        return jumpPage(MODULE_PATH + "ScanQRCode");
    }

    /**
     * 页面跳转
     */
    public ModelAndView jumpPage(String path) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(path);
        return modelAndView;
    }
}
