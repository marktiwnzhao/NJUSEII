@startuml

actor 客户 as Customer
participant 系统 as System
activate Customer
Customer -> System: 请求修改个人信息
activate System
System -> System: 验证用户身份

alt 用户身份验证成功

System --> Customer: 展示用户的个人信息
activate Customer
opt 用户选择修改个人信息

loop 修改个人信息
Customer -> System: 输入新的个人信息

alt 个人信息输入验证成功
System -> System: 暂存修改的个人信息
System --> Customer: 显示用户修改后的信息
else 个人信息输入验证失败
System --> Customer: 提示用户重新输入信息

end
end

Customer -> System: 确认保存
System -> System: 更新信息
System --> Customer: 保存成功并展示新的用户的个人信息

end
else 用户身份验证失败

System --> Customer: 提示用户身份验证失败要求重新登陆


end

@enduml