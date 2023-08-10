### 前端
http://seecoder-gitlab-gitlab.seec.seecoder.cn/kenny67nju/train-ticket-frontend
main分支

下载对应 zip, 解压缩, 在对应文件夹中执行 `npm i` 安装依赖.

使用 `npm run dev` 运行前端, 默认连接到位于 `http://localhost:8080` 的后端.

此处提供的前端实现**已经完成了迭代一的所有内容**, 修改不是必须的, 但你仍可以尝试使用 WebStorm 修改前端代码.

### 后端

http://seecoder-gitlab-gitlab.seec.seecoder.cn/kenny67nju/train-ticket-backend

main分之

下载对应的 zip, 解压缩, 使用 IDEA 打开对应文件夹, 确保安装并选择 **JDK 版本 >=17**, 在 IDEA 中加载 gradle 项目并等待项目构建完成.

在本地配置一个 **Postgresql 数据库**, 将对应的连接信息与凭据填写进 `application.yaml` 中的 `jpa` 下. 

点击 `L23o6Application.java` 中的绿色箭头运行后端, 如果配置正确会看到类似下面的报错信息, 这是由于部分代码仍未实现导致的正常现象, 现在你可以开始尝试编写代码了.

```
***************************
APPLICATION FAILED TO START
***************************

Description:

Parameter 0 of constructor in org.fffd.l23o6.controller.TrainController required a bean of type 'org.fffd.l23o6.service.TrainService' that could not be found.


Action:

Consider defining a bean of type 'org.fffd.l23o6.service.TrainService' in your configuration.
```
### 要求
实现后端代码中的TODO，更有已有Station车站的完整代码，完成对Route路线，Train车次的增删改查。前端代码只需要运行起来配合后端即可。

### 附录

Hibernate 的相关文档: https://hibernate.org/