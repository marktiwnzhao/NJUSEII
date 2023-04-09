@startuml

actor 客户 as Customer
participant 系统 as System
activate Customer
Customer -> System: 选择出发地和目的地、出行日期
activate System
System -> Customer: 展示满足条件的余票列表
Customer -> System: 选定某一张车票
System -> Customer: 展示车票细节信息

@enduml