package com.general.extension.biz.vertical.simple;

import com.general.nbhera.extension.ExtensionPoints;

/**
 * @author xvanning
 * date: 2021/7/17 16:50
 * desc: 垂直业务demo，需要进行业务解析 和 @ExtensionSession 注解
 */
public interface PostProcessorExt extends ExtensionPoints<PostProcessorExt,Object> {

    String getCurrentBiz();
}
