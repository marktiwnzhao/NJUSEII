@startuml

actor 客户 as Customer
participant 系统 as System

activate Customer
Customer -> System : 请求购买车票
activate System
System -> System : 计算票额，生成订单，\n更新余票，更新客户积分与信用
System --> Customer : 返回订单信息

@enduml