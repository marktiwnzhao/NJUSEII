@startuml

actor 铁路信息管理员 as RailwayInfoManager
participant 系统 as System

activate RailwayInfoManager
RailwayInfoManager -> System : 请求线路信息
activate System
System -> System: 验证用户身份

alt 用户身份验证成功

System --> RailwayInfoManager: 返回线路信息
RailwayInfoManager -> System: 选择某条线路或新增线路
System --> RailwayInfoManager: 返回线路详细信息

loop
RailwayInfoManager -> System: 修改线路信息
System -> System: 暂存修改的线路信息
end

RailwayInfoManager -> System: 提交线路信息
System -> System: 更新信息，\n包括相关车票信息
System --> RailwayInfoManager: 提示线路提交成功并返回线路信息

else 用户身份验证失败

System --> RailwayInfoManager: 提示用户身份验证失败要求重新登陆

end

@enduml