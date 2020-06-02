> 发现都是换取到token令牌后，然后去访问淘宝进行爬取的，感觉太麻烦了，换了一个比较傻瓜式的方法。
> 使用java+selenium+swing做的一个小桌面软件，用于爬取淘宝首页数据。

博客地址：[笑笑庄](https://blog.csdn.net/dubismile/article/details/106498538)





# 项目说明
## 界面说明
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020060216303863.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2R1YmlzbWlsZQ==,size_16,color_FFFFFF,t_70#pic_center)   

  1. 淘宝账号和淘宝密码是用来登陆账号使用的，可能中途需要输入手机验证码登录，建议第一次先输入验证码后；
  2. 浏览器、浏览器路径和驱动路径是以谷歌浏览器和火狐浏览器为主。**注意浏览器版本要和驱动对应**
    - chrome浏览器使用chromedriver。
     - firefox浏览器使用geckodriver
  3. 获取接口地址是用来获取查询搜索词汇。
  4. 提交信息地址是用来将查询到的商品数据上传到平台。

## 流程说明
```mermaid
sequenceDiagram
爬虫 ->> 淘宝: 访问淘宝，进行登录 
淘宝 -->> 爬虫: 登录成功，进入查询准备
loop 查询
	爬虫->> 平台: 给个搜索词汇吧
	平台-->>爬虫: 给你个 666 吧
	Note right of 平台: 没有词汇要查了，跳出循环
	爬虫->> 淘宝: 查询 666 按照销量查询
  	淘宝-->> 爬虫: 显示结果
	爬虫-x 平台: 将结果提交给平台
end 
```