@startuml

actor 客户 as Customer
participant 系统 as System

activate Customer
Customer -> System : 请求购买车票
activate System
System -> Customer: 展示车票细节信息
System -> System: 检查用户积分和信用
alt 用户积分和信用足够
System -> System: 标记车票为已售出状态
System -> Customer: 购票成功，展示购票信息
else 金额不足,用户积分或信用不足等
System -> Customer: 购票失败，提示用户
end
@enduml