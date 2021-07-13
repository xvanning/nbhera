# nbhera

## 软件代码发展的形态
1、单个功能的抽象化与标准化
2、多个功能的组合与模板化
3、多个业务的策略化与模板的排列组合
4、多个策略的智能化

## 扩展点的演进过程：
1、首先是 getByCode，一个接口多个实现，放在一个 Map 里面
2、其次是 有策略的获取实现类 ===》 support 
    2.1、 根据 策略模式 code ，直接获取实现类
    2.2、 根据 业务模板 bizCode 和 横向模板code
    
其中 2.1 和 2.2 就是对应的extension的所有类和实现。 当扩展点很多的时候，就需要模板来进行方便管理。

## 主要类的演进过程
extension（扩展点的注册、创建、获取） ==》 template（模板） ==》bizCode（业务code） ==》 bizSession（调用session）  ==》 bizSessionAop（为了方便写代码，类似于@ExtensionSession）


## 名词解释
Extension: 扩展点。表示各个模块接口的具体实现
template: 分为业务模板、水平模板。其中业务模板是业务专属的，水平模板是可以供给多个业务使用。一个模板会有多个扩展点。


## 各类的组成
Extension: ExtensionDefinition(拥有support方法) ==》 ExtensionCache （第一层封装） ==》 RegisterCenter

Biz: BizSession ==》 BizSessionScope
       ||
     BizCacheData
