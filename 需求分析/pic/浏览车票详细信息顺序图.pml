@startuml

actor 客户 as Customer
participant 系统 as System
activate Customer
Customer -> System: 选择出发地和目的地、出行日期
activate System
System -> System: 查询满足条件的余票列表
System -> Customer: 返回满足条件的余票列表
alt 需要查看车票详细信息
Customer -> System: 选定某一张车票
System -> System: 查询选定车票的详细信息
System -> Customer: 返回车票详细信息
else 不需要查看车票详细信息
Customer -> System: 退出
end

@enduml
