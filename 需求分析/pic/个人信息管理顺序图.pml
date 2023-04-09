@startuml

actor 客户 as Customer
participant 系统 as System
activate Customer
Customer -> System: 请求修改个人信息
activate System
System -> System: 验证用户身份

alt 用户身份验证成功

System --> Customer: 展示用户的个人信息

loop
Customer -> System: 修改个人信息
System -> System: 暂存修改的个人信息
end

Customer -> System: 保存更改
System -> System: 更新信息
System --> Customer: 保存成功并展示新的用户的个人信息

else 用户身份验证失败

System --> Customer: 提示用户身份验证失败要求重新登陆

end

@enduml