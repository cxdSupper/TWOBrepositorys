package com.cxd.cmcc.controller;

import com.cxd.cmcc.Constant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

/**
 * @author caixd
 * @date 2025/8/19
 * @desc
 */
@RestController
@RequestMapping("variable")
public class VariableController {

    @RequestMapping("putAutoList")
    public String putAutoList(String name) {
        Object autoList = Constant.getValue("autoList");
        if (autoList == null) {
            autoList = new HashSet<>();
        }
        ((HashSet<String>) autoList).add(name);
        Constant.setValue("autoList", autoList);
        return autoList.toString();
    }

    @RequestMapping("deleteAutoList")
    public String deleteAutoList(String name) {
        Object autoList = Constant.getValue("autoList");
        if (autoList == null) {
            autoList = new HashSet<>();
        }
        ((HashSet<String>) autoList).remove(name);
        Constant.setValue("autoList", autoList);
        return autoList.toString();
    }
}
