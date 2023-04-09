@startuml
[*] --> 未登录
未登录 --> 已登录 : 登录
已登录 --> 查看信息 : 查看
已登录 --> 编辑信息 : 修改
编辑信息 --> 已登录 : 保存修改
查看信息 --> 已登录 : 返回
查看信息 --> 编辑信息 : 修改
编辑信息 --> 编辑信息 : 编辑
已登录 --> [*] : 退出登录
@enduml