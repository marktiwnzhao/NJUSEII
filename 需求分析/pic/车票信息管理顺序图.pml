@startuml

actor 铁路信息管理员 as RailwayInfoManager
participant 系统 as System

activate RailwayInfoManager
RailwayInfoManager -> System : 选择出发地和目的地、日期
activate System
System -> System: 验证用户身份

alt 用户身份验证成功

System --> RailwayInfoManager: 返回车票信息
RailwayInfoManager -> System: 选择某车次或新增车次
System --> RailwayInfoManager: 返回车次详细信息

loop
RailwayInfoManager -> System: 修改车票信息
System -> System: 暂存修改的车票信息
end

RailwayInfoManager -> System: 提交车票信息
System -> System: 更新信息，\n并提醒已购票客户
System --> RailwayInfoManager: 提示提交成功并返回车票信息

else 用户身份验证失败

System --> RailwayInfoManager: 提示用户身份验证失败要求重新登陆

end

@enduml